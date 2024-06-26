package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.Constants.ElbowConstants.*;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Constants.PIDSubsystemState;

/**
 * A subsystem that represents the motor that controls the elbow of the arm.
 *
 * @author Esquimalt Atom Smashers
 */
@Config
public class ElbowSubsystem extends CustomSubsystemBase {
    private final DcMotorEx elbowMotor;

    private final PIDController controller;

    private static double target = 0;
    private double lastPower;

    private ElapsedTime timer;
    private double timeout;

    private final DigitalChannel elbowLimitSwitch;

    /** The state the arm is in: (manual, moving-to-target, or at-target) */
    private PIDSubsystemState state;

    /**
     * Constructs an ElbowSubsystem.
     *
     * @param hardwareMap The hardware map of the robot
     * @param telemetry The telemetry of the robot
     */
    public ElbowSubsystem(HardwareMap hardwareMap, Telemetry telemetry) {
        super(hardwareMap, telemetry);

        elbowMotor = hardwareMap.get(DcMotorEx.class, ELBOW_DC_MOTOR_NAME);
        configureMotor();

        controller = new PIDController(P, I, D);

        elbowLimitSwitch = hardwareMap.get(DigitalChannel.class, ELBOW_LIMIT_SWITCH_NAME);
        elbowLimitSwitch.setMode(DigitalChannel.Mode.INPUT);

        state = PIDSubsystemState.MANUAL;
    }

    /** Configure the elbow motor by setting the direction and zero power behavior. */
    private void configureMotor() {
        elbowMotor.setDirection(ELBOW_MOTOR_DIRECTION);
        elbowMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    /** Resets the encoder on the elbow motor. */
    public void resetEncoder() {
        elbowMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elbowMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    /** Stops the elbow motor. */
    public void stopMotor() {
        elbowMotor.setPower(0);
    }

    /**
     * Raises the arm manually.
     *
     * @param input A input for the speed of the motor
     */
    public void moveManually(double input) {
        state = PIDSubsystemState.MANUAL;
        if (input < 0 && isLimitSwitchPressed()) {
            stopMotor();
            resetEncoder();
            return;
        }
        elbowMotor.setPower(input * MANUAL_MOTOR_SPEED_MULTIPLIER);
    }

    /**
     * Lowers the arm manually
     *
     * @param multiplier A multiplier for the speed of the motor
     */
    @Deprecated
    public void lowerManually(double multiplier) {
        state = PIDSubsystemState.MANUAL;
        if (isLimitSwitchPressed()) {
            stopMotor();
            return;
        }
        elbowMotor.setPower(-MANUAL_MOTOR_SPEED_MULTIPLIER * multiplier);
    }

    /**
     * Set the target for the PID controller. This will make the robot start moving the arm automatically.
     *
     * @param targetPosition The target position in pulses
     */
    public void setTarget(double targetPosition, double timeout) {
        target = targetPosition;
        state = PIDSubsystemState.MOVING_TO_TARGET;
        if (timer == null) timer = new ElapsedTime();
        else timer.reset();
        this.timeout = timeout;
    }

    /**
     * Use the PID controller to calculate how fast we should set the motor to. If the motor is moving slow enough,
     * we are close enough and stop moving further.
     */
    // TODO: Clean this method up
    public void runPID() {
        // If we aren't at the target
        if (state == PIDSubsystemState.MOVING_TO_TARGET)
        {
            // If the target is zero, move the arm all the way down
            // Only stop if we press the limit switch or the timeout ends
            if (target == 0) {
                lastPower = -1;
                elbowMotor.setPower(-1);
                if (isLimitSwitchPressed() || isTimeoutPassed()) {
                    stopMotor();
                    if (isLimitSwitchPressed()) resetEncoder();
                    state = PIDSubsystemState.AT_TARGET;
                }
            }
            else {
                // If we are moving the arm down and pressing the limit switch,
                // stop and reset the encoders
                if (target < elbowMotor.getCurrentPosition() && isLimitSwitchPressed()) {
                    stopMotor();
                    resetEncoder();
                    state = PIDSubsystemState.AT_TARGET;
                    return;
                }
                // Calculate how much we need to move the motor by
                controller.setPID(P, I, D);
                int elbowPosition = elbowMotor.getCurrentPosition();
                double power = controller.calculate(elbowPosition, target);
                lastPower = power;
                elbowMotor.setPower(power);
                // If the power we are setting is basically none, we are close enough to the target
                if (Math.abs(power) <= PID_POWER_TOLERANCE || isTimeoutPassed()) {
                    state = PIDSubsystemState.AT_TARGET;
                    stopMotor();
                }
            }
        }
    }

    /**
     * Check if the timeout has passed, if is has, reset the timeout and return true.
     *
     * @return True if the timeout has elapsed, false otherwise
     */
    private boolean isTimeoutPassed() {
        if (timeout > 0 && timer.seconds() >= timeout) {
            timeout = 0;
            return true;
        }
        return false;
    }

    /** @return True if the motor is at the target, false otherwise */
    public boolean isAtTarget() {
        return state == PIDSubsystemState.AT_TARGET;
    }

    /** @return The position of the elbow motor */
    public int getPosition() {
        return elbowMotor.getCurrentPosition();
    }

    /** Print data from the elbow motor. */
    @Override
    public void printData() {
        telemetry.addLine("--- Elbow Subsystem ---");
        telemetry.addData("State", state);
        telemetry.addData("Elbow Position", elbowMotor.getCurrentPosition());
        telemetry.addData("Elbow last power", lastPower);
        telemetry.addData("Is limit pressed?", isLimitSwitchPressed());
        telemetry.addData("Target", target);
    }

    /** @return the preset low scoring position */
    public int getLowScoringPosition() {
        return LOW_SCORING_POSITION;
    }

    /** @return the preset medium scoring position */
    public int getMediumScoringPosition() {
        return MEDIUM_SCORING_POSITION;
    }

    /** @return the preset high scoring position */
    public int getHighScoringPosition() {
        return HIGH_SCORING_POSITION;
    }

    /** @return the drone launching position of the elbow */
    public int getDroneLaunchPosition() {
        return DRONE_LAUNCH_POSITION;
    }

    /** @return the level position of the elbow */
    public int getLevelPosition() {
        return LEVEL_POSITION;
    }

    /** @return the driving position of the elbow */
    public int getDrivingPosition() {
        return DRIVING_POSITION;
    }

    /** @return the minimum position of the elbow */
    public int getIntakePosition() {
        return INTAKE_POSITION;
    }

    /** @return True if the limit switch is being held down */
    private boolean isLimitSwitchPressed() {
        return !elbowLimitSwitch.getState();
    }
}