package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.auto.AutoPosition;
import org.firstinspires.ftc.teamcode.commands.MoveCommand.MovementType;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;

/**
 * Command that drives from where we placed the purple pixel to the next spot,
 * either the backdrop or just facing the correct direction.
 */
public class AutoDriveFromPurpleCommand extends SequentialCommandGroup {
    private final Command lastCommand;

    /**
     * Creates a command that drives from where we placed the purple pixel to the next spot,
     * either the backdrop or just facing the correct direction.
     *
     * @param driveSubsystem A reference to the driveSubsystem
     * @param autoPosition The starting auto position
     */
    public AutoDriveFromPurpleCommand(DriveSubsystem driveSubsystem, AutoPosition autoPosition) {
        lastCommand = new WaitCommand(1);

        // If we don't want to place the yellow, just turn to face the correct direction, then stop
        if (!autoPosition.isPlacingYellow) {
            addCommands(
                    new MoveCommand(driveSubsystem, MovementType.TURN_TO_HEADING, 0),
                    new MoveCommand(driveSubsystem, MovementType.SLOW_TURN_TO_HEADING, 0)
            );
        }
        else placingUpstage(driveSubsystem, autoPosition);

        addCommands(lastCommand);
    }

    private void placingUpstage(DriveSubsystem driveSubsystem, AutoPosition autoPosition) {
        if (autoPosition.spikeMark == AutoPosition.SpikeMark.UPSTAGE) {
            addCommands(
                    new MoveCommand(driveSubsystem, MovementType.TURN, autoPosition.flip(90)),
                    new MoveCommand(driveSubsystem, MovementType.DRIVE, 25),
                    new MoveCommand(driveSubsystem, MovementType.STRAFE, autoPosition.flip(5))
            );
        } else if (autoPosition.spikeMark == AutoPosition.SpikeMark.MIDDLE) {
            addCommands(
                    new MoveCommand(driveSubsystem, MovementType.TURN, autoPosition.flip(90)),
                    new MoveCommand(driveSubsystem, MovementType.DRIVE, 32),
                    new MoveCommand(driveSubsystem, MovementType.STRAFE, autoPosition.flip(6)),
                    new MoveCommand(driveSubsystem, MovementType.TURN_TO_HEADING, autoPosition.flip(90))
            );
        } else if (autoPosition.spikeMark == AutoPosition.SpikeMark.DOWNSTAGE) {
            addCommands(
                    new MoveCommand(driveSubsystem, MovementType.DRIVE, -4),
                    new MoveCommand(driveSubsystem, MovementType.TURN, autoPosition.flip(180)),
                    new MoveCommand(driveSubsystem, MovementType.DRIVE, 26)
            );
        }
    }

    @Override
    public boolean isFinished() {
        return lastCommand.isFinished();
    }
}