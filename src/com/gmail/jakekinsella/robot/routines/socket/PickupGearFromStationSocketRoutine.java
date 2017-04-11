package com.gmail.jakekinsella.robot.routines.socket;

import com.gmail.jakekinsella.communicator.Communicator;
import com.gmail.jakekinsella.map.Map;
import com.gmail.jakekinsella.robot.RobotControl;
import com.gmail.jakekinsella.robot.routines.Routine;

/**
 * Created by jakekinsella on 4/11/17.
 */
public class PickupGearFromStationSocketRoutine extends Routine {

    private boolean firstTime;

    public PickupGearFromStationSocketRoutine(RobotControl robotControl, Communicator communicator) {
        super(robotControl, communicator);

        this.firstTime = true;
    }

    @Override
    public void execute(Map map) {
        if (this.firstTime) {
            this.robotControl.gotoLocation(100, 100, map);

            this.firstTime = false;
        }
    }

    @Override
    public boolean isFinished() {
        return !this.firstTime && !this.robotControl.isFollowingPath();
    }
}
