import javax.swing.*;
import java.awt.*;

class BottomBarView extends JPanel {

    private static final String INSTRUCTION_LABEL_TEXT = "Click on the chip to bet";

    private JButton yellowChipButton;
    private JButton redChipButton;
    private JButton greenChipButton;
    private JButton blueChipButton;

    private JButton dealButton;
    private JButton standButton;
    private JButton doubleButton;
    private JButton splitButton;

    private JButton quitButton;

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

    // Getters

    public JButton getYellowChipButton() {
        return yellowChipButton;
    }

    public JButton getRedChipButton() {
        return redChipButton;
    }

    public JButton getGreenChipButton() {
        return greenChipButton;
    }

    public JButton getBlueChipButton() {
        return blueChipButton;
    }

    public JButton getDealButton() {
        return dealButton;
    }

    public JButton getStandButton() {
        return standButton;
    }

    public JButton getDoubleButton() {
        return doubleButton;
    }

    public JButton getSplitButton() {
        return splitButton;
    }

    public JButton getQuitButton() {
        return quitButton;
    }

    // Root container setup

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

    // Content panel creation

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
        dealButton = createActionButton("DEAL");
        standButton = createActionButton("STAND");
        doubleButton = createActionButton("DOUBLE");
        splitButton = createActionButton("SPLIT");

        final int buttonMargin = 16;
        buttonsContainer.add(dealButton);
        buttonsContainer.add(Box.createHorizontalStrut(buttonMargin));
        buttonsContainer.add(standButton);
        buttonsContainer.add(Box.createHorizontalStrut(buttonMargin));
        buttonsContainer.add(doubleButton);
        buttonsContainer.add(Box.createHorizontalStrut(buttonMargin));
        buttonsContainer.add(splitButton);

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