import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Player implements Serializable {

    private final AtomicReference<String> id;
    private final AtomicInteger slot;

    private AtomicInteger balance;
    private AtomicInteger roundBet;
    private AtomicBoolean isBottleneck;
    private AtomicInteger handScore;
    private AtomicBoolean isEliminated;

    private List<PlayingCard> cards;

    public Player(String id, Integer slot) {
        this.id = new AtomicReference<>(id);
        this.slot = new AtomicInteger(slot);
        this.balance = new AtomicInteger(Configs.PLAYER_INITIAL_BALANCE);
        this.roundBet = new AtomicInteger(0);
        this.isBottleneck = new AtomicBoolean(true);
        this.handScore = new AtomicInteger(0);
        this.cards = Collections.synchronizedList(new ArrayList<>());
        this.isEliminated = new AtomicBoolean(false);
    }

    public String getId() {
        return id.get();
    }

    public int getSlot() {
        return slot.get();
    }

    public int getRoundBet() {
        return roundBet.get();
    }

    public int getHandScore() {
        return handScore.get();
    }

    public int getBalance() {
        return balance.get();
    }

    public boolean getIsEliminated() {
        return isEliminated.get();
    }

    public List<PlayingCard> getCards() {
        return cards;
    }

    public void setBalance(int balance) {
        this.balance.set(balance);
    }

    public boolean isBottleneck() {
        return isBottleneck.get();
    }

    public void increaseRoundBet(int value) {
        this.roundBet.getAndAdd(value);
    }

    public void setIsBottleneck(boolean isBottleneck) {
        this.isBottleneck.set(isBottleneck);
    }

    public void setIsEliminated(boolean isEliminated) {
        this.isEliminated.set(isEliminated);
    }

    public synchronized void addCard(PlayingCard card) {
        int score = calculateScore(card);
        this.handScore.getAndAdd(score);
        this.cards.add(card);
    }

    public synchronized void removeAllCards() {
        this.handScore.set(0);
        this.cards = Collections.synchronizedList(new ArrayList<>());
    }

    private int calculateScore(PlayingCard card) {
        int score = this.handScore.get();
        if (card.getValue().equals("a")) {
            if (((score + 10) > 21)) {
                score = 1;
            } else {
                score = 11;
            }
        } else if (card.getValue().equals("j") || card.getValue().equals("q") || card.getValue().equals("k")) {
            score = 10;
        } else {
            score = Integer.parseInt(card.getValue());
        }
        return score;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id.get() +
                ", slot=" + slot.get() +
                ", balance=" + balance.get() +
                ", roundBet=" + roundBet.get() +
                ", isBottleneck=" + isBottleneck.get() +
                ", handScore=" + handScore.get() +
                '}';
    }
}
