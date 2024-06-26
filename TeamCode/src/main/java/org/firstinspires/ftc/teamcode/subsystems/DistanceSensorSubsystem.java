package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.Constants.DistanceSensorConstants.*;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * A subsystem that represents the two distance sensors on the sides of the robot. They are used
 * to detect the team prop during autonomous.
 *
 * @author Esquimalt Atom Smashers
 */
public class DistanceSensorSubsystem extends CustomSubsystemBase {
    private final DistanceSensor leftDistanceSensor;
    private final DistanceSensor rightDistanceSensor;

    /**
     * Constructs a new DistanceSensorSubsystem.
     *
     * @param hardwareMap The hardware map of the robot
     * @param telemetry The telemetry of the robot
     */
    public DistanceSensorSubsystem(HardwareMap hardwareMap, Telemetry telemetry) {
        super(hardwareMap, telemetry);

        leftDistanceSensor = hardwareMap.get(DistanceSensor.class, LEFT_DISTANCE_SENSOR_NAME);
        rightDistanceSensor = hardwareMap.get(DistanceSensor.class, RIGHT_DISTANCE_SENSOR_NAME);
    }

    /** @return True if there is something blocking the left sensor less than the threshold inches away */
    public boolean isLeftBlocked() {
        return leftDistanceSensor.getDistance(DistanceUnit.INCH) <= DISTANCE_THRESHOLD;
    }

    /** @return True if there is something blocking the right sensor less than the threshold inches away */
    public boolean isRightBlocked() {
        return rightDistanceSensor.getDistance(DistanceUnit.INCH) <= DISTANCE_THRESHOLD;
    }

    /** Prints data from the distance sensors. */
    @Override
    public void printData() {
        telemetry.addLine("--- Distance ---");
        telemetry.addData("Left (in)", leftDistanceSensor.getDistance(DistanceUnit.INCH));
        telemetry.addData("Right (in)", rightDistanceSensor.getDistance(DistanceUnit.INCH));
    }
}
