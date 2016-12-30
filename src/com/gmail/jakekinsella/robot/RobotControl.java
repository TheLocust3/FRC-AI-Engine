package com.gmail.jakekinsella.robot;

import com.gmail.jakekinsella.communicator.Communicator;
import com.gmail.jakekinsella.map.Map;

/**
 * Created by jakekinsella on 12/20/16.
 */
public class RobotControl {

    private Communicator communicator;
    private AccelerationTracker accelerationTracker;

    public RobotControl(Communicator communicator) {
        this.communicator = communicator;
        this.accelerationTracker = new AccelerationTracker();
    }

    public void tick(double deltaSeconds, Map map) {
        this.accelerationTracker.addAcceleration(this.getAcceleration(), deltaSeconds);
        if (this.accelerationTracker.isAccelerationSpike()) {
            this.accelerationTracker.clearAcceleration();

            // TODO: Place obstacle behind the robot
        }

        // TODO: Update position
    }

    public void gotoLocation() {
        // TODO: Implement pathfinding
    }

    private double getDegrees() {
        return communicator.getDegrees();
    }

    private double getAcceleration() {
        return communicator.getAcceleration();
    }

    private double getVelocity() {
        return communicator.getVelocity();
    }

    private void drive(double speed) {
        this.communicator.move(speed);
    }

    private void turn(double angle) {
        this.communicator.turn(angle);
    }
}
