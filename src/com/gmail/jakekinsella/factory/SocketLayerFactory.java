package com.gmail.jakekinsella.factory;

import com.gmail.jakekinsella.communicator.Communicator;
import com.gmail.jakekinsella.communicator.socket.SocketCommunicator;
import com.gmail.jakekinsella.robot.BaseRobot;
import com.gmail.jakekinsella.robot.SocketRobot;

/**
 * Created by jakekinsella on 12/29/16.
 */
public class SocketLayerFactory implements LayerFactory {

    @Override
    public BaseRobot createRobot(Communicator communicator) {
        return new SocketRobot(communicator);
    }

    @Override
    public Communicator createCommunicator() {
        return new SocketCommunicator();
    }
}
