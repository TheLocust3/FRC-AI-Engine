package com.gmail.jakekinsella.robot.pathing.socket;

import com.gmail.jakekinsella.robot.Angle;
import com.gmail.jakekinsella.robot.RobotControl;
import com.gmail.jakekinsella.robot.pathing.PaddedLine;

/**
 * Created by jakekinsella on 4/10/17.
 */
public class SocketFollower implements Follower {

    private static final int PIXEL_TOLERANCE = 20;
    private static final double ANGLE_TOLERANCE = 1;

    private PaddedLine line;
    private RobotControl robotControl;

    private boolean finished;
    private boolean shouldTurn = true;
    private boolean shouldMove = false;

    public SocketFollower(PaddedLine line, RobotControl robotControl) {
        this.line = line;
        this.robotControl = robotControl;
        this.finished = false;
    }

    public boolean isFinished() {
        return this.finished;
    }

    public void execute() {
        if (this.shouldTurn) {
            this.robotControl.turn(this.line.getAngle());
            this.shouldTurn = false;
            this.shouldMove = true;
        }

        if (this.shouldMove && this.line.checkIfAngleClose(this.robotControl.getAngle(), ANGLE_TOLERANCE)) {
            this.robotControl.drive(0.5); // TODO: Change the robot speed
            this.shouldMove = false;
        }

        this.finished = this.isRobotAtEnd();

        if (this.finished) {
            this.robotControl.drive(0);
        }
    }

    private boolean isRobotAtEnd() {
        boolean atEnd = Math.abs(this.robotControl.getRobotBounds().getCenterX() - this.line.getEndX()) < PIXEL_TOLERANCE;
        atEnd = atEnd && Math.abs(this.robotControl.getRobotBounds().getCenterY() - this.line.getEndY()) < PIXEL_TOLERANCE;
        atEnd = atEnd && this.line.checkIfAngleClose(this.robotControl.getAngle(), ANGLE_TOLERANCE);
        atEnd = atEnd && !this.line.isPointOnLine(this.robotControl.getRobotBounds().getCenterX(), this.robotControl.getRobotBounds().getCenterY());

        return atEnd;
    }
}
