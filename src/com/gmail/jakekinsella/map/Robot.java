package com.gmail.jakekinsella.map;

/**
 * Created by jakekinsella on 12/19/16.
 */
public class Robot extends SolidObject{

    private String alliance;
    private double degrees;

    public Robot(int x, int y) {
        super(x, y, 50, 50, 0.5);
        this.alliance = alliance;
    }

    public Robot(int x, int y, double degrees) {
        this(x, y);
        this.degrees = degrees;
    }

    public String getAlliance() {
        return alliance;
    }
}
