package com.gmail.jakekinsella;

import com.gmail.jakekinsella.communicator.Communicator;
import com.gmail.jakekinsella.background.VisionCollector;
import com.gmail.jakekinsella.communicator.SocketCommunicator;
import com.gmail.jakekinsella.map.Map;
import com.gmail.jakekinsella.robot.RobotControl;

/**
 * Created by jakekinsella on 12/19/16.
 */
public class Main {

    public static void main(String args[]) {
        Map map = new Map();

        Communicator communicator = new SocketCommunicator();
        VisionCollector visionCollector = new VisionCollector(map, communicator);
        RobotControl robot = new RobotControl(communicator);

        new Thread(visionCollector).start();

        long lastTick = System.currentTimeMillis();
        while (true) {
            double deltaSeconds = (System.currentTimeMillis() - lastTick) / 1000.0;

            map.tick(deltaSeconds);
            robot.tick(deltaSeconds, map);

            lastTick = System.currentTimeMillis();
        }
    }
}
