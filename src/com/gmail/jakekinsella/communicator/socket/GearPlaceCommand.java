package com.gmail.jakekinsella.communicator.socket;

import org.json.simple.JSONArray;

import java.net.Socket;

/**
 * Created by jakekinsella on 12/27/16.
 */
public class GearPlaceCommand extends Command {

    public GearPlaceCommand(Socket socket) {
        super(socket, "PLACE_GEAR", new JSONArray());
    }
}
