import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class Player implements Serializable {

    private final AtomicInteger id;

    public Player(int id) {
        this.id = new AtomicInteger(id);
    }

    public int getId() {
        return id.get();
    }

}
