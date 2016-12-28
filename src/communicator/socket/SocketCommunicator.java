package communicator.socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by jakekinsella on 12/20/16.
 */
public class SocketCommunicator {

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

    public JSONObject waitForMapUpdate() {
        Command command = readCommand();
        while (!command.getCommandName().equals("MAP_UPDATE")) {
            command = readCommand();
        }

        return (JSONObject) command.getArgAt(0);
    }

    public void move(double speed) {
        assert(speed < -1.0 && speed > 1.0);

        new MoveCommand(this.socket, speed).sendCommand();
    }

    public void turn(long angle) {
        new TurnCommand(this.socket, angle).sendCommand();
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
