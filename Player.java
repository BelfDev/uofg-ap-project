import java.io.Serializable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Player implements Serializable {

    private final AtomicReference<String> id;
    private final AtomicInteger slot;

    private AtomicInteger balance;
    private AtomicInteger roundBet;
    private AtomicBoolean isBottleneck;

    public Player(String id, Integer slot) {
        this.id = new AtomicReference<>(id);
        this.slot = new AtomicInteger(slot);
        this.balance = new AtomicInteger(Configs.PLAYER_INITIAL_BALANCE);
        this.roundBet = new AtomicInteger(0);
        this.isBottleneck = new AtomicBoolean(true);
    }

    public synchronized String getId() {
        return id.get();
    }

    public synchronized int getSlot() {
        return slot.get();
    }

    public synchronized int getRoundBet() {
        return roundBet.get();
    }

    public synchronized int getBalance() {
        return balance.get();
    }

    public synchronized void setBalance(int balance) {
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
}
