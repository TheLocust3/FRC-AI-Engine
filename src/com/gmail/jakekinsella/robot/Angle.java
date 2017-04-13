package com.gmail.jakekinsella.robot;

/**
 * Created by jakekinsella on 1/6/17.
 */
public class Angle {

    private double degrees;

    public Angle(double degrees) {
        this.degrees = degrees;
    }

    public Angle(double x1, double y1, double x2, double y2) {
        this.degrees = Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));
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
            return 360 + (this.getDegrees() % 360);
        }
    }

    //      90
    //  180 -|- 0
    //      270
    public double getAffineTransformRadians() {
        return this.getRadians() - 0.5 * Math.PI;
    }

    public String toString() {
        return String.valueOf(this.getDegrees());
    }

    public Angle calculateAngleBetween(Angle angle) {
        return new Angle(angle.getNormalizedDegrees() - this.getNormalizedDegrees());
    }

    public boolean equals(Angle angle) {
        return this.getDegrees() == angle.getDegrees();
    }

    public boolean checkIfAnglesClose(Angle angle, final double TOLERANCE) {
        return Math.abs(this.getNormalizedDegrees() - angle.getNormalizedDegrees()) < TOLERANCE;
    }
}
