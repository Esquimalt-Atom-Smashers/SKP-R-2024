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

        // We are upstage
        if (autoPosition.isUpstage) {
            // Make sure that we do want to place the yellow
            if (autoPosition.isPlacingYellow) {
                placingFromUpstage(driveSubsystem, autoPosition);
            }
            // If we don't, just turn the correct heading (I don't think this will actually get used, but I'm keeping it in for consistency)
            else {
                addCommands(
                        new MoveCommand(driveSubsystem, MovementType.TURN_TO_HEADING, 0),
                        new MoveCommand(driveSubsystem, MovementType.SLOW_TURN_TO_HEADING, 0)
                );
            }
        }
        // We are downstage
        else {
            // Place the yellow pixel on the backdrop from the downstage
            if (autoPosition.isPlacingYellow) {
                placingFromDownstage(driveSubsystem, autoPosition);
            }
            // Park in the stage area from the downstage
            else if (autoPosition.isParkingFromDownstage) {
                parkingFromDownstage(driveSubsystem, autoPosition);
            }
            // Otherwise, we just turn to the correct direction
            else {
                addCommands(
                        new MoveCommand(driveSubsystem, MovementType.TURN_TO_HEADING, 0),
                        new MoveCommand(driveSubsystem, MovementType.SLOW_TURN_TO_HEADING, 0)
                );
            }
        }

