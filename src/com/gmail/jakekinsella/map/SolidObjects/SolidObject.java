package com.gmail.jakekinsella.map.SolidObjects;

import com.gmail.jakekinsella.Paintable;
import com.gmail.jakekinsella.robot.pathing.PaddedLine;

import java.awt.*;

/**
 * Created by jakekinsella on 12/19/16.
 */
public class SolidObject implements Paintable {

    private final double DECAY_PER_SECONDS = 0.1;

    private double chanceObjectIsReal; // Decays over time
    private boolean notDeletable;
    private String type;

    Shape bounds;

    public SolidObject(int x, int y, int width, int height, double chanceObjectIsReal, boolean notDeletable, String type) {
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
        return (int) this.bounds.getBounds().getX();
    }

    public int getY() {
        return (int) this.bounds.getBounds().getY();
    }

    public int getCenterX() {
        return (int) this.bounds.getBounds().getCenterX();
    }

    public void setCenterX(int x) {
        this.bounds = new Rectangle(x - (this.getWidth() / 2), this.getY(), this.getWidth(), this.getHeight());
    }

    public int getCenterY() {
        return (int) this.bounds.getBounds().getCenterY();
    }

    public void setCenterY(int y) {
        this.bounds = new Rectangle(this.getX(), y - (this.getHeight() / 2), this.getWidth(), this.getHeight());
    }

    public int getWidth() {
        return (int) this.bounds.getBounds().getWidth();
    }

    public int getHeight() {
        return (int) this.bounds.getBounds().getHeight();
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
    public boolean doesIntersectWithPaddedLine(PaddedLine paddedLine) {
        return paddedLine.getShape().intersects(this.getX(), this.getY(), this.getWidth(), this.getHeight());
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
