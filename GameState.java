import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class GameState implements Serializable {

    private AtomicReference<RoundPhase> roundPhase;
    // Indicates the game bottleneck (i.e. which player everyone is waiting for)
    private Map<String, Player> playerMap;
    private Stack<Integer> availableSlots;

    private AtomicReference<Dealer> dealer;
    private List<PlayingCard> deck;

    public GameState() {
        this.playerMap = Collections.synchronizedMap(new LinkedHashMap<>());
        this.roundPhase = new AtomicReference<>(RoundPhase.INITIAL_BET);
        this.availableSlots = createSlots();
        this.dealer = new AtomicReference<>(new Dealer());
        this.deck = Collections.synchronizedList(PlayingCardFactory.getPlayingCardsDeck());
    }

    public synchronized int getNumberOfPlayers() {
        return playerMap.size();
    }

    public RoundPhase getRoundPhase() {
        return roundPhase.get();
    }

    public synchronized List<Player> getPlayers() {
        return new ArrayList<>(playerMap.values());
    }

    public synchronized Player getPlayer(String id) {
        if (id == null) return null;
        return playerMap.getOrDefault(id, null);
    }

    public Dealer getDealer() {
        return dealer.get();
    }

    public synchronized List<Player> getBottlenecks() {
        return playerMap.values().stream()
                .filter(Player::isBottleneck)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public synchronized void addNewPlayer() {
        String playerId = UUID.randomUUID().toString();
        Integer slot = availableSlots.pop();
        Player player = new Player(playerId, slot);
        playerMap.put(playerId, player);
    }

    public synchronized void removePlayer(String id) {
        Player player = playerMap.get(id);
        if (player != null) {
            int newAvailableSlot = player.getSlot();
            playerMap.remove(id);
            availableSlots.add(newAvailableSlot);
        }
    }

    public synchronized void advanceRound() {
        int currentOrder = roundPhase.get().getOrder();
        RoundPhase nextRoundPhase = RoundPhase.getRoundOfOrder(++currentOrder);
        roundPhase.set(nextRoundPhase);
    }

    public synchronized void dealCards() {
        int numberOfCards = deck.size();
        // Deals the initial dealer cards
        Dealer d = dealer.get();
        d.addCard(deck.get(--numberOfCards));
        // Deals two cards for each player
        for (Player player : getPlayers()) {
            player.addCard(deck.get(--numberOfCards));
            player.addCard(deck.get(--numberOfCards));
        }
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
        return "GameState{" +
                "\nnumberOfPlayers=" + getNumberOfPlayers() +
                ",\nroundPhase=" + roundPhase +
                ",\nplayerMap=" + playerMap +
                ",\navailableSlots=" + availableSlots +
                '}';
    }

}
