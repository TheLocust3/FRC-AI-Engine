package com.gmail.jakekinsella;

import com.gmail.jakekinsella.background.socket.SocketCollector;
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
        SocketRobot robot = new SocketRobot();

        SocketCollector socketCollector = new SocketCollector(robot);
        SocketVision vision = new SocketVision(socketCollector);

        VisionCollector visionCollector = new VisionCollector(map, vision);

        new Thread(visionCollector).start();
        new Thread(socketCollector).start();

        long lastTick = System.currentTimeMillis();
        while (true) {
            double deltaSeconds = (System.currentTimeMillis() - lastTick) / 1000.0;

            map.tick(deltaSeconds);
            robot.tick(deltaSeconds, map);

            lastTick = System.currentTimeMillis();
        }
    }
}
