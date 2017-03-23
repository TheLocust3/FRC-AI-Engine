package com.gmail.jakekinsella.map.SolidObjects;

import com.gmail.jakekinsella.robot.Angle;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Created by jakekinsella on 12/19/16.
 */
public class LoadingStation extends SolidObject {

    private Angle angle;

    public LoadingStation(int x, int y, double degrees) {
        super(x, y, 30, 30, 1, "LOADING_STATION");

        this.angle = new Angle(degrees);

        AffineTransform at = AffineTransform.getRotateInstance(this.angle.getRadians(), this.bounds.getBounds().getX(), this.bounds.getBounds().getY());
        this.bounds = at.createTransformedShape(this.bounds);
    }

    @Override
    public void paint(Graphics2D graphics2D) {
        graphics2D.setColor(Color.GRAY);
        graphics2D.fill(this.bounds);
    }
}
