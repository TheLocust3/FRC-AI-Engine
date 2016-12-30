package com.gmail.jakekinsella.communicator.socket;

import org.json.simple.JSONArray;

import java.net.Socket;

/**
 * Created by jakekinsella on 12/27/16.
 */
public class MoveCommand extends Command {

    public MoveCommand(Socket socket, double speed) {
        super(socket, "MOVE", new JSONArray());

        this.args.add(speed);
    }
}
