package com.gmail.jakekinsella.vision;

import communicator.socket.SocketCommunicator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Created by jakekinsella on 12/20/16.
 */
public class SocketVision extends BaseVision {

    private SocketCommunicator socketCommunicator;

    public SocketVision(SocketCommunicator socketCommunicator) {
        this.socketCommunicator = socketCommunicator;
    }

    @Override
    public ArrayList<int[]> waitForVisionData() {
        ArrayList<int[]> rawMap = new ArrayList<>();

        JSONObject mapUpdate = socketCommunicator.waitForMapUpdate();
        for (Object object : (JSONArray) mapUpdate.get("RED")) {
            JSONObject jsonObject = (JSONObject)object;
            int x = ((Long) jsonObject.get("x")).intValue();
            int y = ((Long) jsonObject.get("y")).intValue();
            rawMap.add(new int[]{x, y, 0});
        }

        for (Object object : (JSONArray) mapUpdate.get("BLUE")) {
            JSONObject jsonObject = (JSONObject)object;
            int x = ((Long) jsonObject.get("x")).intValue();
            int y = ((Long) jsonObject.get("y")).intValue();
            rawMap.add(new int[]{x, y, 1});
        }

        return rawMap;
    }
}
