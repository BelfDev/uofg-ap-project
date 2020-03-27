import java.awt.*;
import java.util.HashMap;
import java.util.Map;

class Configs {

    public static final int MAX_NUMBER_OF_PLAYERS = 5;

    // Sockets
    public static final String SERVER_HOST = "127.0.0.1";
    public static int SERVER_PORT = 8765;

    // Client appearance
    // - Window
    public static final int INITIAL_WINDOW_WIDTH = 1200;
    public static final int INITIAL_WINDOW_HEIGHT = 800;

    // - Common dimensions
    public static final int BAR_BORDER_SIZE = 8;

    // - Top bar
    public static final int TOP_BAR_HEIGHT = 40 + BAR_BORDER_SIZE;
    public static final int TOP_BAR_STATIC_LABEL_FONT_SIZE = 10;
    public static final Color TOP_BAR_COLOR = new Color(31, 31, 29);
    public static final Color TOP_BAR_BORDER_COLOR = new Color(18, 18, 14);

    // - Bottom bar
    public static final int BOTTOM_BAR_HEIGHT = 128 + BAR_BORDER_SIZE;
    public static final int BUTTON_FONT_SIZE = 16;
    public static final Color BOTTOM_BAR_INITIAL_GRADIENT_COLOR = new Color(151, 44, 0);
    public static final Color BOTTOM_BAR_FINAL_GRADIENT_COLOR = new Color(167, 62, 0);
    public static final Color BOTTOM_BAR_TOP_BORDER_COLOR = new Color(117, 28, 0);

    // Player view
    public static final Dimension CARD_SIZE = new Dimension(60, 90);
    public static final Map<Integer, Point> PLAYER_SLOT_LOCATIONS = new HashMap<Integer, Point>() {{
        put(1, new Point(8, 240));
        put(2, new Point(256, 306));
        put(3, new Point(524, 324));
        put(4, new Point(780, 306));
        put(5, new Point(1038, 240));
    }};
    public static final Point DEALER_SLOT_LOCATION = new Point(308, 100);

    private Configs() {
    }
}