package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.LinearSlideSubsystem;

/**
 * Command that moves the slide to a set position using the PID controller.
 */
public class MoveSlideCommand extends CommandBase {
    private final LinearSlideSubsystem linearSlideSubsystem;
    private final double target;

    /**
     * Creates a command that moves the slide to a specified position using the PID controller.
     *
     * @param linearSlideSubsystem Reference to the linearSlideSubsystem
     * @param slidePosition The target position of the slide in pulses
     */
    public MoveSlideCommand(LinearSlideSubsystem linearSlideSubsystem, double slidePosition) {
//        linearSlideSubsystem.getTelemetry().addData("Not started, target", slidePosition).setRetained(true);
        this.linearSlideSubsystem = linearSlideSubsystem;
        target = slidePosition;
        addRequirements(this.linearSlideSubsystem);
    }

    @Override
    public void initialize() {
//        linearSlideSubsystem.getTelemetry().addData("Started, target", target).setRetained(true);
        if (target == -1) {
            cancel();
            end(true);
            return;
        }
        linearSlideSubsystem.setTarget(target, 1.5);
    }

    @Override
    public void execute() {
//        linearSlideSubsystem.getTelemetry().addData("Executing, target", target);
        linearSlideSubsystem.runPID();
    }

    @Override
    public void end(boolean interrupted) {
//        linearSlideSubsystem.getTelemetry().addData("Ending, target", target).setRetained(true);
        linearSlideSubsystem.stopMotor();
    }

    @Override
    public boolean isFinished() {
        return linearSlideSubsystem.isAtTarget();
    }
}
