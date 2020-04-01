import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


/**
 * This class is the model game state. It is meant to be shared among all
 * client connections and should always be kept consistent -- therefore,
 * its implementation is Thread Safe.
 */
public class GameState implements Serializable {

    public static final String AWAITING_PLAYER_MESSAGE = "Waiting for player in slot ";
    private static final String AWAITING_FOR_BETS = "Please place your bets. Hit to confirm.";

    private AtomicReference<RoundPhase> roundPhase;
    // Mao to conveniently access players by their unique identifier strings
    private Map<String, Player> playerMap;
    private Stack<Integer> availableSlots;
    // Indicates the game bottleneck (i.e. which player everyone is waiting for)
    private AtomicReference<Player> bottleneck;

    private AtomicReference<Dealer> dealer;
    private Stack<PlayingCard> deck;
    private AtomicReference<String> feedbackText;

    /**
     * Constructs a thread safe model representation of the game state. This
     * class also provides some convenience methods ot manipulate such state.
     */
    public GameState() {
        this.playerMap = Collections.synchronizedMap(new LinkedHashMap<>());
        this.roundPhase = new AtomicReference<>(RoundPhase.INITIAL_BET);
        this.availableSlots = createSlots();
        this.dealer = new AtomicReference<>(new Dealer());
        this.bottleneck = new AtomicReference<>(null);
        this.deck = PlayingCardFactory.getPlayingCardsDeck();
        this.feedbackText = new AtomicReference<>(AWAITING_FOR_BETS);
    }

    // TRADITIONAL GETTERS

    public RoundPhase getRoundPhase() {
        return roundPhase.get();
    }

    public Dealer getDealer() {
        return dealer.get();
    }

    public Player getBottleneck() {
        return bottleneck.get();
    }

    public String getFeedbackText() {
        return feedbackText.get();
    }

    // CONVENIENCE GETTERS

    public synchronized int getNumberOfPlayers() {
        return playerMap.size();
    }

    public synchronized List<Player> getPlayers() {
        return new ArrayList<>(playerMap.values());
    }

    public synchronized Player getPlayer(String id) {
        if (id == null) return null;
        return playerMap.getOrDefault(id, null);
    }

    public synchronized List<Player> getBottlenecks() {
        return playerMap.values().stream()
                .filter(Player::isBottleneck)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public synchronized List<Player> getOngoingPlayers() {
        return playerMap.values().stream()
                .filter(p -> !p.isEliminated())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public synchronized Player getNextBottleneck() {
        return playerMap.values().stream()
                .filter(Player::isBottleneck)
                .max(Comparator.comparing(Player::getSlot))
                .orElse(null);
    }

    public synchronized List<Player> getEliminatedPlayers() {
        return playerMap.values().stream()
                .filter(Player::isEliminated)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    // SETTERS

    public void setBottleneck(Player player) {
        this.bottleneck.set(player);
    }

    public void setFeedbackText(String feedback) {
        this.feedbackText.set(feedback);
    }

    // OPERATIONS

    public synchronized void addNewPlayer() {
        String playerId = UUID.randomUUID().toString();
        Integer slot = availableSlots.pop();
        Player player = new Player(playerId, slot);
        player.setIsBottleneck(roundPhase.get().equals(RoundPhase.INITIAL_BET));
        playerMap.put(playerId, player);
    }

    public synchronized void removePlayer(String id) {
        Player player = playerMap.get(id);
        if (player != null) {
            int newAvailableSlot = player.getSlot();
            playerMap.remove(id);
            availableSlots.add(newAvailableSlot);
            this.bottleneck.set(getNextBottleneck());
        }
    }

    public synchronized void advanceRound() {
        RoundPhase nextRoundPhase;
        nextRoundPhase = getNextRoundPhase();
        roundPhase.set(nextRoundPhase);
        if (nextRoundPhase != RoundPhase.DEALER_REVEAL) {
            playerMap.values().forEach(p -> {
                p.setIsBottleneck(true);
                p.removeAllCards();
                p.setIsEliminated(false);
                p.setIsWinner(false);
                p.setIsPush(false);
            });
        }
        this.bottleneck.set(getNextBottleneck());
        updateNewRoundFeedback();
    }

    public synchronized void resetDealer() {
        this.dealer = new AtomicReference<>(new Dealer());
    }


    public synchronized PlayingCard dealCard() {
        return deck.pop();
    }

    public synchronized void dealInitialCards() {
        // Reshuffles the deck
        this.deck = PlayingCardFactory.getPlayingCardsDeck();
        // Deals the initial dealer cards
        Dealer d = dealer.get();
        d.addCard(deck.pop());
        // Deals two cards for each player
        for (Player player : getPlayers()) {
            player.addCard(deck.pop());
            player.addCard(deck.pop());
        }
    }

    // CONVENIENCE METHODS

    private Stack<Integer> createSlots() {
        // Creates a stack with available slot numbers that will be consulted when positioning players
        Stack<Integer> availableSlots = new Stack<>();
        for (int i = Configs.MAX_NUMBER_OF_PLAYERS; i >= 1; i--) {
            availableSlots.add(i);
        }
        return availableSlots;
    }

    private RoundPhase getNextRoundPhase() {
        RoundPhase nextRoundPhase;
        if (roundPhase.get() == RoundPhase.DEALER_REVEAL) {
            nextRoundPhase = RoundPhase.INITIAL_BET;
        } else {
            int currentOrder = roundPhase.get().getOrder();
            nextRoundPhase = RoundPhase.getRoundOfOrder(++currentOrder);
        }
        return nextRoundPhase;
    }

    private void updateNewRoundFeedback() {
        switch (roundPhase.get()) {
            case INITIAL_BET:
                setFeedbackText(AWAITING_FOR_BETS);
                break;
            case PLAYER_ACTION:
                setFeedbackText(AWAITING_PLAYER_MESSAGE + bottleneck.get().getSlot() + ".");
        }
    }

    @Override
    public String toString() {
        // Simplified description of the game state
        return "GameState{" +
                "\n roundPhase=" + roundPhase +
                ",\n availableSlots=" + availableSlots +
                ",\n bottleneck=" + bottleneck +
                ",\n numberOfPlayers=" + getPlayers().size() +
                '}';
    }

}
