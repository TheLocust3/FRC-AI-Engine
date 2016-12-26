package com.gmail.jakekinsella.background.socket;

import org.json.simple.JSONArray;

import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by jakekinsella on 12/21/16.
 */
public class CommandResponse extends Command {

    public CommandResponse(Socket socket, JSONArray args) {
        super(socket, "COMMAND_RESPONSE", args);
    }
}
