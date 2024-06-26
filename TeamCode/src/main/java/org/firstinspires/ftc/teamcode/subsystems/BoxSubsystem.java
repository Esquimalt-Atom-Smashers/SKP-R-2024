package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.Constants.BoxConstants.*;

import com.arcrobotics.ftclib.hardware.ServoEx;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LED;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/**
 * A subsystem that represents the servo on the door at the end of the arm and the LEDs
 * on the back of the robot. Uses the servo to open and close the door and the LEDs to indicate
 * when open/closed.
 *
 * @author Esquimalt Atom Smashers
 */
public class BoxSubsystem extends CustomSubsystemBase {
    /** Servo on the back of the box. */
    private final ServoEx boxReleaseServo;

    private final LED redRightLED;
    private final LED greenRightLED;
    private final LED redLeftLED;
    private final LED greenLeftLED;

    /**
     * Constructs a new BoxReleaseSubsystem.
     *
     * @param hardwareMap The hardware map of the robot
     * @param telemetry The telemetry of the robot
     */
    public BoxSubsystem(HardwareMap hardwareMap, Telemetry telemetry) {
        super(hardwareMap, telemetry);

        boxReleaseServo = new SimpleServo(hardwareMap, BOX_SERVO_NAME, MIN_ANGLE, MAX_ANGLE);

        redLeftLED = hardwareMap.get(LED.class, RED_LEFT_LED_NAME);
        greenLeftLED = hardwareMap.get(LED.class, GREEN_LEFT_LED_NAME);
        redRightLED = hardwareMap.get(LED.class, RED_RIGHT_LED_NAME);
        greenRightLED = hardwareMap.get(LED.class, GREEN_RIGHT_LED_NAME);
    }

    /** Turns the servo on the box door to a position which opens it. */
    public void openBox() {
        boxReleaseServo.turnToAngle(OPEN_POSITION);

        enableRedLights();
    }

    /** Turns the servo on the box door to a position which closes it. */
    public void closeBox() {
        boxReleaseServo.turnToAngle(CLOSE_POSITION);

        enableGreenLights();
    }

    public void disableLights() {
        greenRightLED.enable(false);
        redLeftLED.enable(false);
        greenLeftLED.enable(false);
        redRightLED.enable(false);
    }

    // TODO: Figure these out
    // I hate this
    // The green are red and the red are green, but whatever
    private void enableRedLights() {
        greenLeftLED.enable(true);
        redLeftLED.enable(false);
        greenRightLED.enable(true);
        redRightLED.enable(false);
    }

    private void enableGreenLights() {
        greenLeftLED.enable(false);
        redLeftLED.enable(true);
        greenRightLED.enable(false);
        redRightLED.enable(true);
    }

    /** Prints data from the subsystem. */
    @Override
    public void printData() {
        telemetry.addLine("--- Box Release ---");
        telemetry.addData("Angle", boxReleaseServo.getAngle());
        telemetry.addData("Angle (degrees)", boxReleaseServo.getAngle(AngleUnit.DEGREES));
        telemetry.addData("Position", boxReleaseServo.getPosition());
    }
}
