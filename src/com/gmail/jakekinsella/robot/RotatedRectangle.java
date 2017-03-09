package com.gmail.jakekinsella.robot;

import com.gmail.jakekinsella.Paintable;

import java.awt.*;

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

        this.angle = new Angle(startX, startY, endX, endY);
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

    public Angle getAngle() {
        return this.angle;
    }

    public double getLineDistance() {
        return Math.sqrt(Math.pow(this.getEndX() - this.getStartX(), 2) + Math.pow(this.getEndY() - this.getStartY(), 2));
    }

    public Shape getShape() {
        int xValues[] = {(int) (this.getStartX() - (this.getWidth() / 2)), (int) (this.getStartX() + (this.getWidth() / 2)), (int) (this.getEndX() + (this.getWidth() / 2)), (int) (this.getEndX() - (this.getWidth() / 2))};
        int yValues[] = {(int) this.getStartY(), (int) this.getStartY(),  (int) this.getEndY(), (int) this.getEndY()};

        return new Polygon(xValues, yValues, 4);
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
        graphics2D.setColor(Color.RED);
        graphics2D.fill(this.getShape());

        graphics2D.setColor(Color.GREEN);
        graphics2D.setStroke(new BasicStroke(5));

        graphics2D.drawLine((int) this.getStartX(), (int) this.getStartY(), (int) this.getEndX(), (int) this.getEndY());
    }

    private void update(double distance) {
        this.endX = this.startX + (distance * Math.cos(this.angle.getRadians()));
        this.endY = this.startY + (distance * Math.sin(this.angle.getRadians()));
    }

    private void update() {
        this.update(this.getLineDistance());
    }
}
