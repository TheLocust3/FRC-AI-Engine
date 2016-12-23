package com.gmail.jakekinsella.background;

import com.gmail.jakekinsella.map.Map;
import com.gmail.jakekinsella.vision.BaseVision;

/**
 * Created by jakekinsella on 12/20/16.
 */
public class VisionCollector implements Runnable {

    private Map map;
    private BaseVision vision;

    public VisionCollector(Map map, BaseVision vision) {
        this.map = map;
        this.vision = vision;
    }

    @Override
    public void run() {
        while (true) {
            this.map.inputVisionData(this.vision.waitForVisionData());
        }
    }
}
