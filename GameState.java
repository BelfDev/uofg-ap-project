import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class GameState implements Serializable {

    private List<Player> players;

    public GameState() {
        this.players = Collections.synchronizedList(new ArrayList<>());
    }

    public int getNumberOfPlayers() {
        return players.size();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public synchronized void addNewPlayer() {
        String playerId = UUID.randomUUID().toString();
        Player player = new Player(playerId);
        players.add(player);
    }

    public synchronized void removePlayer(String id) {
        Player player = players.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
        players.remove(player);
    }

    @Override
    public String toString() {
        return "GameState{" +
                "numberOfPlayers = " + players.size() +
                " players=" + players +
                '}';
    }

}
