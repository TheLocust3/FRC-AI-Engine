package com.gmail.jakekinsella.debug;

import com.gmail.jakekinsella.map.Map;
import com.gmail.jakekinsella.robot.RobotControl;

import javax.swing.*;

/**
 * Created by jakekinsella on 3/6/17.
 */
public class DebugFrame extends JFrame {

    public DebugFrame(Map map, RobotControl robotControl) {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(map.FRAME_WIDTH, map.FRAME_HEIGHT);

        DebugCanvas debugCanvas = new DebugCanvas(map, robotControl);

        this.add(debugCanvas);
    }
}
