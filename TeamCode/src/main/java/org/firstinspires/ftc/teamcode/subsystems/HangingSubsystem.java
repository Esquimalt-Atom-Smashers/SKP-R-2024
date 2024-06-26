package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.Constants.WinchConstants.*;

import com.arcrobotics.ftclib.hardware.ServoEx;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/**
 * A subsystem that represents the motor that controls the winch.
 *
 * @author Esquimalt Atom Smashers
 */
public class HangingSubsystem extends CustomSubsystemBase {
    private final DcMotorEx winchMotor;
    private final ServoEx hookServo;

    private enum ServoState {
        LEVEL,
        MANUAL
    }

    ServoState servoState = ServoState.LEVEL;

    /**
     * Constructs a new WinchSubsystem
     *
     * @param hardwareMap The hardware map of the robot
     * @param telemetry The telemetry of the robot
     */
    public HangingSubsystem(HardwareMap hardwareMap, Telemetry telemetry) {
        super(hardwareMap, telemetry);

        winchMotor = hardwareMap.get(DcMotorEx.class, WINCH_MOTOR_NAME);
        configureMotor();

        hookServo = new SimpleServo(hardwareMap, HOOK_SERVO_NAME, MIN_ANGLE, MAX_ANGLE);
    }

    /** Configure the winch motor by setting the direction and zero power behavior */
    private void configureMotor() {
        winchMotor.setDirection(WINCH_MOTOR_DIRECTION);
        winchMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        winchMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    /** Start moving the winch motor to pull ourselves up. */
    public void winch() {
        winchMotor.setPower(WINCH_SPEED);
    }

    /** Start moving the winch motor to let ourselves down. */
    public void unwinch() {
        winchMotor.setPower(UNWINCH_SPEED);
    }

    /** Stop the winch motor. */
    public void stopMotor() { winchMotor.setPower(0); }

    /** Raises the servo, also sets the state to manual. */
    public void raiseServo() {
        hookServo.turnToAngle(UP_POSITION);
        servoState = ServoState.MANUAL;
    }

    /** Lowers the servo, also sets the state to level */
    public void lowerServo() {
        hookServo.turnToAngle(DOWN_POSITION);
        servoState = ServoState.LEVEL;
    }

    /** Constantly updates the angle of the servo relative to the position of the elbow, so that it stays level, relative to the ground. Only works while the state is level. */
    public void levelServo(ElbowSubsystem elbowSubsystem) {
        if (servoState == ServoState.LEVEL) {
            hookServo.turnToAngle(convertPosition(elbowSubsystem.getPosition()));
        }
    }

    /**
     * Converts elbow motor position to servo angle.
     * @param value The current position of the elbow motor
     * @return The servo angle that would make it level
     */
    private double convertPosition(double value) {
        // See this
        // elbow    servo
        // 3900	    190
        // 3200	    195
        // 1900 	210
        // 1100 	215
        // 500	    220
        // 0	    225
        // https://www.desmos.com/calculator/8xvalris8b
        return Range.clip(-0.009 * value + 225, 0, 270);
    }

    /** Prints data from the subsystem */
    @Override
    public void printData() {
        telemetry.addData("State", servoState);
        telemetry.addData("Angle", hookServo.getAngle(AngleUnit.DEGREES));
    }
}
