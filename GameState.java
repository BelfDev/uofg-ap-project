import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class GameState implements Serializable {

    private AtomicReference<RoundPhase> roundPhase;
    // Indicates the game bottleneck (i.e. which player everyone is waiting for)
    private List<Player> players;
    private Stack<Integer> availableSlots;

    public GameState() {
        this.players = Collections.synchronizedList(new ArrayList<>());
        this.roundPhase = new AtomicReference<>(RoundPhase.INITIAL_BET);
        this.availableSlots = createSlots();
    }

    public int getNumberOfPlayers() {
        return players.size();
    }

    public RoundPhase getRoundPhase() {
        return roundPhase.get();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public synchronized Player getPlayer(String id) {
        return players.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public synchronized List<Player> getBottlenecks() {
        return players.stream()
                .filter(Player::isBottleneck)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public synchronized void addNewPlayer() {
        String playerId = UUID.randomUUID().toString();
        Integer slot = availableSlots.pop();
        Player player = new Player(playerId, slot);
        players.add(player);
    }

    public synchronized void removePlayer(String id) {
        Player player = players.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (player != null) {
            int newAvailableSlot = player.getSlot();
            players.remove(player);
            availableSlots.add(newAvailableSlot);
        }
    }

    public synchronized void advanceRound() {
        int currentOrder = roundPhase.get().getOrder();
        RoundPhase nextRoundPhase = RoundPhase.getRoundOfOrder(++currentOrder);
        roundPhase.set(nextRoundPhase);
    }

    private Stack<Integer> createSlots() {
        // Creates a stack with available slot numbers that will be consulted when positioning players
        Stack<Integer> availableSlots = new Stack<>();
        for (int i = Configs.MAX_NUMBER_OF_PLAYERS; i >= 1; i--) {
            availableSlots.add(i);
        }
        return availableSlots;
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
