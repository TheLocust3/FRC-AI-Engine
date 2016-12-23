package com.gmail.jakekinsella.background.socket;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by jakekinsella on 12/21/16.
 */
public class ConnectCommand extends Command {

    public ConnectCommand(Socket socket) {
        super(socket, "CONNECTED");
    }
}
