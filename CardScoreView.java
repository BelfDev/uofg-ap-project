import javax.swing.*;
import java.awt.*;

/**
 * This class provides a visual representation of
 * the sum of the card scores.
 */
public class CardScoreView extends JLabel {

    private static final int BORDER_RADIUS = 8;

    /**
     * Constructs a card score view with the given size.
     *
     * @param size the dimension of the score view.
     */
    public CardScoreView(Dimension size) {
        // Customizes the view
        this.setSize(size);
        this.setPreferredSize(size);
        this.setMaximumSize(size);
        this.setHorizontalAlignment(JLabel.CENTER);
        this.setVerticalAlignment(JLabel.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Dimension arcs = new Dimension(BORDER_RADIUS, BORDER_RADIUS);
        int width = getWidth();
        int height = getHeight();
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // Sets background color
        graphics.setColor(getBackground());
        // Ensure rounded background
        graphics.fillRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);
        // Paint border
        graphics.setColor(getForeground());
        graphics.drawRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height); //paint border
        super.paintComponent(g);
    }
}
