package com.gmail.jakekinsella.map.SolidObjects;

import com.gmail.jakekinsella.robot.Angle;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Created by jakekinsella on 3/1/17.
 */
public class Wall extends SolidObject {

    private Angle angle;
    private Shape shape;

    public Wall(int x, int y, int width, int height, double degrees) {
        super(x, y, width, height, 1, true, "WALL");
        this.angle = new Angle(degrees);

        AffineTransform at = AffineTransform.getRotateInstance(this.angle.getRadians(), this.bounds.getX(), this.bounds.getY());
        this.shape = at.createTransformedShape(this.bounds);
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

    @Override
    public void paint(Graphics2D graphics2D) {
        graphics2D.setColor(Color.GRAY);
        graphics2D.fill(this.shape);
    }

    private double getDistance() {
        return Math.sqrt(Math.pow(this.getWidth(), 2) + Math.pow(this.getHeight(), 2));
    }
}
