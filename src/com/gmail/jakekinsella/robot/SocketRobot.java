package com.gmail.jakekinsella.robot;

import com.gmail.jakekinsella.communicator.socket.SocketCommunicator;

/**
 * Created by jakekinsella on 12/20/16.
 */
public class SocketRobot extends BaseRobot {

    private SocketCommunicator socketCommunicator;

    public SocketRobot(SocketCommunicator socketCommunicator) {
        this.socketCommunicator = socketCommunicator;
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
        this.socketCommunicator.move(speed);
    }

    @Override
    public void turn(long angle) {
        this.socketCommunicator.turn(angle);
    }
}
