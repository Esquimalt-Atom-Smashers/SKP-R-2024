package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.hardware.ServoEx;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.subsystems.ElbowSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.HangingSubsystem;

@Config
@TeleOp(name="Servo Testing", group="Testing")
public class ServoTestingOpMode extends LinearOpMode {
    public static double lowerTarget = 0;
    public static double higherTarget = 15;

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        ServoEx servo = new SimpleServo(hardwareMap, Constants.WinchConstants.HOOK_SERVO_NAME, 0, 270);
        GamepadEx gamepad = new GamepadEx(gamepad1);
        ElbowSubsystem elbowSubsystem = new ElbowSubsystem(hardwareMap, telemetry);
        HangingSubsystem hangingSubsystem = new HangingSubsystem(hardwareMap, telemetry);

        telemetry.addLine("This op mode is used to test a servo.");
        telemetry.addLine("While enabled, press A and B to switch between the lower target and higher target.");
        telemetry.addLine("To change the targets, you can use ftc dashboard, to change the servo you must change the servo name in code.");
        telemetry.update();


        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {

            if (gamepad.getButton(GamepadKeys.Button.A))
                hangingSubsystem.raiseServo();
            if (gamepad.getButton(GamepadKeys.Button.B))
                hangingSubsystem.lowerServo();
            hangingSubsystem.levelServo(elbowSubsystem);

            if (Math.abs(gamepad.getLeftY()) >= 0.1) {
                elbowSubsystem.moveManually(gamepad.getLeftY());
            }
            else elbowSubsystem.stopMotor();

            telemetry.addData("Position", servo.getPosition());
            elbowSubsystem.printData();
            telemetry.update();
        }
    }
}
