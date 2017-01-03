package com.gmail.jakekinsella.robot;

import com.gmail.jakekinsella.communicator.Communicator;
import com.gmail.jakekinsella.map.Map;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/**
 * Created by jakekinsella on 12/20/16.
 */
public class RobotControl {

    private final int WIDTH = 50, HEIGHT = 50;

    private Communicator communicator;
    private AccelerationTracker accelerationTracker;
    private Shape boundingBox;

    public RobotControl(Communicator communicator) {
        this.communicator = communicator;
        this.accelerationTracker = new AccelerationTracker();
        this.boundingBox = new Rectangle2D.Double();
    }

    public Rectangle getRobotBounds() {
        return this.boundingBox.getBounds();
    }

    public void updateInternalPositionFromVision(int x, int y) {
        double deltaX = (this.boundingBox.getBounds().getCenterX() + x) / 2.0; // Average them together
        double deltaY = (this.boundingBox.getBounds().getCenterY() + y) / 2.0;

        this.updateInternalPosition(deltaX, deltaY, this.getDegrees());
    }

    public void tick(double deltaSeconds, Map map) {
        this.accelerationTracker.addAcceleration(this.getAcceleration(), deltaSeconds);
        if (this.accelerationTracker.isAccelerationSpike()) {
            this.accelerationTracker.clearAcceleration();

            // TODO: Place obstacle behind the robot
        }

        double deltaX = (this.getVelocity() * Math.sin(Math.toRadians(this.getDegrees()))) * deltaSeconds;
        double deltaY = (this.getVelocity() * Math.cos(Math.toRadians(this.getDegrees()))) * deltaSeconds;
        this.updateInternalPosition(deltaX, deltaY, this.getDegrees());
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

    private void drive(double percentSpeed) {
        this.communicator.move(percentSpeed);
    }

    private void turn(double angle) {
        this.communicator.turn(angle);
    }

    private void updateInternalPosition(double deltaX, double deltaY, double absoluteDeegrees) {
        Rectangle2D.Double rect = new Rectangle2D.Double();
        rect.setRect(this.boundingBox.getBounds().getX() + deltaX, this.boundingBox.getBounds().getY() + deltaY, this.WIDTH, this.HEIGHT);

        AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(absoluteDeegrees), rect.getCenterX(), rect.getCenterY());
        this.boundingBox = at.createTransformedShape(rect);
    }
}
