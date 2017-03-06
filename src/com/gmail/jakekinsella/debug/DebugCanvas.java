package com.gmail.jakekinsella.debug;

import com.gmail.jakekinsella.map.Map;
import com.gmail.jakekinsella.robot.RobotControl;

import javax.swing.*;
import java.awt.*;

/**
 * Created by jakekinsella on 3/6/17.
 */
public class DebugCanvas extends JPanel {

    private Map map;
    private RobotControl robotControl;

    public DebugCanvas(Map map, RobotControl robotControl) {
        this.map = map;
        this.robotControl = robotControl;
    }

    @Override
    public void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        map.paint(graphics2D);
        robotControl.paint(graphics2D);

        if (robotControl.getCurrentPath() != null) {
            robotControl.getCurrentPath().paint(graphics2D);
        }
    }
}
