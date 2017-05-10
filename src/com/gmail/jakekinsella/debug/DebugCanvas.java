package com.gmail.jakekinsella.debug;

import com.gmail.jakekinsella.map.Map;
import com.gmail.jakekinsella.robot.Angle;
import com.gmail.jakekinsella.robot.RobotControl;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Created by jakekinsella on 3/6/17.
 */
public class DebugCanvas extends JPanel {

    private Map map;
    private RobotControl robotControl;

    public DebugCanvas(Map map, RobotControl robotControl) {
        this.map = map;
        this.robotControl = robotControl;

        this.setPreferredSize(new Dimension(map.FRAME_WIDTH, map.FRAME_HEIGHT));
    }

    @Override
    public void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        this.robotControl.paint(graphics2D);
        this.map.paint(graphics2D);

        if (this.robotControl.getCurrentPath() != null) {
            this.robotControl.getCurrentPath().paint(graphics2D);
        }
    }
}
