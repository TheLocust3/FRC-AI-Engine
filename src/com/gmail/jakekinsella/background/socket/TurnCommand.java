package com.gmail.jakekinsella.background.socket;

import org.json.simple.JSONArray;

import java.net.Socket;

/**
 * Created by jakekinsella on 12/27/16.
 */
public class TurnCommand extends Command {

    public TurnCommand(Socket socket, long speed) {
        super(socket, "TURN", new JSONArray());

        this.args.add(speed);
    }
}
