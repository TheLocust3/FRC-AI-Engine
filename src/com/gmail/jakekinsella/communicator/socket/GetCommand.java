package com.gmail.jakekinsella.communicator.socket;

import java.net.Socket;

/**
 * Created by jakekinsella on 12/21/16.
 */
public class GetCommand extends Command {

    public GetCommand(Socket socket, String variable) {
        super(socket, "GET", variable);
    }
}
