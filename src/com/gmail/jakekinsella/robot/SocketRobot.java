package com.gmail.jakekinsella.robot;

import com.gmail.jakekinsella.background.socket.SocketCollector;

/**
 * Created by jakekinsella on 12/20/16.
 */
public class SocketRobot extends BaseRobot {

    private SocketCollector socketCollector;

    public SocketRobot(SocketCollector socketCollector) {
        this.socketCollector = socketCollector;
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
        this.socketCollector.move(speed);
    }

    @Override
    public void turn(long angle) {
        this.socketCollector.turn(angle);
    }
}
