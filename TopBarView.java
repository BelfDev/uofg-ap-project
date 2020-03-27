import javax.swing.*;
import java.awt.*;

/**
 * This class encapsulates the content of the TopBarView.
 * It provides visual feedback on the player's current credit
 * balance, game play status, and number of players in the table.
 */
class TopBarView extends JPanel {

    private JLabel balanceValueLabel;
    private JLabel feedbackLabel;
    private JLabel playersValueLabel;

    public TopBarView() {
        // Sets the bottom bar height
        Dimension size = new Dimension(0, Configs.TOP_BAR_HEIGHT);
        this.setPreferredSize(size);
        // Sets the layout manager to a GridLayout
        GridLayout gridLayout = new GridLayout(0, 3);
        this.setLayout(gridLayout);
        // Sets the bottom bar top border
        this.setBorder(BorderFactory.createMatteBorder(0, 0, Configs.BAR_BORDER_SIZE, 0, Configs.TOP_BAR_BORDER_COLOR));
        // Sets top bar colors
        this.setBackground(Configs.TOP_BAR_COLOR);
        // Content panels
        addPanels();
    }

    // Getters

    public JLabel getBalanceValueLabel() {
        return balanceValueLabel;
    }

    public JLabel getFeedbackLabel() {
        return feedbackLabel;
    }

    public JLabel getPlayersValueLabel() {
        return playersValueLabel;
    }

    public void setPlayersValueLabelText(String value) {
        this.playersValueLabel.setText(value);
    }

    public void setBalanceValue(String value) {
        this.balanceValueLabel.setText(value);
    }

    public void setFeedback(String feedback) {
        this.feedbackLabel.setText(feedback);
    }

    // Content setup

    private void addPanels() {
        // Balance panel
        JPanel balanceContainer = new JPanel();
        BoxLayout balanceLayout = new BoxLayout(balanceContainer, BoxLayout.Y_AXIS);
        balanceContainer.setOpaque(false);
        balanceContainer.setLayout(balanceLayout);

        JLabel balanceLabel = createLabel("BALANCE");
        Font customizedFont = Utils.getBoldFont(balanceLabel.getFont(), Configs.TOP_BAR_STATIC_LABEL_FONT_SIZE);
        balanceLabel.setFont(customizedFont);
        balanceValueLabel = createLabel("0");

        balanceContainer.add(Box.createVerticalGlue());
        balanceContainer.add(balanceLabel);
        balanceContainer.add(Box.createVerticalGlue());
        balanceContainer.add(balanceValueLabel);
        balanceContainer.add(Box.createVerticalGlue());

        // Feedback panel
        JPanel feedbackContainer = new JPanel(new GridBagLayout());
        feedbackContainer.setBorder(BorderFactory.createMatteBorder(0, 8, 0, 8, Configs.TOP_BAR_BORDER_COLOR));
        feedbackContainer.setOpaque(false);

        feedbackLabel = createLabel("Welcome to the Black Jack game!");
        feedbackContainer.add(feedbackLabel);

        // Players panel
        JPanel playersPanel = new JPanel(new GridBagLayout());
        BoxLayout playersLayout = new BoxLayout(playersPanel, BoxLayout.Y_AXIS);
        playersPanel.setLayout(playersLayout);
        playersPanel.setOpaque(false);

        JLabel playersLabel = createLabel("NUMBER OF PLAYERS");
        playersLabel.setFont(customizedFont);
        playersValueLabel = createLabel("2");

        playersPanel.add(Box.createVerticalGlue());
        playersPanel.add(playersLabel);
        playersPanel.add(Box.createVerticalGlue());
        playersPanel.add(playersValueLabel);
        playersPanel.add(Box.createVerticalGlue());

        add(balanceContainer);
        add(feedbackContainer);
        add(playersPanel);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.white);
        label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        return label;
    }

}