package communicator.socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by jakekinsella on 12/21/16.
 */
public class Command {

    private static final Logger logger = LogManager.getLogger();

    private PrintWriter socketOutput;
    private String commandName;
    protected JSONArray args;

    public Command(Socket socket, String commandName, JSONArray args) {
        try {
            this.socketOutput = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            logger.error("Error opening a write stream to the socket", e);
        }

        this.commandName = commandName;
        this.args = args;
    }

    public Command(Socket socket, String commandName, String... args) {
        this(socket, commandName, new JSONArray());

        this.args.add(args);
    }

    public Command(Socket socket, String rawJSON) {
        JSONParser parser = new JSONParser();
        Object obj = null;

        try {
            obj = parser.parse(rawJSON);
        } catch (ParseException e) {
            logger.error(rawJSON);
            logger.error("Error in parsing JSON", e);
        }

        JSONObject jsonObject = (JSONObject) obj;

        try {
            this.socketOutput = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            logger.error("Error opening a write stream to the socket", e);
        }
        this.commandName = (String) jsonObject.get("command");
        this.args = (JSONArray) jsonObject.get("args");
    }

    public String getCommandName() {
        return this.commandName;
    }

    public Object getArgAt(int index) {
        return this.args.get(index);
    }

    public void sendCommand() {
        JSONObject obj = new JSONObject();
        obj.put("command", this.commandName);

        obj.put("args", this.args);

        this.socketOutput.println(obj.toJSONString());
    }
}
