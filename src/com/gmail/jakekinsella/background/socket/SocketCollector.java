package com.gmail.jakekinsella.background.socket;

import com.gmail.jakekinsella.robot.SocketRobot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by jakekinsella on 12/20/16.
 */
public class SocketCollector implements Runnable {

    private static final Logger logger = LogManager.getLogger();

    private Socket socket;
    private BufferedReader socketReader;

    private SocketRobot robot;

    public SocketCollector(SocketRobot robot) {
        this.robot = robot;

        try {
            this.socket = new Socket("127.0.0.1", 2345);
            this.socketReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        } catch (IOException e) {
            logger.error("Connecting to socket", e);
        }
    }

    public String waitForMapUpdate() {
        return ""; // TODO: Implement waiting for a map update
    }

    @Override
    public void run() {
        try {
            this.startupRoutine();
        } catch (Exception e) {
            logger.error("Failed to exectute the start routine with the server", e);
        }

        long lastTick = System.currentTimeMillis();
        while (true) {
            double deltaSeconds = (System.currentTimeMillis() - lastTick) / 1000.0;

            this.tick(deltaSeconds, this.robot);

            lastTick = System.currentTimeMillis();
        }
    }

    private void tick(double deltaSeconds, SocketRobot robot) {
        // TODO: Update robot position
        // TODO: Update map data
    }

    private void startupRoutine() throws Exception {
        Command command = this.readCommand();
        if (!(command instanceof ConnectCommand)) {
            throw new Exception("Server sent back incorrect response!");
        }

        new ConnectCommand(this.socket).sendCommand();

        command = this.readCommand();
        if (!(command instanceof GetCommand)) {
            throw new Exception("Server sent back incorrect response!");
        }
        if (!command.getArgAt(0).equals("api_version")) {
            throw new Exception("Server sent back incorrect response!");
        }

        // TODO: Finish up startup routine
    }

    private Command readCommand() {
        String rawCommand = this.readlnFromSocket(this.socketReader);
        Command command = new Command(this.socket, rawCommand);

        switch (command.getCommandName()) {
            case "GET":
                return new GetCommand(this.socket, (String) command.getArgAt(0));
            case "CONNECTED":
                return new ConnectCommand(this.socket);
            default:
                return command;
        }
    }

    private String readlnFromSocket(BufferedReader socketReader) {
        try {
            while (!socketReader.ready()) {
                logger.debug("Waiting to receive response from client...");
            }

            return socketReader.readLine();
        } catch (IOException e) {
            logger.error("Error in reading line from socket", e);
        }

        return null;
    }
}
