import javax.swing.*;
import java.awt.*;

public class PlayerView extends JPanel {

    private static final int CARD_INSET = 20;
    private static final int SCORE_LABEL_SIZE = 24;
    private static final int SCORE_LABEL_LEFT_MARGIN = 8;
    private static final int BET_CONTAINER_TOP_MARGIN = 32;
    private static final int BET_CONTAINER_HEIGHT = 60;

    private String id;
    private int cardsCounter;
    private int slot;
    private JPanel cardsContainer;
    private JLabel scoreLabel;
    private JLabel nameLabel;

    public PlayerView(String playerId, int slot) {
        this.id = playerId;
        this.slot = slot;
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

    public String getId() {
        return id;
    }

    public int getSlot() {
        return slot;
    }

    public int getCardsCounter() {
        return cardsCounter;
    }

    public JPanel getCardsContainer() {
        return cardsContainer;
    }

    public JLabel getNameLabel() {
        return nameLabel;
    }

    public void setName(String name) {
        this.nameLabel.setText(name);
    }

    private JPanel createBetContainer() {
        JPanel container = new JPanel(new BorderLayout());
        container.setSize(this.getWidth(), BET_CONTAINER_HEIGHT);
        container.setLocation(0, Configs.CARD_SIZE.height + BET_CONTAINER_TOP_MARGIN);

        JLabel betLabel = new JLabel("BET");
        betLabel.setHorizontalAlignment(JLabel.CENTER);
        betLabel.setForeground(Color.white);

        JLabel betValueLabel = new JLabel("0");
        betValueLabel.setHorizontalAlignment(JLabel.CENTER);
        Font f = Utils.getBoldFont(betValueLabel.getFont(), 16);
        betValueLabel.setFont(f);
        betValueLabel.setForeground(Color.white);

        nameLabel = new JLabel("You");
        nameLabel.setHorizontalAlignment(JLabel.CENTER);
        nameLabel.setForeground(Color.orange);

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
//        scoreLabel.setText("2");
        scoreLabel.setLocation(cardsContainer.getWidth() - SCORE_LABEL_SIZE, 0);

//        addCard(new CardView("assets/card_2_c_ic"));
//        addCard(new CardView("assets/card_10_d_ic"));
//        addCard(new CardView("assets/card_a_h_ic"));

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
