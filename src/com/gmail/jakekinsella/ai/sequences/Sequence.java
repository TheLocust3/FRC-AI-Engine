package com.gmail.jakekinsella.ai.sequences;

import com.gmail.jakekinsella.map.Map;
import com.gmail.jakekinsella.robot.RobotControl;

/**
 * Created by jakekinsella on 3/27/17.
 */
public abstract class Sequence {

    RobotControl robotControl;

    private boolean isRunning;

    public Sequence(RobotControl robotControl) {
        this.robotControl = robotControl;

        this.isRunning = false;
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    public abstract void run(double deltaSeconds, Map map);
}
