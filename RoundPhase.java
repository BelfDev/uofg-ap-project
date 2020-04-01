import java.util.Arrays;

/**
 * This enumeration holds all the game phase values.
 * They are used to determine certain behaviors and UI
 * changes along the game.
 */
public enum RoundPhase {
    INITIAL_BET(0),
    PLAYER_ACTION(1),
    DEALER_REVEAL(2);

    private int order;

    RoundPhase(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    public static RoundPhase getRoundOfOrder(int order) {
        return Arrays.stream(RoundPhase.values())
                .filter(r -> r.order == order)
                .findFirst()
                .orElse(null);
    }
}
