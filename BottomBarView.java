import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class encapsulates the contents of the BottomBarView.
 * It provides the player with game controls to bet chips and
 * perform actions such as DEAL, STAND, DOUBLE, and SPLIT.
 * It also enables the player to QUIT the game.
 */
class BottomBarView extends JPanel {

    private static final String INSTRUCTION_LABEL_TEXT = "Click on the chip to bet";

    // Chip buttons
    private JButton yellowChipButton;
    private JButton redChipButton;
    private JButton greenChipButton;
    private JButton blueChipButton;

    // Control buttons
    private JButton hitButton;
    private JButton standButton;
    private JButton doubleButton;
    private JButton resetBetButton;
    private JButton quitButton;

    /**
     * Constructs the BottomBarView with game controls.
     */
    public BottomBarView() {
        // Sets the bottom bar height
        Dimension size = new Dimension(0, Configs.BOTTOM_BAR_HEIGHT);
        this.setPreferredSize(size);
        // Sets the layout manager to a BoxLayout
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.X_AXIS);
        this.setLayout(boxLayout);
        // Sets the bottom bar top border
        this.setBorder(BorderFactory.createMatteBorder(Configs.BAR_BORDER_SIZE, 0, 0, 0, Configs.BOTTOM_BAR_TOP_BORDER_COLOR));
        // Adds content panels
        addPanels();
    }

    // GETTERS

    public List<JButton> getChipButtons() {
        return new ArrayList<JButton>() {{
            add(yellowChipButton);
            add(redChipButton);
            add(greenChipButton);
            add(blueChipButton);
        }};
    }

    public JButton getHitButton() {
        return hitButton;
    }

    public JButton getStandButton() {
        return standButton;
    }

    public JButton getDoubleButton() {
        return doubleButton;
    }

    public JButton getResetBetButton() {
        return resetBetButton;
    }

    public JButton getQuitButton() {
        return quitButton;
    }

    // CONTAINER APPEARANCE

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

    private void addPanels() {
        add(createBetPanel());
        add(createActionPanel());
        add(createOptionsPanel());
    }

    // CONTENT CREATION

    private JPanel createOptionsPanel() {
        // Creates the container panel
        JPanel container = new JPanel();
        container.setLayout(new GridBagLayout());
        container.setOpaque(false);

        // Creates and customizes the quit button
        quitButton = new JButton("QUIT GAME");
        Dimension buttonSize = new Dimension(160, 48);
        quitButton.setMaximumSize(buttonSize);
        quitButton.setPreferredSize(buttonSize);
        Font customizedFont = Utils.getBoldFont(quitButton.getFont(), Configs.BUTTON_FONT_SIZE);
        quitButton.setFont(customizedFont);
        quitButton.setForeground(Color.red);

        quitButton.setActionCommand(ClientActionType.QUIT_GAME.toString());

        container.add(quitButton);

        return container;
    }

    private JPanel createActionPanel() {
        // Creates the container panel
        JPanel container = new JPanel();
        container.setLayout(new GridBagLayout());
        container.setBorder(BorderFactory.createMatteBorder(0, 8, 0, 8, Configs.BOTTOM_BAR_TOP_BORDER_COLOR));
        container.setOpaque(false);

        // Creates the buttons container panel
        JPanel buttonsContainer = new JPanel();
        buttonsContainer.setLayout(new BoxLayout(buttonsContainer, BoxLayout.X_AXIS));
        buttonsContainer.setOpaque(false);

        // Creates the action buttons
        hitButton = createActionButton("HIT");
        hitButton.setActionCommand(ClientActionType.HIT.toString());
        standButton = createActionButton("STAND");
        standButton.setActionCommand(ClientActionType.STAND.toString());
        doubleButton = createActionButton("DOUBLE");
        doubleButton.setActionCommand(ClientActionType.DOUBLE.toString());
        resetBetButton = createActionButton("RESET\nBET");
        resetBetButton.setActionCommand(ClientActionType.RESET_BET.toString());

        final int buttonMargin = 16;
        buttonsContainer.add(hitButton);
        buttonsContainer.add(Box.createHorizontalStrut(buttonMargin));
        buttonsContainer.add(standButton);
        buttonsContainer.add(Box.createHorizontalStrut(buttonMargin));
        buttonsContainer.add(doubleButton);
        buttonsContainer.add(Box.createHorizontalStrut(buttonMargin));
        buttonsContainer.add(resetBetButton);

        container.add(buttonsContainer);

        return container;
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
        Font customizedFont = Utils.getBoldFont(instructionLabel.getFont(), Configs.BUTTON_FONT_SIZE);
        instructionLabel.setFont(customizedFont);
        instructionLabel.setForeground(Color.white);
        instructionPanel.add(instructionLabel);

        // Creates the chip panel
        JPanel chipPanel = new JPanel();
        chipPanel.setLayout(new BoxLayout(chipPanel, BoxLayout.X_AXIS));
        chipPanel.setOpaque(false);

        // Creates the chip buttons
        yellowChipButton = createIconButton("assets/yellow_chip_ic.png");
        redChipButton = createIconButton("assets/red_chip_ic.png");
        greenChipButton = createIconButton("assets/green_chip_ic.png");
        blueChipButton = createIconButton("assets/blue_chip_ic.png");

        yellowChipButton.setName("1");
        redChipButton.setName("4");
        greenChipButton.setName("10");
        blueChipButton.setName("20");

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

    private JButton createActionButton(String title) {
        JButton button = new JButton(title);
        Dimension buttonSize = new Dimension(100, 100);
        button.setMaximumSize(buttonSize);
        button.setPreferredSize(buttonSize);
        Font customizedFont = Utils.getBoldFont(button.getFont(), Configs.BUTTON_FONT_SIZE);
        button.setFont(customizedFont);
        button.setForeground(Configs.BOTTOM_BAR_TOP_BORDER_COLOR);
        return button;
    }

    private JButton createIconButton(String fileName) {
        JButton button = new JButton();
        Image image = Utils.loadImage(fileName);
        assert image != null;
        button.setIcon(new ImageIcon(image));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        return button;
    }

}