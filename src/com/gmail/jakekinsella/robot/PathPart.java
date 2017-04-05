package com.gmail.jakekinsella.robot;

import com.gmail.jakekinsella.Paintable;

import java.awt.*;

/**
 * Created by jakekinsella on 1/31/17.
 */
public class PathPart implements Paintable {

    private static final int PIXEL_TOLERANCE = 50;
    private static final double ANGLE_TOLERANCE = 5;

    private boolean finished;
    private boolean isStarted = false;

    private RotatedRectangle line;
    private RobotControl robotControl;

    public PathPart(RotatedRectangle line, RobotControl robotControl) {
        this.line = line;
        this.robotControl = robotControl;
        this.finished = false;
    }

    public RotatedRectangle getLine() {
        return this.line;
    }

    public boolean isFinished() {
        return this.finished;
    }

    public void execute() {
        if (!this.isStarted) {
            this.robotControl.turn(this.line.getAngle());
            this.isStarted = true;
        }

        if (this.checkIfAnglesAreClose(this.robotControl.getAngle(), this.line.getAngle())) {
            this.robotControl.drive(0.5); // TODO: Change the robot speed
        }

        this.finished = this.isRobotAtEnd();
    }

    @Override
    public void paint(Graphics2D graphics2D) {
        this.line.paint(graphics2D);
    }

    private boolean isRobotAtEnd() { // TODO: Check if the robot overshot
        boolean atEnd = Math.abs(this.robotControl.getRobotBounds().getCenterX() - this.line.getEndX()) < PIXEL_TOLERANCE;
        atEnd = atEnd && Math.abs(this.robotControl.getRobotBounds().getCenterY() - this.line.getEndY()) < PIXEL_TOLERANCE;
        atEnd = atEnd && this.checkIfAnglesAreClose(this.robotControl.getAngle(), this.line.getAngle());

        return atEnd;
    }

    private boolean checkIfAnglesAreClose(Angle angle1, Angle angle2) {
        return Math.abs(angle1.getNormalizedDegrees() - angle2.getNormalizedDegrees()) < ANGLE_TOLERANCE;
    }
}
