import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


/**
 * This class is a thread safe model representation of the Player entity.
 * It also provides a few methods for conveniently modifying its state.
 */
public class Player implements Serializable {

    private final AtomicReference<String> id;
    private final AtomicInteger slot;

    private List<PlayingCard> cards;
    private AtomicReference<Double> balance;
    private AtomicInteger roundBet;
    private AtomicBoolean isBottleneck;
    private AtomicInteger handScore;

    private AtomicBoolean isEliminated;
    private AtomicBoolean isWinner;
    private AtomicBoolean isPush;

    private AtomicInteger numberOfElevenAces;

    /**
     * Constructs the player model.
     *
     * @param id   an unique identifier String.
     * @param slot the table slot where the player should be placed in.
     */
    public Player(String id, Integer slot) {
        this.id = new AtomicReference<>(id);
        this.slot = new AtomicInteger(slot);
        this.balance = new AtomicReference<Double>(Configs.PLAYER_INITIAL_BALANCE);
        this.roundBet = new AtomicInteger(0);
        this.isBottleneck = new AtomicBoolean(true);
        this.handScore = new AtomicInteger(0);
        this.cards = Collections.synchronizedList(new ArrayList<>());
        this.isEliminated = new AtomicBoolean(false);
        this.numberOfElevenAces = new AtomicInteger(0);
        this.isPush = new AtomicBoolean(false);
        this.isWinner = new AtomicBoolean(false);
    }

    // GETTERS

    public String getId() {
        return id.get();
    }

    public int getSlot() {
        return slot.get();
    }

    public double getBalance() {
        return balance.get();
    }

    public int getRoundBet() {
        return roundBet.get();
    }

    public boolean isBottleneck() {
        return isBottleneck.get();
    }

    public int getHandScore() {
        return handScore.get();
    }

    public List<PlayingCard> getCards() {
        return cards;
    }

    public boolean isEliminated() {
        return isEliminated.get();
    }

    public boolean isPush() {
        return isPush.get();
    }

    public boolean isWinner() {
        return isWinner.get();
    }

    // SETTERS

    public void setBalance(double balance) {
        this.balance.set(balance);
    }

    public void increaseRoundBet(int value) {
        this.roundBet.getAndAdd(value);
    }

    public void resetRoundBet() {
        this.roundBet.set(0);
    }

    public void setIsBottleneck(boolean isBottleneck) {
        this.isBottleneck.set(isBottleneck);
    }

    public void setIsEliminated(boolean isEliminated) {
        this.isEliminated.set(isEliminated);
    }

    public void setIsPush(boolean isPush) {
        this.isPush.set(isPush);
    }

    public void setIsWinner(boolean isWinner) {
        this.isWinner.set(isWinner);
    }

    // OPERATIONS

    public synchronized void addCard(PlayingCard card) {
        int score = calculateScore(card);
        this.handScore.getAndAdd(score);
        this.cards.add(card);
    }

    public synchronized void removeAllCards() {
        this.handScore.set(0);
        this.cards = Collections.synchronizedList(new ArrayList<>());
    }

    // CONVENIENCE METHODS

    private int calculateScore(PlayingCard card) {
        int score = this.handScore.get();
        if (card.getValue().equals("a")) {
            if (((score + 10) > 21) || numberOfElevenAces.get() == 1) {
                score = 1;
            } else {
                score = 11;
                numberOfElevenAces.getAndIncrement();
            }
        } else if (card.getValue().equals("j") || card.getValue().equals("q") || card.getValue().equals("k")) {
            score = 10;
        } else {
            score = Integer.parseInt(card.getValue());
        }
        // Corrects the value of Aces if necessary
        return correctAceValueIfNeeded(score);
    }

    private int correctAceValueIfNeeded(int score) {
        if (this.cards.size() > 0) {
            for (int i = 0; i < numberOfElevenAces.get(); i++) {
                if (score - 10 <= 21) {
                    numberOfElevenAces.getAndDecrement();
                    return score;
                }
            }
        }
        return score;
    }


    @Override
    public String toString() {
        // Simplified description of a Player
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
