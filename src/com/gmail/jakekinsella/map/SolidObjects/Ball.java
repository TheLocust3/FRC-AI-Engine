package com.gmail.jakekinsella.map.SolidObjects;

/**
 * Created by jakekinsella on 12/19/16.
 */
public class Ball extends SolidObject {

    public Ball(int x, int y, boolean notDeletable) {
        super(x, y, 10, 10, 1, notDeletable);
    }

    public Ball(int x, int y) {
        this(x, y, false);
    }
}
