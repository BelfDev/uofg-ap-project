import javax.swing.*;
import java.awt.*;

/**
 * This class represents the black jack dealer. It displays
 * the dealer's cards and score.
 */
public class DealerView extends JPanel {

    private static final int WIDTH = 600;

    private JPanel cardsContainer;
    private CardScoreView scoreView;
    private JPanel rightBorder;

    /**
     * Creates a DealerView that displays the Dealer's cards and score.
     */
    public DealerView() {
        // Customizes the root container
        this.setSize(WIDTH, 2 * Configs.CARD_SIZE.height);
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        this.setLayout(boxLayout);
        this.setOpaque(false);

        // Sets the content containers
        cardsContainer = createCardsContainer();
        JPanel scoreContainer = createScoreContainer();

        this.add(cardsContainer);
        this.add(scoreContainer);
    }

    // Getters

    public CardScoreView getScoreView() {
        return scoreView;
    }

    public void setScore(int value) {
        this.scoreView.setText(String.valueOf(value));
        this.scoreView.setVisible(value > 0);
        revalidate();
        repaint();
    }

    /**
     * Adds a CardView to the DealerView. The position is rearranged
     * automatically.
     *
     * @param card the CardView to be added.
     */
    public void addCard(CardView card) {
        cardsContainer.add(card);
        cardsContainer.add(Box.createRigidArea(new Dimension(8, 0)));
        cardsContainer.add(rightBorder);
        revalidate();
        repaint();
    }

    // Content setup

    private JPanel createCardsContainer() {
        // Creates a JPanel to hold the dealer cards
        JPanel container = new JPanel();
        BoxLayout boxLayout = new BoxLayout(container, BoxLayout.X_AXIS);
        container.setLayout(boxLayout);

        JPanel leftBorder = createBorderPanel();
        rightBorder = createBorderPanel();
        container.add(leftBorder);

        container.setOpaque(false);

        return container;
    }

    private JPanel createScoreContainer() {
        // Creates a JPanel to hold the ScoreView
        JPanel container = new JPanel(new GridBagLayout());
        container.setOpaque(false);
        container.setSize(600, Configs.CARD_SIZE.height);

        Dimension scoreDimension = new Dimension(40, 40);
        scoreView = new CardScoreView(scoreDimension);
        Font f = scoreView.getFont();
        scoreView.setFont(Utils.getBoldFont(f, 16));
        scoreView.setForeground(Color.white);
        scoreView.setBackground(Color.black);
        scoreView.setText("21");
        scoreView.setVisible(false);

        container.add(scoreView);

        return container;
    }

    private JPanel createBorderPanel() {
        // Creates a dummy JPanel to serve as a customized border
        // for the cards container. This is necessary due to the
        // null layout manager.
        JPanel borderPanel = new JPanel();

        borderPanel.setSize(Configs.CARD_SIZE);
        borderPanel.setPreferredSize(Configs.CARD_SIZE);
        borderPanel.setMaximumSize(Configs.CARD_SIZE);

        borderPanel.setOpaque(false);

        return borderPanel;
    }

}
