import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReference;

public class Player implements Serializable {

    private final AtomicReference<String> id;

    public Player(String id) {
        this.id = new AtomicReference<>(id);
    }

    public synchronized String getId() {
        return id.get();
    }

}
