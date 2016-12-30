package com.gmail.jakekinsella;

import com.gmail.jakekinsella.communicator.Communicator;
import com.gmail.jakekinsella.background.VisionCollector;
import com.gmail.jakekinsella.factory.LayerFactory;
import com.gmail.jakekinsella.factory.SocketLayerFactory;
import com.gmail.jakekinsella.map.Map;
import com.gmail.jakekinsella.robot.BaseRobot;

/**
 * Created by jakekinsella on 12/19/16.
 */
public class Main {

    public static void main(String args[]) {
        LayerFactory layerFactory = new SocketLayerFactory();

        Map map = new Map();

        Communicator communicator = layerFactory.createCommunicator();
        BaseRobot robot = layerFactory.createRobot(communicator);

        VisionCollector visionCollector = new VisionCollector(map, communicator);

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