//        // If we don't want to place the yellow or park upstage, we are done, so we turn and park
//        if (!autoPosition.isPlacingYellow && !autoPosition.isParkingFromDownstage) {
//            addCommands(
//                    new MoveCommand(driveSubsystem, MovementType.TURN_TO_HEADING, 0),
//                    new MoveCommand(driveSubsystem, MovementType.SLOW_TURN_TO_HEADING, 0)
//            );
//            return;
//        }
//
//        // If we are upstage, we just drive to the backdrop, then place the yellow
//        if (autoPosition.isUpstage) {
//            placingFromUpstage(driveSubsystem, autoPosition);
//        }
//        // Otherwise, we are down stage
//        //
//        else {
//            if (autoPosition.isParkingFromDownstage) {
//                parkingFromDownstage(driveSubsystem, autoPosition);
//            }
//            else if (autoPosition.isPlacingYellow) {
//                placingFromDownstage(driveSubsystem, autoPosition);
//            }
//
//        }

        addCommands(lastCommand);
    }

    private void placingFromUpstage(DriveSubsystem driveSubsystem, AutoPosition autoPosition) {
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


    // This adds the commands needed to drive from placing the downstage purple pixel, to the backdrop
    // then place the yellow pixel on the backdrop
    private void placingFromDownstage(DriveSubsystem driveSubsystem, AutoPosition autoPosition) {

        if (autoPosition.spikeMark == AutoPosition.SpikeMark.UPSTAGE) {
            // We start around 28 inches forwards, 4 inches right, facing the backdrop
            // TODO: Be super careful testing this!!!
            addCommands(
                    // Strafe a bit
                    new MoveCommand(driveSubsystem, MovementType.STRAFE, autoPosition.flip(24)),
                    // Turn to heading
                    new MoveCommand(driveSubsystem, MovementType.TURN_TO_HEADING, autoPosition.flip(90)),
                    new MoveCommand(driveSubsystem, MovementType.SLOW_TURN_TO_HEADING, autoPosition.flip(90)),
                    // Drive forwards
                    new MoveCommand(driveSubsystem, MovementType.DRIVE, 80),
                    // Strafe
                    new MoveCommand(driveSubsystem, MovementType.STRAFE, autoPosition.flip(-25)),
                    // Turn to heading
                    new MoveCommand(driveSubsystem, MovementType.TURN_TO_HEADING, autoPosition.flip(90)),
                    new MoveCommand(driveSubsystem, MovementType.SLOW_TURN_TO_HEADING, autoPosition.flip(90))
            );
        } else if (autoPosition.spikeMark == AutoPosition.SpikeMark.MIDDLE) {
            // We start 25 inches forwards
            // TODO: Be super careful testing this!!!
            addCommands(
                    new MoveCommand(driveSubsystem, MovementType.STRAFE, autoPosition.flip(15)),
                    new MoveCommand(driveSubsystem, MovementType.SLOW_TURN_TO_HEADING, 0),
                    // Move forwards a bit
                    new MoveCommand(driveSubsystem, MovementType.DRIVE, 25),
                    // Rotate to 90
                    new MoveCommand(driveSubsystem, MovementType.TURN, autoPosition.flip(90)),
                    // Turn to heading to be safe?
                    new MoveCommand(driveSubsystem, MovementType.SLOW_TURN_TO_HEADING, autoPosition.flip(90)),
                    // Drive forwards under the truss, stopping at the tape
                    new MoveCommand(driveSubsystem, MovementType.DRIVE, 90),
                    // Strafe left a bit
                    new MoveCommand(driveSubsystem, MovementType.STRAFE, autoPosition.flip(-23)),
                    // Turn to heading again
                    new MoveCommand(driveSubsystem, MovementType.SLOW_TURN_TO_HEADING, autoPosition.flip(90)),
                    new MoveCommand(driveSubsystem, MovementType.SLOW_TURN_TO_HEADING, autoPosition.flip(90))
                    // End
            );
        } else if (autoPosition.spikeMark == AutoPosition.SpikeMark.DOWNSTAGE) {
            // We start about 28 inches forwards, 2 inches left
            // TODO: Be super careful testing this!!!
            addCommands(
                    // Strafe right
                    new MoveCommand(driveSubsystem, MovementType.STRAFE, 23),
                    // Turn to heading
                    new MoveCommand(driveSubsystem, MovementType.TURN_TO_HEADING, autoPosition.flip(90)),
                    // Drive forwards
                    new MoveCommand(driveSubsystem, MovementType.DRIVE, 75),
                    // Turn to heading
                    new MoveCommand(driveSubsystem, MovementType.TURN_TO_HEADING, autoPosition.flip(90)),
                    // Strafe left
                    new MoveCommand(driveSubsystem, MovementType.STRAFE, autoPosition.flip(-24)),
                    // Turn to heading
                    new MoveCommand(driveSubsystem, MovementType.SLOW_TURN_TO_HEADING, autoPosition.flip(90))

            );

        }
    }

    // This adds the commands needed to drive from the backdrop to park in the stage area
    private void parkingFromDownstage(DriveSubsystem driveSubsystem, AutoPosition autoPosition) {
        if (autoPosition.spikeMark == AutoPosition.SpikeMark.UPSTAGE) {
            // TODO: Test this
            addCommands(
                    // Strafe a bit
                    new MoveCommand(driveSubsystem, MovementType.STRAFE, autoPosition.flip(24)),
                    // Turn to heading
                    new MoveCommand(driveSubsystem, MovementType.TURN_TO_HEADING, autoPosition.flip(90)),
                    new MoveCommand(driveSubsystem, MovementType.SLOW_TURN_TO_HEADING, autoPosition.flip(90)),
                    // Drive forwards
                    new MoveCommand(driveSubsystem, MovementType.DRIVE, 80),
                    // Turn to heading
                    new MoveCommand(driveSubsystem, MovementType.TURN_TO_HEADING, autoPosition.flip(0)),
                    new MoveCommand(driveSubsystem, MovementType.SLOW_TURN_TO_HEADING, autoPosition.flip(0))
                    // TODO: Maybe move backwards a bit
            );
        }
        else if (autoPosition.spikeMark == AutoPosition.SpikeMark.MIDDLE) {
            // TODO: Test this
            addCommands(
                    // Move around the purple pixel, which is directly ahead of us
                    new MoveCommand(driveSubsystem, MovementType.STRAFE, autoPosition.flip(15)),
                    new MoveCommand(driveSubsystem, MovementType.SLOW_TURN_TO_HEADING, 0),
                    // Move forwards a bit
                    new MoveCommand(driveSubsystem, MovementType.DRIVE, 25),
                    // Rotate to 90
                    new MoveCommand(driveSubsystem, MovementType.TURN, autoPosition.flip(90)),
                    // Turn to heading to be safe?
                    new MoveCommand(driveSubsystem, MovementType.SLOW_TURN_TO_HEADING, autoPosition.flip(90)),
                    // Drive forwards under the truss, stopping at the tape
                    new MoveCommand(driveSubsystem, MovementType.DRIVE, 90),
                    // Turn to heading again
                    new MoveCommand(driveSubsystem, MovementType.SLOW_TURN_TO_HEADING, autoPosition.flip(0)),
                    new MoveCommand(driveSubsystem, MovementType.SLOW_TURN_TO_HEADING, autoPosition.flip(0))
                    // TODO: Maybe move backwards a bit afterwards
                    // End
            );
        }
        else if (autoPosition.spikeMark == AutoPosition.SpikeMark.DOWNSTAGE) {
            addCommands(
                    // Strafe right
                    new MoveCommand(driveSubsystem, MovementType.STRAFE, autoPosition.flip(-20)),
                    // Turn to heading
                    new MoveCommand(driveSubsystem, MovementType.TURN_TO_HEADING, autoPosition.flip(90)),
                    new MoveCommand(driveSubsystem, MovementType.SLOW_TURN_TO_HEADING, autoPosition.flip(90)),
                    // Drive forwards
                    new MoveCommand(driveSubsystem, MovementType.DRIVE, 85),
                    // Turn to heading
                    new MoveCommand(driveSubsystem, MovementType.TURN_TO_HEADING, autoPosition.flip(0)),
                    // Turn to heading
                    new MoveCommand(driveSubsystem, MovementType.SLOW_TURN_TO_HEADING, autoPosition.flip(0)),
                    // Drive backwards to make sure we aren't too far over the middle line
                    new MoveCommand(driveSubsystem, MovementType.DRIVE, -5)
            );
        }
    }

    @Override
    public boolean isFinished() {
        return lastCommand.isFinished();
    }
}