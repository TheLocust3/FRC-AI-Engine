package com.gmail.jakekinsella.map.SolidObjects;

import com.gmail.jakekinsella.robot.Angle;

/**
 * Created by jakekinsella on 12/19/16.
 */
public class Robot extends SolidObject {

    private Angle angle;

    public Robot(int x, int y) {
        super(x, y, 50, 50, 0.5, "ROBOT");
    }

    public Robot(int x, int y, double angle) {
        this(x, y);
        this.angle = new Angle(angle);
    }

    public Robot(Robot robot1, Robot robot2) {
        this((robot1.getX() + robot2.getX()) / 2, (robot1.getY() + robot2.getY()) / 2);
    }

    public Angle getAngle() {
        return this.angle;
    }
}
