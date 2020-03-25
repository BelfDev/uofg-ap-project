import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ClientRequest implements Serializable {

    private Command command;
    private String playerId;
    private Map<String, String> payload;

    private ClientRequest() {
    }

    public Command getCommand() {
        return command;
    }

    public String getPlayerId() {
        return playerId;
    }

    public Map<String, String> getPayload() {
        return payload;
    }

    public static class Builder {

        private Command command;
        private String playerId;
        private Map<String, String> payload;

        public Builder(Command command, String playerId) {
            this.command = command;
            this.playerId = playerId;
            this.payload = new HashMap<>();
        }

        public Builder withData(String key, String value) {
            payload.put(key, value);
            return this;
        }

        public ClientRequest build() {
            ClientRequest request = new ClientRequest();
            request.command = this.command;
            request.playerId = this.playerId;
            request.payload = this.payload;
            return request;
        }

    }
}
