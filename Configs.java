import java.awt.*;

class Configs {

    // Sockets
    public static final String SERVER_HOST = "127.0.0.1";
    public static int SERVER_PORT = 8888;

    // Client appearance
    // - Window
    public static final int INITIAL_WINDOW_WIDTH = 1200;
    public static final int INITIAL_WINDOW_HEIGHT = 800;

    // - Common dimensions
    public static final int BAR_BORDER_SIZE = 8;

    // - Top bar
    public static final int TOP_BAR_HEIGHT = 40 + BAR_BORDER_SIZE;
    public static final Color TOP_BAR_COLOR = new Color(31, 31, 29);
    public static final Color TOP_BAR_BOTTOM_BORDER_COLOR = new Color(18, 18, 14);

    // - Bottom bar
    public static final int BOTTOM_BAR_HEIGHT = 140 + BAR_BORDER_SIZE;
    public static final Color BOTTOM_BAR_INITIAL_GRADIENT_COLOR = new Color(151, 44, 0);
    public static final Color BOTTOM_BAR_FINAL_GRADIENT_COLOR = new Color(167, 62, 0);
    public static final Color BOTTOM_BAR_TOP_BORDER_COLOR = new Color(117, 28, 0);

    private Configs() {
    }
}