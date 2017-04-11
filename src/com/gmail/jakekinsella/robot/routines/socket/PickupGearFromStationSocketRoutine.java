package com.gmail.jakekinsella.robot.routines.socket;

import com.gmail.jakekinsella.communicator.Communicator;
import com.gmail.jakekinsella.map.Map;
import com.gmail.jakekinsella.robot.RobotControl;
import com.gmail.jakekinsella.robot.routines.Routine;

/**
 * Created by jakekinsella on 4/11/17.
 */
public class PickupGearFromStationSocketRoutine extends Routine {

    public PickupGearFromStationSocketRoutine(RobotControl robotControl, Communicator communicator) {
        super(robotControl, communicator);
    }

    @Override
    public void execute(Map map) {
        this.robotControl.gotoLocation(100, 100, map);
    }

    @Override
    public boolean isFinished() {
        return !this.robotControl.isFollowingPath();
    }
}
