package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auto.AutonomousController;

@Autonomous(name = "Blue Downstage (Parks upstage)", group = "Auto")
public class AutoBlueDownstageParking extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        AutonomousController autonomousController = new AutonomousController(this, true, false, false, true);
//        telemetry.update();

        waitForStart();
        autonomousController.start();

        while (opModeIsActive() && !isStopRequested()) {
            autonomousController.run();
        }
    }
}
