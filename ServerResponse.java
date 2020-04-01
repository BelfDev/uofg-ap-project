import java.io.Serializable;

/**
 * This class represents the server response understood by the client.
 * It contains the response status and the updated game state.
 */
public class ServerResponse implements Serializable {

    private ResponseStatus status;
    private GameState gameState;

    public ServerResponse(ResponseStatus status, GameState gameState) {
        this.status = status;
        this.gameState = gameState;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public GameState getGameState() {
        return gameState;
    }

    @Override
    public String toString() {
        return "ServerResponse{" +
                "status=" + status +
                ", gameState=" + gameState +
                '}';
    }

}
