import java.io.Serializable;

public class GameState implements Serializable {

    private int numberOfPlayers;

    public GameState() {
    }

    public synchronized int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public synchronized void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    @Override
    public String toString() {
        return "GameState{" +
                "numberOfPlayers=" + numberOfPlayers +
                '}';
    }

}
