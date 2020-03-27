import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Player implements Serializable {

    private final AtomicReference<String> id;

    private AtomicInteger balance;
    private AtomicInteger roundBet;

    public Player(String id) {
        this.id = new AtomicReference<>(id);
    }

    public synchronized String getId() {
        return id.get();
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
