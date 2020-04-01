import javax.swing.*;
import java.awt.*;

/**
 * This class holds all content related to a single player.
 * It displays the cards, player name, slot, bet value, and hand score.
 */
public class PlayerView extends JPanel implements CardReceiver {

    private static final int CARD_INSET = 20;
    private static final int SCORE_LABEL_SIZE = 24;
    private static final int SCORE_LABEL_LEFT_MARGIN = 8;
    private static final int BET_CONTAINER_TOP_MARGIN = 32;
    private static final int BET_CONTAINER_HEIGHT = 60;

    private int cardsCounter;
    private int slot;
    private JPanel cardsContainer;
    private JLabel scoreLabel;
    private JLabel nameLabel;
    private JLabel betValueLabel;

    private boolean isRedLineVisible;

    /**
     * Constructs a view that represents a player.
     *
     * @param slot the slot where the player should be placed.
     */
    public PlayerView(int slot) {
        this.slot = slot;
        this.isRedLineVisible = false;
        int width = Configs.CARD_SIZE.width + CARD_INSET * 4 + SCORE_LABEL_SIZE + SCORE_LABEL_LEFT_MARGIN;
        // Configures the layout
        this.setSize(width, Configs.CARD_SIZE.height + BET_CONTAINER_HEIGHT + BET_CONTAINER_TOP_MARGIN);
        this.setLayout(null);

        this.cardsCounter = 0;

        // Creates the content containers
        JPanel cardsContainer = createCardsContainer();
        JPanel betContainer = createBetContainer();
        // Adds the content
        add(cardsContainer);
        add(betContainer);

        setOpaque(false);
    }

    // GETTERS

    public int getSlot() {
        return slot;
    }

    public void setName(String name) {
        this.nameLabel.setText(name);
    }

    // SETTERS

    public void setBetValue(String value) {
        this.betValueLabel.setText(value);
    }

    // CARD RECEIVER IMPLEMENTATION

    @Override
    public int getNumberOfCards() {
        return cardsCounter;
    }

    @Override
    public void addCard(CardView card) {
        int width = cardsContainer.getWidth();
        int height = cardsContainer.getHeight();
        // Adapt cardsContainer and PlayerView sizes
        int newContainerWidth = cardsCounter > 0 ? width + CARD_INSET : width;
        cardsContainer.setSize(newContainerWidth, height);
        if (this.getWidth() < newContainerWidth) {
            this.setSize((newContainerWidth), this.getHeight());
        }
        scoreLabel.setLocation(cardsContainer.getWidth() - SCORE_LABEL_SIZE, 0);
        // Position cards
        card.setLocation(cardsCounter * CARD_INSET, 0);
        cardsContainer.add(card);
        // Bring last card to front
        cardsContainer.setComponentZOrder(card, 0);
        cardsCounter++;
        revalidate();
        repaint();
    }

    public void toggleRedLine() {
        if (isRedLineVisible) {
            cardsContainer.remove(0);
            isRedLineVisible = false;
        } else {
            JPanel redPanel = new JPanel();
            int width = (CARD_INSET * (cardsCounter - 1)) + Configs.CARD_SIZE.width;
            redPanel.setBounds(0, cardsContainer.getHeight() / 2, width, 4);
            redPanel.setBackground(Color.red);
            cardsContainer.add(redPanel);
            cardsContainer.setComponentZOrder(redPanel, 0);
            revalidate();
            repaint();
        }
    }

    @Override
    public void removeCards() {
        this.cardsCounter = 0;
        this.cardsContainer.removeAll();
        this.cardsContainer = createCardsContainer();
        this.add(cardsContainer);
        revalidate();
        repaint();
    }

    @Override
    public void setScore(int value) {
        this.scoreLabel.setText(String.valueOf(value));
        this.scoreLabel.setVisible(value > 0);
        revalidate();
        repaint();
    }

    // CONTENT SETUP

    private JPanel createBetContainer() {
        // Container appearance
        JPanel container = new JPanel(new BorderLayout());
        container.setSize(this.getWidth(), BET_CONTAINER_HEIGHT);
        container.setLocation(0, Configs.CARD_SIZE.height + BET_CONTAINER_TOP_MARGIN);

        // Bet label
        JLabel betLabel = new JLabel("BET");
        betLabel.setHorizontalAlignment(JLabel.CENTER);
        betLabel.setForeground(Color.white);

        betValueLabel = new JLabel("0");
        betValueLabel.setHorizontalAlignment(JLabel.CENTER);
        Font f = Utils.getBoldFont(betValueLabel.getFont(), 16);
        betValueLabel.setFont(f);
        betValueLabel.setForeground(Color.white);

        // Name label
        nameLabel = new JLabel();
        nameLabel.setHorizontalAlignment(JLabel.CENTER);
        nameLabel.setForeground(Color.orange);

        // Adds content
        container.add(betLabel, BorderLayout.NORTH);
        container.add(betValueLabel, BorderLayout.CENTER);
        container.add(nameLabel, BorderLayout.SOUTH);

        container.setOpaque(false);

        return container;
    }

    private JPanel createCardsContainer() {
        cardsContainer = new JPanel();
        cardsContainer.setLayout(null);
        cardsContainer.setSize(Configs.CARD_SIZE.width + SCORE_LABEL_SIZE + SCORE_LABEL_LEFT_MARGIN, Configs.CARD_SIZE.height);
        cardsContainer.setOpaque(false);

        scoreLabel = new CardScoreView(new Dimension(SCORE_LABEL_SIZE, SCORE_LABEL_SIZE));
        scoreLabel.setLocation(cardsContainer.getWidth() - SCORE_LABEL_SIZE, 0);
        scoreLabel.setVisible(false);

        cardsContainer.add(scoreLabel);

        return cardsContainer;
    }

}
