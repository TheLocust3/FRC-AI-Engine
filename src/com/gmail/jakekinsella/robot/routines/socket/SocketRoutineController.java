package com.gmail.jakekinsella.robot.routines.socket;

import com.gmail.jakekinsella.communicator.Communicator;
import com.gmail.jakekinsella.robot.RobotControl;
import com.gmail.jakekinsella.robot.routines.RoutineController;

/**
 * Created by jakekinsella on 4/11/17.
 */
public class SocketRoutineController extends RoutineController {


    public SocketRoutineController(RobotControl robotControl, Communicator communicator) {
        super(robotControl, communicator);
    }

    @Override
    public void pickupGearFromStation() {
        this.currentRoutine = new PickupGearFromStationSocketRoutine(1, this.robotControl, this.communicator);
    }
}
