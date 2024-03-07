package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.Constants.LEDConstants.*;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * A subsystem that represents the LEDs inside the frame of the robot. Uses them to look cool.
 *
 * @author Esquimalt Atom Smashers
 */
public class LEDSubsystem extends CustomSubsystemBase {
    private final RevBlinkinLedDriver ledDriver;

    /**
     * Constructs an LEDSubsystem.
     *
     * @param hardwareMap The hardware map of the robot
     * @param telemetry The telemetry of the robot
     */
    public LEDSubsystem(HardwareMap hardwareMap, Telemetry telemetry) {
        super(hardwareMap, telemetry);

        ledDriver = hardwareMap.get(RevBlinkinLedDriver.class, LED_NAME);
    }

    /**
     * Sets the lights to a specified {@link RevBlinkinLedDriver.BlinkinPattern BlinkinPattern}.
     *
     * @param pattern The pattern to set the lights to
     */
    public void setLights(RevBlinkinLedDriver.BlinkinPattern pattern) {
        ledDriver.setPattern(pattern);
    }

    /** Sets the light to solid blue. */
    public void setSolidBlue() {
        setLights(RevBlinkinLedDriver.BlinkinPattern.BLUE);
    }

    /** Sets the lights to strobe blue. */
    @SuppressWarnings("unused")
    public void setStrobeBlue() {
        setLights(RevBlinkinLedDriver.BlinkinPattern.STROBE_BLUE);
    }

    /** Sets the lights to solid red. */
    public void setSolidRed() {
        setLights(RevBlinkinLedDriver.BlinkinPattern.RED);
    }

    /** Sets the lights to strobe red. */
    @SuppressWarnings("unused")
    public void setStrobeRed() {
        setLights(RevBlinkinLedDriver.BlinkinPattern.STROBE_RED);
    }

    /** Sets the lights to solid hot pink. */
    public void setPink() {
        setLights(RevBlinkinLedDriver.BlinkinPattern.HOT_PINK);
    }

    /** Sets the lights to rainbow. */
    @SuppressWarnings("unused")
    public void setRainbow() {
        setLights(RevBlinkinLedDriver.BlinkinPattern.RAINBOW_WITH_GLITTER);
    }

    /** Sets the lights to twinkle party. */
    @SuppressWarnings("unused")
    public void setParty() {
        setLights(RevBlinkinLedDriver.BlinkinPattern.TWINKLES_PARTY_PALETTE);
    }

    /** Sets the lights to lava waves. */
    @SuppressWarnings("unused")
    public void setLavaWaves() {
        setLights(RevBlinkinLedDriver.BlinkinPattern.COLOR_WAVES_LAVA_PALETTE);
    }
}
