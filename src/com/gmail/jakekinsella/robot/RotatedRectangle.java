package com.gmail.jakekinsella.robot;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/**
 * Created by jakekinsella on 1/30/17.
 */
public class RotatedRectangle {

    private double startX, startY, endX, endY;
    private double width;
    private Angle angle;

    public RotatedRectangle(double startX, double startY, double endX, double endY, double width) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.width = width;

        this.angle = new Angle(startX, startY, endX, endY); // TODO: I feel a bit iffy about this
        this.update();
    }

    // TODO: Currently no way of getting the top right and bottom left points so max and min are very close
    // THIS TOTALLY DOESN'T WORK
    public double getStartX() {
        return this.startX;
    }

    public double getStartY() {
        return this.startY;
    }

    public double getEndX() {
        return this.endX;
    }

    public double getEndY() {
        return this.endY;
    }

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.getLineDistance() * Math.sin(this.getAngle().getRadians());
    }

    public Angle getAngle() {
        return this.angle;
    }

    public double getLineDistance() {
        return Math.sqrt(Math.pow(this.getEndX() - this.getStartX(), 2) + Math.pow(this.getEndY() - this.getStartY(), 2));
    }

    public Shape getShape() {
        Rectangle2D.Double rect = new Rectangle2D.Double(this.getStartX(), this.getStartY(), this.getWidth(), this.getHeight());
        AffineTransform at = AffineTransform.getRotateInstance(this.getAngle().getRadians());

        return at.createTransformedShape(rect);
    }

    public void rotate(Angle angle) {
        this.angle = angle;
        this.update();
    }

    private void update() {
        double distance = this.getLineDistance();
        this.endX = (distance * Math.cos(this.angle.getRadians())) + this.startX;
        this.endY = (distance * Math.sin(this.angle.getRadians())) + this.startY;
    }
}
