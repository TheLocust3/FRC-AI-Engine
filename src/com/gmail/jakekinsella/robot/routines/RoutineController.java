package com.gmail.jakekinsella.robot.routines;

import com.gmail.jakekinsella.communicator.Communicator;
import com.gmail.jakekinsella.map.Map;
import com.gmail.jakekinsella.robot.RobotControl;

/**
 * Created by jakekinsella on 4/11/17.
 */
public abstract class RoutineController {

    protected Routine currentRoutine;
    protected RobotControl robotControl;
    protected Communicator communicator;

    public RoutineController(RobotControl robotControl, Communicator communicator) {
        this.robotControl = robotControl;
        this.communicator = communicator;
    }

    public abstract void pickupGearFromStation();

    public void tick(Map map) {
        this.currentRoutine.execute(map);

        if (this.isRoutineFinished()) {
            this.currentRoutine = null;
        }
    }

    private boolean isRoutineFinished() {
        if (this.currentRoutine != null) {
            return this.currentRoutine.isFinished();
        }

        return false;
    }
}
