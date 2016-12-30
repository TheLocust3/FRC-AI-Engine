package com.gmail.jakekinsella.background;

import com.gmail.jakekinsella.communicator.Communicator;
import com.gmail.jakekinsella.map.Map;

/**
 * Created by jakekinsella on 12/20/16.
 */
public class VisionCollector implements Runnable {

    private Map map;
    private Communicator communicator;

    public VisionCollector(Map map, Communicator communicator) {
        this.map = map;
        this.communicator = communicator;
    }

    @Override
    public void run() {
        while (true) {
            this.map.inputVisionData(this.communicator.getVisionUpdate());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
