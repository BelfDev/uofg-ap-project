import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;

class BottomBarView extends JPanel {

    private static final String INSTRUCTION_LABEL_TEXT = "Click on the chip to bet";

    public JButton yellowChipButton;
    public JButton redChipButton;
    public JButton greenChipButton;
    public JButton blueChipButton;

    public BottomBarView() {
        // Sets the bottom bar height
        Dimension size = new Dimension(0, Configs.BOTTOM_BAR_HEIGHT);
        this.setPreferredSize(size);
        // Sets the layout manager to a BoxLayout
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.X_AXIS);
        this.setLayout(boxLayout);
        // Sets the bottom bar top border
        this.setBorder(BorderFactory.createMatteBorder(Configs.BAR_BORDER_SIZE, 0, 0, 0, Configs.BOTTOM_BAR_TOP_BORDER_COLOR));

        setupPanels();
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

    private void setupPanels() {
        JPanel betPanel = createBetPanel();
        JPanel actionPanel = new JPanel();
        JPanel gameOptionsPanel = new JPanel();

        actionPanel.setBackground(Color.red);
        gameOptionsPanel.setBackground(Color.yellow);

        add(betPanel);
        add(actionPanel);
        add(gameOptionsPanel);
    }

    private JPanel createBetPanel() {
        // Creates the container panel
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        container.setOpaque(false);

        // Creates the instruction panel
        JPanel instructionPanel = new JPanel();
        JLabel instructionLabel = new JLabel(INSTRUCTION_LABEL_TEXT);
        instructionLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        Font f = instructionLabel.getFont();
        instructionLabel.setFont(new Font(f.getName(), Font.BOLD, 16));
        instructionLabel.setForeground(Color.white);
        instructionPanel.add(instructionLabel);

        // Creates the chip panel
        JPanel chipPanel = new JPanel();
        chipPanel.setLayout(new BoxLayout(chipPanel, BoxLayout.X_AXIS));
        chipPanel.setOpaque(false);

        // Creates the chip buttons
        yellowChipButton = createIconButton("yellow_chip_ic.png");
        redChipButton = createIconButton("red_chip_ic.png");
        greenChipButton = createIconButton("green_chip_ic.png");
        blueChipButton = createIconButton("blue_chip_ic.png");

        chipPanel.add(yellowChipButton);
        chipPanel.add(redChipButton);
        chipPanel.add(greenChipButton);
        chipPanel.add(blueChipButton);

        // Adds content panels to the container
        container.add(Box.createVerticalGlue());
        container.add(instructionLabel);
        container.add(Box.createVerticalGlue());
        container.add(chipPanel);
        container.add(Box.createVerticalGlue());

        return container;
    }

    private JButton createIconButton(String fileName) {
        JButton button = new JButton();
        Image image = loadImage(fileName);
        assert image != null;
        button.setIcon(new ImageIcon(image));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        return button;
    }

    private Image loadImage(String fileName) {
        try {
            return ImageIO.read(new File(fileName));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}