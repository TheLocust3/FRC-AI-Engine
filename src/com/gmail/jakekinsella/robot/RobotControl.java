package com.gmail.jakekinsella.robot;

import com.gmail.jakekinsella.communicator.Communicator;
import com.gmail.jakekinsella.map.FuzzyObject;
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
    private LinePath currentPath;

    public RobotControl(Communicator communicator) {
        this.communicator = communicator;
        this.accelerationTracker = new AccelerationTracker();
        this.boundingBox = new Rectangle2D.Double();
    }

    public Rectangle2D getRobotBounds() {
        return this.boundingBox.getBounds2D();
    }

    public void updateInternalPositionFromVision(int x, int y) {
        double deltaX = (this.boundingBox.getBounds().getCenterX() + x) / 2.0; // Average them together
        double deltaY = (this.boundingBox.getBounds().getCenterY() + y) / 2.0;

        this.updateInternalPosition(deltaX, deltaY, this.getAngle().getNormalizedDegrees());
    }

    public void tick(double deltaSeconds, Map map) {
        this.accelerationTracker.addAcceleration(this.getAcceleration(), deltaSeconds);
        if (this.accelerationTracker.isAccelerationSpike()) {
            this.handleVisionSpike(map);
        }

        double deltaX = (this.getVelocity() * Math.sin(this.getAngle().getRadians())) * deltaSeconds;
        double deltaY = (this.getVelocity() * Math.cos(this.getAngle().getRadians())) * deltaSeconds;
        this.updateInternalPosition(deltaX, deltaY, this.getAngle().getNormalizedDegrees());

        this.followLinePath();
    }

    public void gotoLocation(double newX, double newY, Map map) {
        this.currentPath = new LinePath(map, this); // Always want to be updating the path with new map data
        this.currentPath.generatePath(this.getRobotBounds().getX(), this.getRobotBounds().getY(), newX, newY);
    }

    public Angle getAngle() {
        return new Angle(communicator.getDegrees());
    }

    public double getAcceleration() {
        return communicator.getAcceleration();
    }

    public double getVelocity() {
        return communicator.getVelocity();
    }

    public void drive(double percentSpeed) {
        this.communicator.move(percentSpeed);
    }

    public void turn(Angle angle) {
        this.communicator.turn(angle.getDegrees());
    }

    private void updateInternalPosition(double deltaX, double deltaY, double absoluteDegrees) {
        Rectangle2D.Double rect = new Rectangle2D.Double();
        rect.setRect(this.boundingBox.getBounds().getX() + deltaX, this.boundingBox.getBounds().getY() + deltaY, this.WIDTH, this.HEIGHT);

        AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(absoluteDegrees), rect.getCenterX(), rect.getCenterY());
        this.boundingBox = at.createTransformedShape(rect);
    }

    private void followLinePath() {
        PathPart currentPart = this.currentPath.getCurrentPath();
        if (currentPart != null) {
            currentPart.execute();
        }
    }

    private void handleVisionSpike(Map map) {
        int placeObjectBehind = this.accelerationTracker.getAvgAcceleration() > 0 ? 0 : 360;
        double deltaX = this.WIDTH * Math.cos(Math.toRadians(this.getAngle().getDegrees() + placeObjectBehind));
        double deltaY = this.HEIGHT * Math.sin(Math.toRadians(this.getAngle().getDegrees() + placeObjectBehind));

        FuzzyObject fuzzyObject = new FuzzyObject((int) (this.getRobotBounds().getX() + deltaX), (int) (this.getRobotBounds().getY() + deltaY), (int) (this.WIDTH * 1.5), (int) (this.HEIGHT * 1.5));
        map.addObstacle(fuzzyObject);

        this.accelerationTracker.clearAcceleration();
    }
}
