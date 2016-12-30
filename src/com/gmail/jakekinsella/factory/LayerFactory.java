package com.gmail.jakekinsella.factory;

import com.gmail.jakekinsella.communicator.Communicator;
import com.gmail.jakekinsella.robot.BaseRobot;

/**
 * Created by jakekinsella on 12/29/16.
 */
public interface LayerFactory {
    BaseRobot createRobot(Communicator communicator);
    Communicator createCommunicator();
}
