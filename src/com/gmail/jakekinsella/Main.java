package com.gmail.jakekinsella;

import com.gmail.jakekinsella.communicator.Communicator;
import com.gmail.jakekinsella.background.VisionCollector;
import com.gmail.jakekinsella.communicator.SocketCommunicator;
import com.gmail.jakekinsella.debug.DebugFrame;
import com.gmail.jakekinsella.map.Map;
import com.gmail.jakekinsella.robot.RobotControl;
import com.gmail.jakekinsella.robot.routines.RoutineController;
import com.gmail.jakekinsella.robot.routines.socket.SocketRoutineController;

/**
 * Created by jakekinsella on 12/19/16.
 */
public class Main {

    public static void main(String args[]) throws InterruptedException {
        Map map = new Map();

        Communicator communicator = new SocketCommunicator();
        RobotControl robot = new RobotControl(communicator, 668, 100);
        RoutineController routineController = new SocketRoutineController(robot, communicator);

        VisionCollector visionCollector = new VisionCollector(map, communicator, robot);

        DebugFrame debugFrame = new DebugFrame(map, robot);
        if (args[0].toLowerCase().equals("debug")) {
            debugFrame.setVisible(true);
        }

        new Thread(visionCollector).start();

        //robot.gotoLocation(450, 250, map);
        routineController.pickupGearFromStation();

        long lastTick = System.currentTimeMillis();
        while (true) {
            double deltaSeconds = (System.currentTimeMillis() - lastTick) / 1000.0;

            map.tick(deltaSeconds);
            robot.tick(deltaSeconds, map);
            routineController.tick(map);

            if (args[0].toLowerCase().equals("debug")) {
                debugFrame.repaint();
            }

            lastTick = System.currentTimeMillis();

            Thread.sleep(100);
        }
    }
}
