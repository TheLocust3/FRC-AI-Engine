package com.gmail.jakekinsella.robot;

import com.gmail.jakekinsella.Paintable;
import com.gmail.jakekinsella.communicator.Communicator;
import com.gmail.jakekinsella.communicator.SocketCommunicator;
import com.gmail.jakekinsella.map.SolidObjects.FuzzyObject;
import com.gmail.jakekinsella.map.Map;
import com.gmail.jakekinsella.robot.pathing.LinePath;
import com.gmail.jakekinsella.robot.pathing.PathPart;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/**
 * Created by jakekinsella on 12/20/16.
 */
public class RobotControl implements Paintable {

    private final int WIDTH = 50, HEIGHT = 40;

    private Shape boundingBox;

    private Communicator communicator;
    private AccelerationTracker accelerationTracker;
    private LinePath currentPath;
    private Angle angle;

    public RobotControl(Communicator communicator, int startX, int startY) {
        this.communicator = communicator;
        this.accelerationTracker = new AccelerationTracker();
        this.boundingBox = new Rectangle2D.Double(0, 0, this.WIDTH, this.HEIGHT);
        this.updateInternalPosition(startX, startY, new Angle(0));
    }

    public Rectangle2D getRobotBounds() {
        return this.boundingBox.getBounds2D();
    }

    public LinePath getCurrentPath() {
        return this.currentPath;
    }

    public void updateInternalPositionFromVision(int centerX, int centerY, Angle angle, double confidenceModifier) {
        double deltaX = (centerX - this.boundingBox.getBounds().getCenterX()) * confidenceModifier;
        double deltaY = (centerY - this.boundingBox.getBounds().getCenterY()) * confidenceModifier;

        this.updateInternalPosition(deltaX, deltaY, angle);
    }

    public void tick(double deltaSeconds, Map map) {
        this.accelerationTracker.addAcceleration(this.getAcceleration(), deltaSeconds);
        if (this.accelerationTracker.isAccelerationSpike()) {
            this.handleVisionSpike(map);
        }

        double deltaX = (this.getVelocity() * Math.cos(this.getAngle().getRadians())) * deltaSeconds;
        double deltaY = (this.getVelocity() * Math.sin(this.getAngle().getRadians())) * deltaSeconds;
        this.updateInternalPosition(deltaX, deltaY, this.getAngle());

        this.followLinePath();
    }

    public void gotoLocation(double newX, double newY, Map map) {
        this.currentPath = new LinePath(map, this); // Always want to be updating the path with new map data
        this.currentPath.generatePath(this.getRobotBounds().getCenterX(), this.getRobotBounds().getCenterY(), newX, newY);
    }

    public Angle getAngle() {
        return this.angle;
    }

    public double getAcceleration() {
        return this.communicator.getAcceleration();
    }

    public double getVelocity() {
        return this.communicator.getVelocity();
    }

    public boolean isOverSocket() {
        return this.communicator instanceof SocketCommunicator;
    }

    public void drive(double percentSpeed) {
        this.communicator.move(percentSpeed);
    }

    public void turn(Angle angle) {
        this.communicator.turn(angle.getDegrees());
    }

    @Override
    public void paint(Graphics2D graphics2D) {
        graphics2D.setColor(Color.BLUE);
        graphics2D.fill(this.boundingBox);
    }

    private void updateInternalPosition(double deltaX, double deltaY, Angle angle) {
        this.angle = angle;

        Rectangle2D.Double rect = new Rectangle2D.Double();
        rect.setRect(this.boundingBox.getBounds().getCenterX() - (this.WIDTH / 2) + deltaX, this.boundingBox.getBounds().getCenterY() - (this.HEIGHT / 2) + deltaY, this.WIDTH, this.HEIGHT);

        AffineTransform at = AffineTransform.getRotateInstance(angle.getRadians(), rect.getCenterX(), rect.getCenterY());
        this.boundingBox = at.createTransformedShape(rect);
    }

    private void followLinePath() {
        if (this.currentPath != null) {
            PathPart currentPart = this.currentPath.getCurrentPath();
            if (currentPart != null) {
                currentPart.execute();
            }
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
