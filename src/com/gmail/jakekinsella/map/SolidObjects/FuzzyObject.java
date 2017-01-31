package com.gmail.jakekinsella.map.SolidObjects;

/**
 * Created by jakekinsella on 12/19/16.
 * Is an object that is found by accelerometer and we can't be sure that it is real
 */
public class FuzzyObject extends SolidObject {

    public FuzzyObject(int x, int y, int width, int height) {
        super(x, y, width, height, 0.5);
    }
}
