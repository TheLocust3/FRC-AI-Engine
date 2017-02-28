package com.gmail.jakekinsella.robot;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/**
 * Created by jakekinsella on 1/30/17.
 */
public class RotatedRectangle {

    private Angle angle;
    private Rectangle2D.Double rectangle;
    private Shape shape;

    // x1,y1 = bottom left
    // x2,y2 = top right
    public RotatedRectangle(double x1, double y1, double x2, double y2) {
        this.angle = new Angle(x1, y1, x2, y2); // TODO: I feel a bit iffy about this
        this.rectangle = new Rectangle2D.Double();
        this.rectangle.setRect(x1, y1, x1 + Math.abs(x2 - x1), y1 + Math.abs(y2 - y1));
        this.updateShape();
    }

    // TODO: Currently no way of getting the top right and bottom left points so max and min are very close
    public double getMinX() {
        return this.shape.getBounds().getMinX();
    }

    public double getMinY() {
        return this.shape.getBounds().getMinY();
    }

    public double getMaxX() {
        return this.shape.getBounds().getMaxX();
    }

    public double getMaxY() {
        return this.shape.getBounds().getMaxY();
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
        return this.shape;
    }

    public double getLineDistance() {
        return this.getHeight();
    }

    public void rotate(Angle angle) {
        this.angle = angle;
        this.updateShape();
    }

    private void updateShape() {
        AffineTransform at = AffineTransform.getRotateInstance(this.angle.getRadians());
        this.shape = at.createTransformedShape(this.rectangle);
    }
}
