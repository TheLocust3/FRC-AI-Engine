package com.gmail.jakekinsella.robot.pathing;

import com.gmail.jakekinsella.Paintable;
import com.gmail.jakekinsella.communicator.Communicator;
import com.gmail.jakekinsella.communicator.SocketCommunicator;
import com.gmail.jakekinsella.robot.Angle;
import com.gmail.jakekinsella.robot.RobotControl;
import com.gmail.jakekinsella.robot.pathing.socket.Follower;
import com.gmail.jakekinsella.robot.pathing.socket.SocketFollower;

import java.awt.*;

/**
 * Created by jakekinsella on 1/31/17.
 */
public class PathPart implements Paintable {

    private RotatedRectangle line;
    private RobotControl robotControl;
    private Follower follower;

    public PathPart(RotatedRectangle line, RobotControl robotControl) {
        this.line = line;
        this.robotControl = robotControl;

        if (robotControl.isOverSocket()) {
            this.follower = new SocketFollower(this.line, this.robotControl);
        } else {
            // TODO: Implement real follower
        }
    }

    public RotatedRectangle getLine() {
        return this.line;
    }

    @Override
    public void paint(Graphics2D graphics2D) {
        this.line.paint(graphics2D);
    }

    public boolean isFinished() {
        return this.follower.isFinished();
    }

    public void execute() {
        this.follower.execute();
    }
}
