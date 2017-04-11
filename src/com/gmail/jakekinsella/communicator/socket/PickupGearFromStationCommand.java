package com.gmail.jakekinsella.communicator.socket;

import org.json.simple.JSONArray;

import java.net.Socket;

/**
 * Created by jakekinsella on 12/27/16.
 */
public class PickupGearFromStationCommand extends Command {

    public PickupGearFromStationCommand(Socket socket) {
        super(socket, "PICKUP_GEAR_STATION", new JSONArray());
    }
}
