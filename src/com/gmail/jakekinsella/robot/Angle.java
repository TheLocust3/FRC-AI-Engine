package com.gmail.jakekinsella.robot;

/**
 * Created by jakekinsella on 1/6/17.
 */
public class Angle {

    //      90
    //  180 -|- 0
    //      270
    private double degrees;

    public Angle(double degrees) {
        this.degrees = degrees;
    }

    public Angle(double x1, double y1, double x2, double y2) {
        this.degrees = Math.toDegrees(Math.atan((y2 - y1) / (x1 - x2)));
    }

    public double getDegrees() {
        return this.degrees;
    }

    public double getRadians() {
        return Math.toRadians(this.getDegrees());
    }

    // degrees 0-360
    public double getNormalizedDegrees() {
        if (this.getDegrees() > 0) {
            return this.getDegrees() % 360;
        } else {
            return 360 - (this.getDegrees() % 360);
        }
    }
}
