import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GameState implements Serializable {

    private final AtomicInteger numberOfPlayers;
    private List<Player> players = Collections.synchronizedList(new ArrayList<>());


    public GameState() {
        this.numberOfPlayers = new AtomicInteger(0);
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers.get();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void incrementNumberOfPlayers() {
        numberOfPlayers.incrementAndGet();
    }

    public void addNewPlayer() {
        int playerId = numberOfPlayers.incrementAndGet();
        Player player = new Player(playerId);
        players.add(player);
    }

    public void removePlayer(int id) {
        players.remove(id - 1);
        numberOfPlayers.decrementAndGet();
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
