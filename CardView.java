import javax.swing.*;
import java.awt.*;

/**
 * This class represents the playing card.
 */
public class CardView extends JLabel {

    private static final int BORDER_RADIUS = 8;

    private Image backgroundImage;

    /**
     * Constructs a card view with the given image name.
     */
    public CardView(String imageName) {
        backgroundImage = Utils.loadImage(String.format("%s.png", imageName));
        this.setSize(Configs.CARD_SIZE);
        this.setPreferredSize(Configs.CARD_SIZE);
        this.setMaximumSize(Configs.CARD_SIZE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Dimension arcs = new Dimension(BORDER_RADIUS, BORDER_RADIUS);
        int width = getWidth();
        int height = getHeight();
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // Ensure rounded background
        graphics.fillRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);
        // Apply card image
        g.drawImage(backgroundImage, 0, 0, width, height, this);
        // Paint border
        graphics.setColor(getForeground());
        graphics.drawRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height); //paint border
        super.paintComponent(g);
    }
}
