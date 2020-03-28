import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ClientRequest implements Serializable {

    private Command command;
    private String playerId;
    private Map<String, Object> payload;

    private ClientRequest() {
    }

    public Command getCommand() {
        return command;
    }

    public String getPlayerId() {
        return playerId;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public static class Builder {

        private Command command;
        private String playerId;
        private Map<String, Object> payload;

        public Builder(Command command, String playerId) {
            this.command = command;
            this.playerId = playerId;
            this.payload = new HashMap<>();
        }

        public Builder withData(String key, Object value) {
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
