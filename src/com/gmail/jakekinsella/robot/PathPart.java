package com.gmail.jakekinsella.robot;

import com.gmail.jakekinsella.map.Map;

/**
 * Created by jakekinsella on 1/31/17.
 */
public class PathPart {

    private boolean finished;

    private RotatedRectangle line;
    private Map map;
    private RobotControl robotControl;

    public PathPart(RotatedRectangle line, Map map, RobotControl robotControl) {
        this.line = line;
        this.map = map;
        this.robotControl = robotControl;
        this.finished = false;
    }

    public boolean isFinished() {
        return this.finished;
    }

    public void execute() {
        this.robotControl.turn(line.getAngle());
        this.robotControl.drive(0.5);

        //this.finished = true; // TODO: Figure out how to tell where the robot is
    }
}
