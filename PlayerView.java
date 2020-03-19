import javax.swing.*;
import java.awt.*;

public class PlayerView extends JPanel {

    private static final int CARD_INSET = 20;
    private static final int SCORE_LABEL_SIZE = 24;
    private static final int SCORE_LABEL_LEFT_MARGIN = 8;
    private static final int BET_CONTAINER_TOP_MARGIN = 32;
    private static final int BET_CONTAINER_HEIGHT = 40;

    private int cardsCounter;
    private JPanel cardsContainer;
    private JLabel scoreLabel;


    public PlayerView() {
        this.setLayout(null);
        int width = Configs.CARD_SIZE.width + CARD_INSET * 4 + SCORE_LABEL_SIZE + SCORE_LABEL_LEFT_MARGIN;
        this.setSize(width, Configs.CARD_SIZE.height + BET_CONTAINER_HEIGHT + BET_CONTAINER_TOP_MARGIN);

        this.cardsCounter = 0;

        JPanel cardsContainer = createCardsContainer();
        JPanel betContainer = createBetContainer();

        add(cardsContainer);
        add(betContainer);

        setOpaque(false);
    }

    private JPanel createBetContainer() {
        JPanel container = new JPanel(new BorderLayout());
        container.setSize(this.getWidth(), BET_CONTAINER_HEIGHT);
        container.setLocation(0, Configs.CARD_SIZE.height + BET_CONTAINER_TOP_MARGIN);

        JLabel betLabel = new JLabel("BET");
        betLabel.setHorizontalAlignment(JLabel.CENTER);
        betLabel.setForeground(Color.white);

        JLabel betValueLabel = new JLabel("100");
        betValueLabel.setHorizontalAlignment(JLabel.CENTER);
        Font f = Utils.getBoldFont(betValueLabel.getFont(), 16);
        betValueLabel.setFont(f);
        betValueLabel.setForeground(Color.white);

        container.add(betLabel, BorderLayout.NORTH);
        container.add(betValueLabel, BorderLayout.CENTER);

        container.setOpaque(false);

        return container;
    }

    private JPanel createCardsContainer() {
        cardsContainer = new JPanel();
        cardsContainer.setLayout(null);
        cardsContainer.setSize(Configs.CARD_SIZE.width + SCORE_LABEL_SIZE + SCORE_LABEL_LEFT_MARGIN, Configs.CARD_SIZE.height);
        cardsContainer.setOpaque(false);

        scoreLabel = new CardScoreView(new Dimension(SCORE_LABEL_SIZE, SCORE_LABEL_SIZE));
        scoreLabel.setText("2");
        scoreLabel.setLocation(cardsContainer.getWidth() - SCORE_LABEL_SIZE, 0);

        addCard(new CardView("assets/card_2_c_ic"));
        addCard(new CardView("assets/card_2_c_ic"));
        addCard(new CardView("assets/card_2_c_ic"));
        addCard(new CardView("assets/card_2_c_ic"));
        addCard(new CardView("assets/card_2_c_ic"));
        addCard(new CardView("assets/card_2_c_ic"));

        cardsContainer.add(scoreLabel);

        return cardsContainer;
    }

    private void addCard(CardView card) {
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
    }

}
