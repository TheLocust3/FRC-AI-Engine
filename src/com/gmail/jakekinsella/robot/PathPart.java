package com.gmail.jakekinsella.robot;

/**
 * Created by jakekinsella on 1/31/17.
 */
public class PathPart {

    private final int PIXEL_TOLERANCE = 50;

    private boolean finished;

    private RotatedRectangle line;
    private RobotControl robotControl;

    public PathPart(RotatedRectangle line, RobotControl robotControl) {
        this.line = line;
        this.robotControl = robotControl;
        this.finished = false;
    }

    public boolean isFinished() {
        return this.finished;
    }

    public void execute() {
        this.robotControl.turn(line.getAngle());
        this.robotControl.drive(0.5); // TODO: Change the robot speed
        this.finished = isRobotAtEnd();
    }

    private boolean isRobotAtEnd() { // TODO: Check if the robot overshot
        boolean atEnd = Math.abs(this.robotControl.getRobotBounds().getCenterX() - this.line.getX2()) < PIXEL_TOLERANCE;
        atEnd = atEnd && Math.abs(this.robotControl.getRobotBounds().getCenterY() - this.line.getY2()) < PIXEL_TOLERANCE;

        return atEnd;
    }
}
