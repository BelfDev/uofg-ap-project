import java.util.ArrayList;
import java.util.List;

public class StateBroadcaster {
    private List<StateListener> listeners = new ArrayList<>();

    public void addListener(StateListener listener) {
        listeners.add(listener);
    }

    public void notifyNewState(GameState state) {
        System.out.println("new state received");
        listeners.forEach(l -> l.onReceiveState(state));
    }

}
