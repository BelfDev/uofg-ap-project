import java.util.ArrayList;
import java.util.List;

/**
 * This class encapsulates the logic to notify game state changes.
 */
public class StateBroadcaster {
    private List<StateListener> listeners = new ArrayList<>();

    /**
     * Adds a state listener to the listeners list.
     *
     * @param listener the state listener.
     */
    public void addListener(StateListener listener) {
        listeners.add(listener);
    }

    /**
     * Notifies the given state to all observers.
     *
     * @param state new state to be broadcasted to observers.
     */
    public void notifyNewState(GameState state) {
        listeners.forEach(l -> l.onReceiveState(state));
    }

}
