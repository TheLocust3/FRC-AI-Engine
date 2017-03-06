package com.gmail.jakekinsella.debug;

import com.gmail.jakekinsella.map.Map;

import javax.swing.*;

/**
 * Created by jakekinsella on 3/6/17.
 */
public class DebugFrame extends JFrame {

    public int FRAME_WIDTH = 1000, FRAME_HEIGHT = 1000;

    public DebugFrame(Map map) {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);

        DebugCanvas debugCanvas = new DebugCanvas(map);

        this.add(debugCanvas);
    }
}
