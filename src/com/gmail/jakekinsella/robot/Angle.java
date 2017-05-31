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
        if (this.getDegrees() >= 0) {
            return this.getDegrees() % 360;
        }

        return 360 + (this.getDegrees() % 360);
    }

    // Only used in PaddedLine
    public double getPaddedLineRadians() {
        return 1.5 * Math.PI - this.getRadians();
    }

    // Random hack
    public double getRobotControlRadians() {
        return 2 * Math.PI - this.getRadians();
    }

    public String toString() {
        return String.valueOf(this.getDegrees());
    }

    public Angle calculateAngleBetween(Angle angle) {
        if (angle.getDegrees() > this.getDegrees()) {
            return new Angle(angle.getNormalizedDegrees() - this.getNormalizedDegrees());
        }

       return new Angle(angle.getDegrees() - this.getDegrees());
    }

    public boolean equals(Angle angle) {
        return this.getDegrees() == angle.getDegrees();
    }

    public boolean checkIfAnglesClose(Angle angle, final double TOLERANCE) {
        return Math.abs(angle.getNormalizedDegrees() - this.getNormalizedDegrees()) < TOLERANCE;
    }
}
