import java.io.Serializable;
import java.util.Map;

public class ClientRequest implements Serializable {

    private Command command;
    private int playerId;
    private Map<String, String> payload;

    public ClientRequest(Command command, int playerId, Map<String, String> payload) {
        this.command = command;
        this.playerId = playerId;
        this.payload = payload;
    }

    public ClientRequest(Command command, int playerId) {
        this(command, playerId, null);
    }

    public Command getCommand() {
        return command;
    }

    public int getPlayerId() {
        return playerId;
    }

    public Map<String, String> getPayload() {
        return payload;
    }

}
