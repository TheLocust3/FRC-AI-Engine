package com.gmail.jakekinsella.debug;

import com.gmail.jakekinsella.map.Map;

import javax.swing.*;
import java.awt.*;

/**
 * Created by jakekinsella on 3/6/17.
 */
public class DebugCanvas extends JPanel {

    private Map map;

    public DebugCanvas(Map map) {
        this.map = map;
    }

    @Override
    public void paintComponent(Graphics graphics) {
        map.paint((Graphics2D) graphics);
    }
}
