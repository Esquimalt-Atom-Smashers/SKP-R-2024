package org.firstinspires.ftc.teamcode.auto;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.FunctionalCommand;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.commands.AutoPlaceYellowCommand;
import org.firstinspires.ftc.teamcode.commands.CommandManager;
import org.firstinspires.ftc.teamcode.commands.MoveCommand;
import org.firstinspires.ftc.teamcode.commands.MoveElbowCommand;

public class AutonomousController {
    enum AutonomousState {
        MOVING_TO_SPIKE_MARKS,
        PLACING_PURPLE,
        MOVING_TO_BACKDROP,
        MOVING_TO_PLACE_YELLOW,
        TURNING_CORRECT_DIRECTION,
        IDLE
    }

    private final HardwareMap hardwareMap;
    private final Telemetry telemetry;
    private final Robot robot;
    private final CommandManager commandManager;

    private Command currentCommand;
    private AutonomousState state;

    private final AutoPosition autoPosition;

    public AutonomousController(OpMode opMode, boolean isBlueAlliance, boolean isUpstage, boolean isPlacingYellow) {
        this.hardwareMap = opMode.hardwareMap;
        this.telemetry = opMode.telemetry;

        robot = new Robot(opMode, true, false);
//        robot.getElbowSubsystem().getTelemetry().addData("From elbow, position", robot.getElbowSubsystem().getPosition()).setRetained(true);
//        robot.getLinearSlideSubsystem().getTelemetry().addData("From slide, position", robot.getLinearSlideSubsystem().getPosition()).setRetained(true);

        commandManager = new CommandManager(robot);
        autoPosition = new AutoPosition(isBlueAlliance, isPlacingYellow, isUpstage);

        if (isBlueAlliance) robot.getLedSubsystem().setBlue();
        else robot.getLedSubsystem().setRed();

//        robot.getHangingSubsystem().setDefaultCommand(commandManager.getAutoDefaultHangingCommand());
    }

    public void start() {
        state = AutonomousState.MOVING_TO_SPIKE_MARKS;
//        Command wait = new WaitCommand(10);
//        scheduleCommand(new SequentialCommandGroup(
//                new MoveCommand(robot.getDriveSubsystem(), MoveCommand.MovementType.DRIVE, 10),
//                new MoveElbowCommand(robot.getElbowSubsystem(), 3000),
//                wait
//        ));
//        currentCommand = wait;
        scheduleCommand(commandManager.getAutoSetupCommand());
    }

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
                    state = autoPosition.isPlacingYellow ? AutonomousState.MOVING_TO_BACKDROP : AutonomousState.IDLE;
                    scheduleCommand(commandManager.getAutoDriveFromPurpleCommand(autoPosition));
                }
                break;
            case MOVING_TO_BACKDROP:
                if (canContinue()) {
                    state = AutonomousState.MOVING_TO_PLACE_YELLOW;
//                    Command test = new WaitCommand(1);
//                    scheduleCommand(new SequentialCommandGroup(
//                            new InstantCommand(() -> robot.getElbowSubsystem().moveManually(0.3), robot.getElbowSubsystem()),
//                            new WaitCommand(1000),
//                            new InstantCommand(() -> robot.getElbowSubsystem().moveManually(0), robot.getElbowSubsystem()),
//                            new InstantCommand(() -> robot.getElbowSubsystem().stopMotor(), robot.getElbowSubsystem()),
//                            test
//                    ));
//                    currentCommand = test;
                    scheduleCommand(commandManager.getAutoPlaceYellowAndHideCommand(autoPosition));
//                    scheduleCommand(new AutoPlaceYellowCommand(robot.getElbowSubsystem(), robot.getLinearSlideSubsystem(), robot.getBoxSubsystem()));
                }

                break;
            case MOVING_TO_PLACE_YELLOW:
                if (canContinue()) {
                    state = AutonomousState.IDLE;
                }
                break;
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
        robot.getLinearSlideSubsystem().printData();
        telemetry.update();
    }

    public AutoPosition.SpikeMark getSpikeMark() {
        return autoPosition.spikeMark;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    private boolean canContinue() {
        return currentCommand.isFinished();
    }

    private void scheduleCommand(Command command) {
        if (command.isScheduled()) {
            robot.getTelemetry().addData("Command already scheduled", command).setRetained(true);
            return;
        }
        robot.getTelemetry().addData("Scheduling command", command.getName()).setRetained(true);
        currentCommand = command;
        CommandScheduler.getInstance().schedule(command);
    }
}
