package com.gmail.jakekinsella;

import com.gmail.jakekinsella.communicator.socket.SocketCommunicator;
import com.gmail.jakekinsella.background.VisionCollector;
import com.gmail.jakekinsella.map.Map;
import com.gmail.jakekinsella.robot.SocketRobot;
import com.gmail.jakekinsella.vision.SocketVision;

/**
 * Created by jakekinsella on 12/19/16.
 */
public class Main {

    public static void main(String args[]) {
        Map map = new Map();

        SocketCommunicator socketCommunicator = new SocketCommunicator();
        SocketRobot robot = new SocketRobot(socketCommunicator);
        SocketVision vision = new SocketVision(socketCommunicator);

        VisionCollector visionCollector = new VisionCollector(map, vision);

        new Thread(visionCollector).start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        robot.turn(180);

        long lastTick = System.currentTimeMillis();
        while (true) {
            double deltaSeconds = (System.currentTimeMillis() - lastTick) / 1000.0;

            map.tick(deltaSeconds);
            robot.tick(deltaSeconds, map);

            lastTick = System.currentTimeMillis();
        }
    }
}
