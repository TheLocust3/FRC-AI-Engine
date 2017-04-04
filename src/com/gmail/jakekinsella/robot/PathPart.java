package com.gmail.jakekinsella.robot;

import com.gmail.jakekinsella.Paintable;

import java.awt.*;

/**
 * Created by jakekinsella on 1/31/17.
 */
public class PathPart implements Paintable {

    private final int PIXEL_TOLERANCE = 50;

    private boolean finished;

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
        if (!this.robotControl.getAngle().equals(line.getAngle())) {
            this.robotControl.turn(line.getAngle());
        } else {
            this.robotControl.drive(0.5); // TODO: Change the robot speed
        }

        this.finished = isRobotAtEnd();
    }

    @Override
    public void paint(Graphics2D graphics2D) {
        this.line.paint(graphics2D);
    }

    private boolean isRobotAtEnd() { // TODO: Check if the robot overshot
        boolean atEnd = Math.abs(this.robotControl.getRobotBounds().getCenterX() - this.line.getEndX()) < PIXEL_TOLERANCE;
        atEnd = atEnd && Math.abs(this.robotControl.getRobotBounds().getCenterY() - this.line.getEndY()) < PIXEL_TOLERANCE;
        atEnd = atEnd && this.robotControl.getAngle().equals(line.getAngle());

        return atEnd;
    }
}
