package com.gmail.jakekinsella.robot.pathing;

import com.gmail.jakekinsella.Paintable;
import com.gmail.jakekinsella.robot.Angle;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Created by jakekinsella on 1/30/17.
 */
public class PaddedLine implements Paintable {

    private double startX, startY, endX, endY;
    private double width;
    private Angle angle;
    private Shape shape;

    public PaddedLine(double startX, double startY, double endX, double endY, double width) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.width = width;

        this.angle = new Angle(startX, startY, endX, endY);
        this.setup();
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

    public Angle getAngle() {
        return this.angle;
    }

    public double getLineDistance() {
        return Math.sqrt(Math.pow(this.getEndX() - this.getStartX(), 2) + Math.pow(this.getEndY() - this.getStartY(), 2));
    }

    public Shape getShape() {
        return shape;
    }

    public void setDistance(double distance) {
        this.update(distance);
    }

    public boolean isPointOnLine(double x, double y) {
        return this.shape.contains(x, y);
    }

    // Special method for checking angles with padding lines. This is a hack
    public boolean checkIfAngleClose(Angle angle, final double TOLERANCE) {
        // TODO: Eventually, this is going to break
        if (this.getAngle().getDegrees() < 0) {
            return Math.abs(angle.getDegrees() + this.getAngle().getDegrees()) < TOLERANCE;
        }

        return Math.abs(angle.getDegrees() - this.getAngle().getDegrees()) < TOLERANCE;
    }

    public void rotate(Angle angle) {
        this.angle = angle;
        this.update();
    }

    @Override
    public void paint(Graphics2D graphics2D) {
        graphics2D.setColor(Color.GREEN);
        graphics2D.setStroke(new BasicStroke(4));
        graphics2D.drawLine((int) this.getStartX(), (int) this.getStartY(), (int) this.getEndX(), (int) this.getEndY());
    }

    private void update(double distance) {
        this.endX = this.startX + (distance * Math.cos(this.angle.getRadians()));
        this.endY = this.startY + (distance * Math.sin(this.angle.getRadians()));

        this.setup();
    }

    private void update() {
        this.update(this.getLineDistance());
    }

    private void setup() {
        int xValues[] = {(int) (this.getStartX() - (this.getWidth() / 2)), (int) (this.getStartX() + (this.getWidth() / 2)), (int) (this.getStartX() + (this.getWidth() / 2)), (int) (this.getStartX() - (this.getWidth() / 2))};
        int yValues[] = {(int) this.getStartY(), (int) this.getStartY(), (int) (this.getStartY() + this.getLineDistance()), (int) (this.getStartY() + this.getLineDistance())};
        Polygon polygon = new Polygon(xValues, yValues, 4);

        AffineTransform at = AffineTransform.getRotateInstance(Math.PI - this.angle.getPaddedLineRadians(), polygon.getBounds2D().getCenterX(), polygon.getBounds2D().getY());
        this.shape = at.createTransformedShape(polygon);
    }
}
