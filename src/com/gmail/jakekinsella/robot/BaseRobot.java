package com.gmail.jakekinsella.robot;

import com.gmail.jakekinsella.map.Map;

/**
 * Created by jakekinsella on 12/20/16.
 */
public abstract class BaseRobot {

    public abstract double getDegrees();

    public abstract double getAcceleration();

    public abstract double getVelocity();

    public abstract void drive(double speed);

    public abstract void turn(long speed);

    public void gotoLocation() {
        // TODO: Implement pathfinding
    }

    public void tick(double deltaSeconds, Map map) {
        // TODO: Check acceleration for obstacle
        // TODO: Update position
    }
}
