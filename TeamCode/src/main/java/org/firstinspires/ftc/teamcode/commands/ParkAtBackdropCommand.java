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
public class ParkAtBackdropCommand extends SequentialCommandGroup {
    private final Command lastCommand;

    /**
     * Creates a command that drives from where we placed the purple pixel to the next spot,
     * either the backdrop or just facing the correct direction.
     *
     * @param driveSubsystem A reference to the driveSubsystem
     * @param autoPosition The starting auto position
     */
    public ParkAtBackdropCommand(DriveSubsystem driveSubsystem, AutoPosition autoPosition) {
        lastCommand = new WaitCommand(1);

        placingDownstage(driveSubsystem, autoPosition);

        addCommands(lastCommand);
    }
    private void placingDownstage(DriveSubsystem driveSubsystem, AutoPosition autoPosition) {
        if (autoPosition.isParkingFromDownstage) {
            if(autoPosition.spikeMark == AutoPosition.SpikeMark.DOWNSTAGE)
                addCommands(
                    // Strafe right
                    new MoveCommand(driveSubsystem, MovementType.STRAFE, autoPosition.flip(-20)),
                    // Turn to heading
                    new MoveCommand(driveSubsystem, MovementType.TURN_TO_HEADING, autoPosition.flip(90)),
                    new MoveCommand(driveSubsystem, MovementType.SLOW_TURN_TO_HEADING, autoPosition.flip(90)),
                    // Wait for 5 seconds to allow our teammate to do whatever
                    new WaitCommand(5000),
                    // Drive forwards
                    new MoveCommand(driveSubsystem, MovementType.DRIVE, 85),
                    // Turn to heading
                    new MoveCommand(driveSubsystem, MovementType.TURN_TO_HEADING, autoPosition.flip(0)),
                    // Turn to heading
                    new MoveCommand(driveSubsystem, MovementType.SLOW_TURN_TO_HEADING, autoPosition.flip(0)),
                    // Move backwards
                    new MoveCommand(driveSubsystem, MovementType.DRIVE, -5)
            );
            else if (autoPosition.spikeMark == AutoPosition.SpikeMark.MIDDLE)
                addCommands(
                    // Turn to heading
                    new MoveCommand(driveSubsystem, MovementType.TURN_TO_HEADING, autoPosition.flip(90)),
                    new MoveCommand(driveSubsystem, MovementType.SLOW_TURN_TO_HEADING, autoPosition.flip(90)),
                    // Wait for 5 seconds to allow our teammate to do whatever
                    new WaitCommand(5000),
                    // Drive forwards
                    new MoveCommand(driveSubsystem, MovementType.DRIVE, 85),
                    // Turn to heading
                    new MoveCommand(driveSubsystem, MovementType.TURN_TO_HEADING, autoPosition.flip(0)),
                    // Turn to heading
                    new MoveCommand(driveSubsystem, MovementType.SLOW_TURN_TO_HEADING, autoPosition.flip(0)),
                    // Move backwards
                    new MoveCommand(driveSubsystem, MovementType.DRIVE, 20)
                );
        else if (autoPosition.spikeMark == AutoPosition.SpikeMark.UPSTAGE)
            addCommands(
                // Strafe right
                new MoveCommand(driveSubsystem, MovementType.STRAFE, autoPosition.flip(20)),
                // Turn to heading
                new MoveCommand(driveSubsystem, MovementType.TURN_TO_HEADING, autoPosition.flip(90)),
                new MoveCommand(driveSubsystem, MovementType.SLOW_TURN_TO_HEADING, autoPosition.flip(90)),
                // Wait for 5 seconds to allow our teammate to do whatever
                new WaitCommand(5000),
                // Drive forwards
                new MoveCommand(driveSubsystem, MovementType.DRIVE, 85),
                // Turn to heading
                new MoveCommand(driveSubsystem, MovementType.TURN_TO_HEADING, autoPosition.flip(0)),
                // Turn to heading
                new MoveCommand(driveSubsystem, MovementType.SLOW_TURN_TO_HEADING, autoPosition.flip(0)),
                // Move backwards
                new MoveCommand(driveSubsystem, MovementType.DRIVE, -5)
            );
        }
        else addCommands(

                new MoveCommand(driveSubsystem, MovementType.TURN_TO_HEADING, 0),
                new MoveCommand(driveSubsystem, MovementType.SLOW_TURN_TO_HEADING, 0)
        );
    }

    @Override
    public boolean isFinished() {
        return lastCommand.isFinished();
    }
}