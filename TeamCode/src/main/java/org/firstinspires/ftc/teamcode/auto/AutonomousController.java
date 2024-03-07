package org.firstinspires.ftc.teamcode.auto;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.commands.CommandManager;

/**
 * A class that is used to move the robot and arm during autonomous.
 */
public class AutonomousController {
    enum AutonomousState {
        MOVING_TO_SPIKE_MARKS,
        PLACING_PURPLE,
        MOVING_TO_BACKDROP,
        MOVING_TO_PLACE_YELLOW,
        TURNING_CORRECT_DIRECTION,
        IDLE
    }

    private final Telemetry telemetry;
    private final Robot robot;
    private final CommandManager commandManager;

    private Command currentCommand;
    private AutonomousState state;

    private final AutoPosition autoPosition;

    /**
     * Constructs a new AutonomousController, which initialized a new robot, command manager, and auto position.
     *
     * @param opMode The opMode that created this
     * @param isBlueAlliance Whether we are on the blue alliance
     * @param isUpstage Whether we are upstage
     * @param isPlacingYellow Whether we want to place the yellow pixel
     * @param isParkingFromDownstage Whether we want to park upstage from downstage
     */
    public AutonomousController(OpMode opMode, boolean isBlueAlliance, boolean isUpstage, boolean isPlacingYellow, boolean isParkingFromDownstage) {
        this.telemetry = opMode.telemetry;

        robot = new Robot(opMode, true, false);

        commandManager = new CommandManager(robot);
        autoPosition = new AutoPosition(isBlueAlliance, isPlacingYellow, isUpstage, isParkingFromDownstage);

        if (isBlueAlliance) robot.getLedSubsystem().setSolidBlue();
        else robot.getLedSubsystem().setSolidRed();
    }

    /**
     * Schedules the first command used in autonomous.
     */
    public void start() {
        state = AutonomousState.MOVING_TO_SPIKE_MARKS;
        scheduleCommand(commandManager.getAutoSetupCommand());
    }

    /**
     * Runs the autonomous manager. If the current command is finished, schedule the next command and switches the state.
     * Also runs the command scheduler, and updates the telemetry.
     */
    public void run() {
        switch (state) {
            case MOVING_TO_SPIKE_MARKS:
                if (canContinue()) {
                    if (robot.getDistanceSensorSubsystem().isLeftBlocked())
                        autoPosition.setSpikeMark(autoPosition.isBlue ? AutoPosition.SpikeMark.UPSTAGE : AutoPosition.SpikeMark.DOWNSTAGE);
                    else if (robot.getDistanceSensorSubsystem().isRightBlocked())
                        autoPosition.setSpikeMark(autoPosition.isBlue ? AutoPosition.SpikeMark.DOWNSTAGE : AutoPosition.SpikeMark.UPSTAGE);
                    else
                        autoPosition.setSpikeMark(AutoPosition.SpikeMark.MIDDLE);
                    state = AutonomousState.PLACING_PURPLE;
                    scheduleCommand(commandManager.getAutoDriveAndPlacePurpleCommand(autoPosition));
                }
                break;
            case PLACING_PURPLE:
                if (canContinue()) {
                    // AutoDriveFromPurpleCommand is the last command that gets run for certain
                    state = autoPosition.isPlacingYellow ? AutonomousState.MOVING_TO_BACKDROP : AutonomousState.IDLE;
                    if (autoPosition.isUpstage) scheduleCommand(commandManager.getAutoDriveFromPurpleCommand(autoPosition));
                    else scheduleCommand(commandManager.getParkAtBackdropCommand(autoPosition));
                }
                break;
            case MOVING_TO_BACKDROP:
                if (canContinue()) {
                    state = AutonomousState.MOVING_TO_PLACE_YELLOW;
                    scheduleCommand(commandManager.getAutoPlaceYellowAndHideCommand(autoPosition));
                }

                break;
            case MOVING_TO_PLACE_YELLOW:
            case TURNING_CORRECT_DIRECTION:
                if (canContinue()) {
                    state = AutonomousState.IDLE;
                }
                break;
            case IDLE:
                break;
        }
        CommandScheduler.getInstance().run();
        telemetry.addData("State", state);
        telemetry.addData("Current command", currentCommand.getName());
//        robot.getLinearSlideSubsystem().printData();
        telemetry.update();
    }

    /** @return The autonomous position, which contains information about our starting position */
    @SuppressWarnings("unused")
    public AutoPosition.SpikeMark getSpikeMark() {
        return autoPosition.spikeMark;
    }

    /** @return The command manager */
    @SuppressWarnings("unused")
    public CommandManager getCommandManager() {
        return commandManager;
    }

    /**
     * Checks if the current command is finished or not.
     *
     * @return True if the current command has finished, false otherwise
     */
    private boolean canContinue() {
        return currentCommand.isFinished();
    }

    /**
     * Schedules a command to run. Also set the current command to the one passed in.
     *
     * @param command The command to run
     */
    private void scheduleCommand(Command command) {
        if (command.isScheduled()) {
            return;
        }
        currentCommand = command;
        CommandScheduler.getInstance().schedule(command);
    }
}
