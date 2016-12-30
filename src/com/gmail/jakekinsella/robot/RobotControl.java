package com.gmail.jakekinsella.robot;

import com.gmail.jakekinsella.communicator.Communicator;
import com.gmail.jakekinsella.map.Map;

/**
 * Created by jakekinsella on 12/20/16.
 */
public class RobotControl {

    private Communicator communicator;

    public RobotControl(Communicator communicator) {
        this.communicator = communicator;
    }

    public double getDegrees() {
        return 0;
    }

    public double getAcceleration() {
        return 0;
    }

    public double getVelocity() {
        return 0;
    }

    public void drive(double speed) {
        this.communicator.move(speed);
    }

    public void turn(double angle) {
        this.communicator.turn(angle);
    }

    public void gotoLocation() {
        // TODO: Implement pathfinding
    }

    public void tick(double deltaSeconds, Map map) {
        // TODO: Check acceleration for obstacle
        // TODO: Update position
    }
}
