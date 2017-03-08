package com.gmail.jakekinsella;

import com.gmail.jakekinsella.communicator.Communicator;
import com.gmail.jakekinsella.background.VisionCollector;
import com.gmail.jakekinsella.communicator.SocketCommunicator;
import com.gmail.jakekinsella.debug.DebugFrame;
import com.gmail.jakekinsella.map.Map;
import com.gmail.jakekinsella.robot.RobotControl;

/**
 * Created by jakekinsella on 12/19/16.
 */
public class Main {

    public static void main(String args[]) throws InterruptedException {
        Map map = new Map();

        Communicator communicator = new SocketCommunicator();
        RobotControl robot = new RobotControl(communicator);
        VisionCollector visionCollector = new VisionCollector(map, communicator, robot);

        DebugFrame debugFrame = new DebugFrame(map, robot);
        if (args[0].toLowerCase().equals("debug")) {
            debugFrame.setVisible(true);
        }

        new Thread(visionCollector).start();

        robot.gotoLocation(10, 10, map);

        long lastTick = System.currentTimeMillis();
        while (true) {
            double deltaSeconds = (System.currentTimeMillis() - lastTick) / 1000.0;

            map.tick(deltaSeconds);
            robot.tick(deltaSeconds, map);

            if (args[0].toLowerCase().equals("debug")) {
                debugFrame.repaint();
            }

            lastTick = System.currentTimeMillis();

            Thread.sleep(100);
        }
    }
}
