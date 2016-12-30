package com.gmail.jakekinsella.map;

/**
 * Created by jakekinsella on 12/19/16.
 */
public class Robot extends SolidObject{

    private String alliance;

    public Robot(int x, int y) {
        super(x, y, 50, 50, 0.5);
        this.alliance = alliance;
    }

    public String getAlliance() {
        return alliance;
    }
}
