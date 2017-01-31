package com.gmail.jakekinsella.map.SolidObjects;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by jakekinsella on 12/19/16.
 */
public class SolidObject {
    private final double DECAY_PER_SECONDS = 0.1;

    private int x, y, width, height;
    private Shape bounds;
    private double chanceObjectIsReal; // Decays over time
    private boolean notDeletable;

    public SolidObject(int x, int y, int width, int height, double chanceObjectIsReal, boolean notDeletable) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.bounds = new Rectangle2D.Double(x, y, width, height);
        this.chanceObjectIsReal = chanceObjectIsReal;
        this.notDeletable = notDeletable;
    }

    public SolidObject(int x, int y, int width, int height, double chanceObjectIsReal) {
        this(x, y, width, height, chanceObjectIsReal, false);
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

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
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

    public void decayChanceObjectIsReal(double deltaSeconds) {
        if (this.chanceObjectIsReal != 1) {
            this.chanceObjectIsReal -= deltaSeconds * this.DECAY_PER_SECONDS;
        }
    }

    public void tick(double deltaSeconds) {
        this.decayChanceObjectIsReal(deltaSeconds);
    }
}
