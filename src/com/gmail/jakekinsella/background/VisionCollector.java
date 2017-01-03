package com.gmail.jakekinsella.background;

import com.gmail.jakekinsella.communicator.Communicator;
import com.gmail.jakekinsella.map.Map;
import com.gmail.jakekinsella.robot.RobotControl;

/**
 * Created by jakekinsella on 12/20/16.
 */
public class VisionCollector implements Runnable {

    private Map map;
    private Communicator communicator;
    private RobotControl robotControl;

    public VisionCollector(Map map, Communicator communicator, RobotControl robotControl) {
        this.map = map;
        this.communicator = communicator;
        this.robotControl = robotControl;
    }

    @Override
    public void run() {
        while (true) {
            this.map.inputVisionData(this.communicator.getVisionUpdate(), robotControl);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
