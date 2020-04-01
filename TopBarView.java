import javax.swing.*;
import java.awt.*;

/**
 * This class encapsulates the contents of the TopBarView.
 * It provides a visual feedback on the player's current credit
 * balance, game play status, and number of players in the table.
 */
class TopBarView extends JPanel {

    private static final String WELCOME_MESSAGE = "Welcome to the Black Jack game!";
    private static final String BALANCE_TEXT = "BALANCE";
    private static final String NUMBER_OF_PLAYERS_TEXT = "BALANCE";

    private JLabel balanceValueLabel;
    private JLabel feedbackLabel;
    private JLabel playersValueLabel;

    /**
     * Constructs a TopBarView that provides text feedback about the
     * ongoing game dynamics.
     */
    public TopBarView() {
        // Sets the bottom bar height
        Dimension size = new Dimension(0, Configs.TOP_BAR_HEIGHT);
        this.setPreferredSize(size);
        // Sets the layout manager to GridLayout
        GridLayout gridLayout = new GridLayout(0, 3);
        this.setLayout(gridLayout);
        // Sets the bottom bar top border
        this.setBorder(BorderFactory.createMatteBorder(0, 0, Configs.BAR_BORDER_SIZE, 0, Configs.TOP_BAR_BORDER_COLOR));
        // Sets top bar colors
        this.setBackground(Configs.TOP_BAR_COLOR);
        // Content panels
        addPanels();
    }

    // SETTERS

    public void setPlayersValueLabelText(String value) {
        this.playersValueLabel.setText(value);
    }

    public void setBalanceValue(String value) {
        this.balanceValueLabel.setText(value);
    }

    public void setFeedback(String feedback) {
        this.feedbackLabel.setText(feedback);
    }

    // CONTENT SETUP

    private void addPanels() {
        // Balance panel
        JPanel balanceContainer = new JPanel();
        BoxLayout balanceLayout = new BoxLayout(balanceContainer, BoxLayout.Y_AXIS);
        balanceContainer.setOpaque(false);
        balanceContainer.setLayout(balanceLayout);

        JLabel balanceLabel = createLabel(BALANCE_TEXT);
        Font customizedFont = Utils.getBoldFont(balanceLabel.getFont(), Configs.TOP_BAR_STATIC_LABEL_FONT_SIZE);
        balanceLabel.setFont(customizedFont);
        balanceValueLabel = createLabel("0");
        addContentToPanel(balanceContainer, balanceLabel, balanceValueLabel);

        // Feedback panel
        JPanel feedbackContainer = new JPanel(new GridBagLayout());
        feedbackContainer.setBorder(BorderFactory.createMatteBorder(0, 8, 0, 8, Configs.TOP_BAR_BORDER_COLOR));
        feedbackContainer.setOpaque(false);

        feedbackLabel = createLabel(WELCOME_MESSAGE);
        feedbackContainer.add(feedbackLabel);

        // Players panel
        JPanel playersPanel = new JPanel(new GridBagLayout());
        BoxLayout playersLayout = new BoxLayout(playersPanel, BoxLayout.Y_AXIS);
        playersPanel.setLayout(playersLayout);
        playersPanel.setOpaque(false);

        JLabel playersLabel = createLabel(NUMBER_OF_PLAYERS_TEXT);
        playersLabel.setFont(customizedFont);
        playersValueLabel = createLabel("0");

        addContentToPanel(playersPanel, playersLabel, playersValueLabel);

        // Adds content
        add(balanceContainer);
        add(feedbackContainer);
        add(playersPanel);
    }

    // CONVENIENCE METHODS

    private void addContentToPanel(JPanel panel, JLabel topLabel, JLabel bottomLabel) {
        panel.add(Box.createVerticalGlue());
        panel.add(topLabel);
        panel.add(Box.createVerticalGlue());
        panel.add(bottomLabel);
        panel.add(Box.createVerticalGlue());
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.white);
        label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        return label;
    }

}
