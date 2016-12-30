package com.gmail.jakekinsella.robot;

import java.util.ArrayList;

/**
 * Tracks acceleration over time to then be used to check if we hit an obstacle
 */
public class AccelerationTracker {

    public final double MAX_TIME = 1.0;
    public final double ACCEPTABLE_TIME = MAX_TIME - 0.25;
    public final double MAX_PERCENT_CHANGE = 55;

    private ArrayList<Double> accelerationList = new ArrayList<>();
    private ArrayList<Double> deltaTimes = new ArrayList<>();

    public void addAcceleration(double acceleration, double deltaTime) {
        if (this.sumDeltaTimes() >= this.MAX_TIME) {
            this.accelerationList.remove(0);
            this.deltaTimes.remove(0);
        }

        this.accelerationList.add(acceleration);
        this.deltaTimes.add(deltaTime);
    }

    public void clearAcceleration() {
        this.accelerationList.clear();
        this.deltaTimes.clear();
    }

    public boolean isAccelerationSpike() {
        if (this.sumDeltaTimes() < this.ACCEPTABLE_TIME) {
            return false;
        }

        double latestAcceleration = this.accelerationList.get(this.accelerationList.size() - 1);
        double avgAcceleration = this.getAvgAcceleration();
        double percentDifference = ((latestAcceleration - avgAcceleration) / avgAcceleration) * 100.0;

        return percentDifference > this.MAX_PERCENT_CHANGE;
    }

    private double sumDeltaTimes() {
        double sum = 0;
        for (double deltaTime : this.deltaTimes) {
            sum += deltaTime;
        }

        return sum;
    }

    private double getAvgAcceleration() {
        double sum = 0;
        for (double acceleration : this.accelerationList) {
            sum += acceleration;
        }

        return sum / accelerationList.size();
    }
}
