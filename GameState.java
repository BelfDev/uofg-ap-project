import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class GameState implements Serializable {

    private final AtomicInteger numberOfPlayers;

    public GameState() {
        this.numberOfPlayers = new AtomicInteger(0);
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers.get();
    }

    public void incrementNumberOfPlayers() {
        numberOfPlayers.incrementAndGet();
    }

    public void decrementNumberOfPlayers() {
        numberOfPlayers.decrementAndGet();
    }

    @Override
    public String toString() {
        return "GameState{" +
                "numberOfPlayers=" + numberOfPlayers +
                '}';
    }

}
