package com.gmail.jakekinsella.robot;

import com.gmail.jakekinsella.communicator.Communicator;

/**
 * Created by jakekinsella on 12/20/16.
 */
public class SocketRobot extends BaseRobot {

    public SocketRobot(Communicator communicator) {
        super(communicator);
    }

    @Override
    public double getDegrees() {
        return 0; // TODO: Implement get degrees
    }

    @Override
    public double getAcceleration() {
        return 0; // TODO: Implement get acceleration
    }

    @Override
    public double getVelocity() {
        return 0; // TODO: Implement get velocity
    }

    @Override
    public void drive(double speed) {
        this.communicator.move(speed);
    }

    @Override
    public void turn(long angle) {
        this.communicator.turn(angle);
    }
}
