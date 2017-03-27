package com.gmail.jakekinsella.ai;

import com.gmail.jakekinsella.map.Map;
import com.gmail.jakekinsella.robot.RobotControl;

/**
 * Created by jakekinsella on 3/27/17.
 */
public abstract class GenericAI {

    RobotControl robotControl;

    public GenericAI(RobotControl robotControl) {
        this.robotControl = robotControl;
    }

    public abstract void tick(double deltaSeconds, Map map);
}
