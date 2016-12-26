package com.gmail.jakekinsella.vision;

import com.gmail.jakekinsella.background.socket.SocketCollector;

import java.util.ArrayList;

/**
 * Created by jakekinsella on 12/20/16.
 */
public class SocketVision extends BaseVision {

    private SocketCollector socketCollector;

    public SocketVision(SocketCollector socketCollector) {
        this.socketCollector = socketCollector;
    }

    @Override
    public ArrayList<int[]> waitForVisionData() {
        String mapUpdate = socketCollector.waitForMapUpdate();
        // Do stuff with it

        return new ArrayList<>(); // TODO: Implement get vision data from socket
    }
}
