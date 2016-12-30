package com.gmail.jakekinsella.communicator;

import java.util.ArrayList;

/**
 * Created by jakekinsella on 12/29/16.
 */
public interface Communicator {
    void move(double speed);
    void turn(double angle);
    ArrayList<int[]> getVisionUpdate();
}
