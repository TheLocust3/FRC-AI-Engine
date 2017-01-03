package com.gmail.jakekinsella.communicator;

import com.gmail.jakekinsella.communicator.Communicator;
import com.gmail.jakekinsella.communicator.socket.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by jakekinsella on 12/20/16.
 */
public class SocketCommunicator implements Communicator {

    private static final Logger logger = LogManager.getLogger();

    private Socket socket;
    private BufferedReader socketReader;

    public SocketCommunicator() {
        try {
            this.socket = new Socket("127.0.0.1", 2345);
            this.socketReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        } catch (IOException e) {
            logger.error("Connecting to socket", e);
        }

        this.setup();
    }

    @Override
    public double getDegrees() {
        return 0;
    }

    @Override
    public double getAcceleration() {
        return 0;
    }

    @Override
    public double getVelocity() {
        return 0;
    }

    @Override
    public void move(double percentSpeed) {
        assert(percentSpeed < -1.0 && percentSpeed > 1.0);

        new MoveCommand(this.socket, percentSpeed).sendCommand();
    }

    @Override
    public void turn(double angle) {
        new TurnCommand(this.socket, (long) angle).sendCommand();
    }

    @Override
    public ArrayList<int[]> getVisionUpdate() {
        Command command = readCommand();
        while (!command.getCommandName().equals("MAP_UPDATE")) {
            command = readCommand();
        }

        ArrayList<int[]> rawMap = new ArrayList<>();

        JSONObject mapUpdate = (JSONObject) command.getArgAt(0);
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

    private void setup() {
        try {
            this.startupRoutine();
            this.startupGame();
        } catch (Exception e) {
            logger.error("Failed to execute the start routine with the server", e);
        }
    }

    private void startupRoutine() throws Exception {
        logger.info("Beginning startup routine with server");

        Command command = this.readCommand();
        if (!command.getCommandName().equals("CONNECTED")) {
            throw new Exception("Server sent back incorrect response!");
        }

        JSONArray responseArgs = new JSONArray();
        responseArgs.add(true);
        new CommandResponse(this.socket, responseArgs).sendCommand();

        command = this.readCommand();
        if (!(command instanceof GetCommand) && !command.getArgAt(0).equals("api_version")) {
            throw new Exception("Server sent back incorrect response!");
        }

        responseArgs = new JSONArray();
        responseArgs.add(1.0);
        new CommandResponse(this.socket, responseArgs).sendCommand();

        command = this.readCommand();
        if (!(command instanceof GetCommand) && !command.getArgAt(0).equals("robot_name")) {
            throw new Exception("Server sent back incorrect response!");
        }

        responseArgs = new JSONArray();
        responseArgs.add("Momentum");
        new CommandResponse(this.socket, responseArgs).sendCommand();
    }

    private void startupGame() throws Exception {
        logger.info("Beginning game with server");

        Command command = this.readCommand();
        if (!command.getCommandName().equals("GAME_START")) {
            throw new Exception("Server sent back incorrect response!");
        }

        JSONArray responseArgs = new JSONArray();
        responseArgs.add(true);
        new CommandResponse(this.socket, responseArgs).sendCommand();

        logger.info("Game started");

        command = this.readCommand();
        if (!command.getCommandName().equals("START_AUTO")) {
            throw new Exception("Server sent back incorrect response!");
        }

        new CommandResponse(this.socket, responseArgs).sendCommand();;
        logger.info("Autonomous started");
    }

    private Command readCommand() {
        String rawCommand = this.readlnFromSocket(this.socketReader);
        Command command = new Command(this.socket, rawCommand);

        switch (command.getCommandName()) {
            case "GET":
                return new GetCommand(this.socket, (String) command.getArgAt(0));
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
