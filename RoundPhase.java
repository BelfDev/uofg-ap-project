import java.util.Arrays;

public enum RoundPhase {
    INITIAL_BET(0);

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
