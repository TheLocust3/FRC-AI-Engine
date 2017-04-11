package com.gmail.jakekinsella.robot.routines;

import com.gmail.jakekinsella.communicator.Communicator;
import com.gmail.jakekinsella.map.Map;
import com.gmail.jakekinsella.robot.RobotControl;

/**
 * Created by jakekinsella on 4/11/17.
 */
public abstract class Routine {

    protected RobotControl robotControl;
    protected Communicator communicator;

    public Routine(RobotControl robotControl, Communicator communicator) {
        this.robotControl = robotControl;
        this.communicator = communicator;
    }

    public abstract void execute(Map map);

    public abstract boolean isFinished();
}
