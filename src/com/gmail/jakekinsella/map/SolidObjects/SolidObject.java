package com.gmail.jakekinsella.map.SolidObjects;

import com.gmail.jakekinsella.Paintable;
import com.gmail.jakekinsella.robot.RotatedRectangle;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by jakekinsella on 12/19/16.
 */
public class SolidObject implements Paintable {
    private final double DECAY_PER_SECONDS = 0.1;

    private int x, y, width, height;
    private double chanceObjectIsReal; // Decays over time
    private boolean notDeletable;
    private String type;

    Shape bounds;

    public SolidObject(int x, int y, int width, int height, double chanceObjectIsReal, boolean notDeletable, String type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.bounds = new Rectangle(x, y, width, height);
        this.chanceObjectIsReal = chanceObjectIsReal;
        this.notDeletable = notDeletable;
        this.type = type;
    }

    public SolidObject(int x, int y, int width, int height, double chanceObjectIsReal, String type) {
        this(x, y, width, height, chanceObjectIsReal, false, type);
    }

    public void setChanceObjectIsReal(double chanceObjectIsReal) {
        this.chanceObjectIsReal = chanceObjectIsReal;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getCenterX() {
        return (int) this.bounds.getBounds().getCenterX();
    }

    public int getCenterY() {
        return (int) this.bounds.getBounds().getCenterY();
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public String getType() {
        return this.type;
    }

    public boolean isNotDeletable() {
        return this.notDeletable;
    }

    public boolean isObjectReal() {
        return this.chanceObjectIsReal > 0;
    }

    public boolean doesIntersect(Shape shape) {
        return this.bounds.intersects(shape.getBounds().getX(), shape.getBounds().getY(), shape.getBounds().getWidth(), shape.getBounds().getHeight());
    }

    // TODO: This doesn't work with rotated shapes yet
    public boolean doesIntersectWithRotatedRectangle(RotatedRectangle rotatedRectangle) {
        if (rotatedRectangle.getShape().contains(this.getX(), this.getY())) {
            return true;
        }

        if (rotatedRectangle.getShape().contains(this.getX() + this.getWidth(), this.getY())) {
            return true;
        }

        if (rotatedRectangle.getShape().contains(this.getX(), this.getY() + this.getHeight())) {
            return true;
        }

        if (rotatedRectangle.getShape().contains(this.getX() + this.getWidth(), this.getY() + this.getHeight())) {
            return true;
        }

        return false;
    }

    public void decayChanceObjectIsReal(double deltaSeconds) {
        if (this.chanceObjectIsReal != 1) {
            this.chanceObjectIsReal -= deltaSeconds * this.DECAY_PER_SECONDS;
        }
    }

    public void tick(double deltaSeconds) {
        this.decayChanceObjectIsReal(deltaSeconds);
    }

    @Override
    public void paint(Graphics2D graphics2D) {
        graphics2D.setColor(Color.BLACK);
        graphics2D.fill(this.bounds);
    }
}
