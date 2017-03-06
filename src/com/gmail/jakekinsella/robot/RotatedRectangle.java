package com.gmail.jakekinsella.robot;

import com.gmail.jakekinsella.Paintable;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by jakekinsella on 1/30/17.
 */
public class RotatedRectangle implements Paintable {

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

    public Rectangle getShape() {
        Rectangle2D.Double rect = new Rectangle2D.Double(this.getStartX(), this.getStartY(), this.getWidth(), this.getHeight());
        AffineTransform at = AffineTransform.getRotateInstance(this.getAngle().getRadians());
        Shape shape = at.createTransformedShape(rect);

        return new Rectangle((int) shape.getBounds2D().getMinX(), (int) shape.getBounds2D().getMinY(), (int) shape.getBounds2D().getMaxX(), (int) shape.getBounds2D().getMaxY()); // TODO: use an actual rotated shape
    }

    public void setDistance(double distance) {
        this.update(distance);
    }

    public void rotate(Angle angle) {
        this.angle = angle;
        this.update();
    }

    @Override
    public void paint(Graphics2D graphics2D) {
        graphics2D.setColor(Color.CYAN);
        graphics2D.setStroke(new BasicStroke(4));

        graphics2D.drawLine((int) this.getStartX(), (int) this.getStartY(), (int) this.getEndX(), (int) this.getEndY());
    }

    private void update(double distance) {
        this.endX = (distance * Math.cos(this.angle.getRadians())) + this.startX;
        this.endY = (distance * Math.sin(this.angle.getRadians())) + this.startY;
    }

    private void update() {
        this.update(this.getLineDistance());
    }
}
