import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Player implements Serializable {

    private final AtomicReference<String> id;
    private final AtomicInteger slot;

    private AtomicInteger balance;
    private AtomicInteger roundBet;

    public Player(String id, Integer slot) {
        this.id = new AtomicReference<>(id);
        this.slot = new AtomicInteger(slot);
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

    public void setRoundBet(int roundBet) {
        this.roundBet.set(roundBet);
    }

}
