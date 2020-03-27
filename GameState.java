import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class GameState implements Serializable {

    private AtomicReference<RoundPhase> roundPhase;
    // Indicates the game bottleneck (i.e. which player everyone is waiting for)
    private AtomicReference<String>  bottleneck;
    private List<Player> players;

    public GameState() {
        this.players = Collections.synchronizedList(new ArrayList<>());
        this.roundPhase = new AtomicReference<>(RoundPhase.INITIAL_BET);
        this.bottleneck = null;
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

    public synchronized void advanceRound() {
        int currentOrder = roundPhase.get().getOrder();
        RoundPhase nextRoundPhase = RoundPhase.getRoundOfOrder(++currentOrder);
        roundPhase.set(nextRoundPhase);
    }

    @Override
    public String toString() {
        return " GameState {" +
                "\n\troundPhase = " + roundPhase.toString() +
                "\n\tnumberOfPlayers = " + players.size() +
                "\n\tplayers=" + players +
                "\n}";
    }

}
