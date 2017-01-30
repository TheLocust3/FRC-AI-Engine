package com.gmail.jakekinsella.robot;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/**
 * Created by jakekinsella on 1/30/17.
 */
public class RotatedRectangle {

    private double x1, y1, x2, y2;
    private Angle angle;
    private Rectangle2D.Double rectangle;

    // x1,y1 = bottom left
    // x2,y2 = top right
    public RotatedRectangle(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.angle = new Angle(x1, y1, x2, y2); // TODO: I feel a bit iffy about this
        this.rectangle = new Rectangle2D.Double();
        this.rectangle.setRect(x1, y1, x1 + Math.abs(x2 - x1), y1 + Math.abs(y2 - y1));
    }

    public double getX1() {
        return this.x1;
    }

    public double getY1() {
        return this.y1;
    }

    public double getX2() {
        return this.x2;
    }

    public double getY2() {
        return this.y2;
    }

    public double getWidth() {
        return this.getShape().getBounds2D().getWidth();
    }

    public double getHeight() {
        return this.getShape().getBounds2D().getHeight();
    }

    public Angle getAngle() {
        return this.angle;
    }

    public Shape getShape() {
        AffineTransform at = AffineTransform.getRotateInstance(this.angle.getRadians());
        return at.createTransformedShape(this.rectangle);
    }

    public double getLineDistance() {
        return this.getHeight();
    }
}
