package com.gmail.jakekinsella.map.SolidObjects;

import com.gmail.jakekinsella.robot.Angle;
import com.gmail.jakekinsella.robot.pathing.PaddedLine;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Created by jakekinsella on 3/1/17.
 */
public class Wall extends SolidObject {

    private double slope, bValue;

    private Angle angle;

    public Wall(int x, int y, int width, int height, double degrees) {
        super(x, y, width, height, 1, true, "WALL");
        this.angle = new Angle(degrees);

        AffineTransform at = AffineTransform.getRotateInstance(this.angle.getRadians(), this.bounds.getBounds().getX(), this.bounds.getBounds().getY());
        this.bounds = at.createTransformedShape(this.bounds);

        this.setupIntersectionDetection();
    }

    public Angle getAngle() {
        return this.angle;
    }

    public int getEndX() {
        return (int) (this.getX() + (this.getDistance() * Math.cos(this.getAngle().getRadians())));
    }

    public int getEndY() {
        return (int) (this.getY() + (this.getDistance() * Math.sin(this.getAngle().getRadians())));
    }

    @Override
    public boolean doesIntersect(Shape rectangle) {
        return this.bounds.intersects(rectangle.getBounds().getX(), rectangle.getBounds().getY(), rectangle.getBounds().getWidth(), rectangle.getBounds().getHeight());
    }

    public double[] getIntersection(PaddedLine paddedLine) {
        double pointSlope = (paddedLine.getStartY() - paddedLine.getEndY()) / (paddedLine.getStartX() - paddedLine.getEndX());
        double pointBValue = paddedLine.getStartY() - (pointSlope * paddedLine.getStartX());

        double xIntersection, yIntersection;
        if (this.isVertical()) {
            yIntersection = pointSlope * this.getX() + pointBValue;
            xIntersection = (yIntersection - pointBValue) / pointSlope;
        } else {
            xIntersection = (pointBValue - this.bValue) / (this.slope - pointSlope); // Solve the two equations set equal to each other
            yIntersection = (this.slope * xIntersection) + this.bValue;
        }

        return new double[] {xIntersection, yIntersection};
    }

    @Override
    public void paint(Graphics2D graphics2D) {
        graphics2D.setColor(Color.GRAY);
        graphics2D.fill(this.bounds);
    }

    private void setupIntersectionDetection() {
        this.slope = ((double) this.getY() - this.getEndY()) / ((double) this.getX() - this.getEndX());
        this.bValue = (double) this.getY() - (this.slope * (double) this.getX()); // y = ax + b
    }

    private double getDistance() {
        return Math.sqrt(Math.pow(this.getWidth(), 2) + Math.pow(this.getHeight(), 2));
    }

    private boolean isVertical() {
        return this.getAngle().getDegrees() % 180 == 0;
    }
}
