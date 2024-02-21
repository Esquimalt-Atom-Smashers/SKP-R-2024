package org.firstinspires.ftc.teamcode.opmodes;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auto.AutoPosition;
import org.firstinspires.ftc.teamcode.commands.AutoPlaceYellowAndHideCommand;
import org.firstinspires.ftc.teamcode.commands.AutoPlaceYellowCommand;
import org.firstinspires.ftc.teamcode.commands.MoveCommand;
import org.firstinspires.ftc.teamcode.subsystems.BoxSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ElbowSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LinearSlideSubsystem;

@Autonomous(group = "auto")
public class TestingAutoOpMode extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        DriveSubsystem driveSubsystem = new DriveSubsystem(hardwareMap, telemetry);
        ElbowSubsystem elbowSubsystem = new ElbowSubsystem(hardwareMap, telemetry);
        LinearSlideSubsystem slideSubsystem = new LinearSlideSubsystem(hardwareMap, telemetry);
        BoxSubsystem boxSubsystem = new BoxSubsystem(hardwareMap, telemetry);

        telemetry.addLine("This op mode is used to test the autonomous driving.");
        telemetry.addLine("When you start this, it will drive and strafe in a square.");
        telemetry.update();

        waitForStart();
        telemetry.update();
        new AutoPlaceYellowAndHideCommand(driveSubsystem, elbowSubsystem, slideSubsystem, boxSubsystem, new AutoPosition(AutoPosition.SpikeMark.UPSTAGE, true, true, true)).schedule();

        while (opModeIsActive() && !isStopRequested()) {
            CommandScheduler.getInstance().run();
            telemetry.update();
        }
    }

}
