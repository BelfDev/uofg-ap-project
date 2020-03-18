import javax.swing.*;
import java.awt.*;

class BottomBarView extends JPanel {

    public BottomBarView() {
        // Sets the bottom bar height
        Dimension size = new Dimension(0, Configs.BOTTOM_BAR_HEIGHT);
        this.setPreferredSize(size);
        // Sets the layout manager to a GridLayout
        GridLayout gridLayout = new GridLayout(0, 3);
        this.setLayout(gridLayout);
        // Sets the bottom bar top border
        this.setBorder(BorderFactory.createMatteBorder(Configs.BAR_BORDER_SIZE, 0, 0, 0, Configs.BOTTOM_BAR_TOP_BORDER_COLOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Paints the background with a linear gradient color
        paintGradientBackground(g, Configs.BOTTOM_BAR_INITIAL_GRADIENT_COLOR, Configs.BOTTOM_BAR_FINAL_GRADIENT_COLOR);
    }

    private void paintGradientBackground(Graphics g, Color initialColor, Color finalColor) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        GradientPaint paint = new GradientPaint(0, 0, initialColor, 0, getHeight(), finalColor);
        g2d.setPaint(paint);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

}