package com.gmail.jakekinsella.vision;

import com.gmail.jakekinsella.communicator.socket.SocketCommunicator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Collection;

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
        JSONArray allRobots = new JSONArray();
        allRobots.addAll((Collection) mapUpdate.get("RED"));
        allRobots.addAll((Collection) mapUpdate.get("BLUE"));

        for (Object object : allRobots) {
            JSONObject jsonObject = (JSONObject)object;
            int x = ((Long) jsonObject.get("x")).intValue();
            int y = ((Long) jsonObject.get("y")).intValue();
            rawMap.add(new int[]{x, y, 0});
        }

        return rawMap;
    }
}
