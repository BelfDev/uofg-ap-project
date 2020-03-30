import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class GameState implements Serializable {

    private static final String AWAITING_FOR_BETS = "Please place your bets.";
    private static final String AWAITING_FOR_DEALER = "And the dealer reveal his cards!";

    private AtomicReference<RoundPhase> roundPhase;
    // Indicates the game bottleneck (i.e. which player everyone is waiting for)
    private Map<String, Player> playerMap;
    private Stack<Integer> availableSlots;
    private AtomicReference<Player> bottleneck;

    private AtomicReference<Dealer> dealer;
    private Stack<PlayingCard> deck;
    private AtomicReference<String> feedbackText;

    public GameState() {
        this.playerMap = Collections.synchronizedMap(new LinkedHashMap<>());
        this.roundPhase = new AtomicReference<>(RoundPhase.INITIAL_BET);
        this.availableSlots = createSlots();
        this.dealer = new AtomicReference<>(new Dealer());
        this.bottleneck = new AtomicReference<>(null);
        this.deck = PlayingCardFactory.getPlayingCardsDeck();
        this.feedbackText = new AtomicReference<>("Welcome to the Black Jack game!");
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

    public Player getBottleneck() {
        return bottleneck.get();
    }

    public void setBottleneck(Player player) {
        this.bottleneck.set(player);
    }

    public synchronized List<Player> getBottlenecks() {
        return playerMap.values().stream()
                .filter(Player::isBottleneck)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public String getFeedbackText() {
        return feedbackText.get();
    }

    public void setFeedbackText(String feedback) {
        this.feedbackText.set(feedback);
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
            });
        }
        this.bottleneck.set(getNextBottleneck());
        updateNewRoundFeedback();
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

    public synchronized Player getNextBottleneck() {
        return playerMap.values().stream()
                .filter(Player::isBottleneck)
                .max(Comparator.comparing(Player::getSlot))
                .orElse(null);
    }

    public synchronized PlayingCard dealCard() {
        return deck.pop();
    }

    public synchronized void dealInitialCards() {
        // Deals the initial dealer cards
        Dealer d = dealer.get();
        d.addCard(deck.pop());
        // Deals two cards for each player
        for (Player player : getPlayers()) {
            player.addCard(deck.pop());
            player.addCard(deck.pop());
        }
    }

    public synchronized List<Player> getEliminatedPlayers() {
        return playerMap.values().stream()
                .filter(Player::isEliminated)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private Stack<Integer> createSlots() {
        // Creates a stack with available slot numbers that will be consulted when positioning players
        Stack<Integer> availableSlots = new Stack<>();
        for (int i = Configs.MAX_NUMBER_OF_PLAYERS; i >= 1; i--) {
            availableSlots.add(i);
        }
        return availableSlots;
    }

    private void updateNewRoundFeedback() {
        switch (roundPhase.get()) {
            case INITIAL_BET:
                setFeedbackText(AWAITING_FOR_BETS);
                break;
            case DEALER_REVEAL:
                setFeedbackText(AWAITING_FOR_DEALER);
                break;
        }
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
