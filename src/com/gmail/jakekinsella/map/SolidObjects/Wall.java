package com.gmail.jakekinsella.map.SolidObjects;

import com.gmail.jakekinsella.robot.Angle;
import com.gmail.jakekinsella.robot.RotatedRectangle;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Created by jakekinsella on 3/1/17.
 */
public class Wall extends SolidObject {

    private double slope, bValue;

    private Angle angle;
    private Shape shape;

    public Wall(int x, int y, int width, int height, double degrees) {
        super(x, y, width, height, 1, true, "WALL");
        this.angle = new Angle(degrees);

        AffineTransform at = AffineTransform.getRotateInstance(this.angle.getRadians(), this.bounds.getX(), this.bounds.getY());
        this.shape = at.createTransformedShape(this.bounds);

        this.setupIntersectionDetection();
    }

    public Angle getAngle() {
        return this.angle;
    }

    public double getEndX() {
        return this.getX() + (this.getDistance() * Math.cos(this.getAngle().getRadians()));
    }

    public double getEndY() {
        return this.getY() + (this.getDistance() * Math.sin(this.getAngle().getRadians()));
    }

    @Override
    public boolean doesIntersect(Shape rectangle) {
        return this.shape.intersects(rectangle.getBounds().getX(), rectangle.getBounds().getY(), rectangle.getBounds().getWidth(), rectangle.getBounds().getHeight());
    }

    double xIntersection, yIntersection;
    public double[] getIntersection(RotatedRectangle rotatedRectangle) {
        double pointSlope = (rotatedRectangle.getStartY() - rotatedRectangle.getEndY()) / (rotatedRectangle.getStartX() - rotatedRectangle.getEndX());
        double pointBValue = rotatedRectangle.getStartY() - (pointSlope * rotatedRectangle.getStartX());

        if (this.slope != 0) {
            xIntersection = (pointBValue - this.bValue) / (this.slope - pointSlope); // Solve the two equations set equal to each other
            yIntersection = (this.slope * xIntersection) + this.bValue;
        } else {
            yIntersection = pointSlope * this.getX() + pointBValue;
            xIntersection = (yIntersection - pointBValue) / pointSlope;
        }

        return new double[] {xIntersection, yIntersection};
    }

    @Override
    public void paint(Graphics2D graphics2D) {
        graphics2D.setColor(Color.GRAY);
        graphics2D.fill(this.shape);

        graphics2D.setColor(Color.RED);
        graphics2D.fillOval((int) xIntersection, (int) yIntersection, 10, 10);
    }

    private void setupIntersectionDetection() {
        this.slope = ((double) this.getY() - this.getEndY()) / ((double) this.getX() - this.getEndX());
        this.bValue = (double) this.getY() - (this.slope * (double) this.getX()); // y = ax + b
    }

    private double getDistance() {
        return Math.sqrt(Math.pow(this.getWidth(), 2) + Math.pow(this.getHeight(), 2));
    }
}
