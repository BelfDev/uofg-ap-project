import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class encapsulates all the content displayed on the
 * black jack game table. Namely: all the player views.
 */
class BJTableView extends JPanel {

    private List<PlayerView> playerViews;
    private Image backgroundImage;

    private DealerView dealerView;

    /**
     * Constructs the black jack table content.
     */
    public BJTableView() {
        this.playerViews = new ArrayList<>();
        this.backgroundImage = Utils.loadImage("assets/bj-table.png");
        this.setLayout(null);
        addDealer();
    }

    // GETTERS

    public DealerView getDealerView() {
        return dealerView;
    }

    // BACKGROUND APPEARANCE

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draws the background image.
        g.drawImage(backgroundImage, -2, -2, getWidth() + 4, getHeight() + 4, this);
    }

    // PLAYERS

    /**
     * Adds a new PlayerView to the black jack table.
     *
     * @param name the text to be displayed underneath the player cards.
     * @param slot the slot number.
     * @return the newly-added PlayerView.
     */
    public PlayerView addPlayer(String name, int slot) {
        // Checks if all available slots have been claimed
        if (slot <= Configs.MAX_NUMBER_OF_PLAYERS) {
            // Creates a new player view
            PlayerView playerView = new PlayerView(slot);
            if (name != null) {
                playerView.setName(name);
            }
            playerViews.add(playerView);
            // Sets the player location
            Point playerLocation = Configs.PLAYER_SLOT_LOCATIONS.get(slot);
            playerView.setLocation(playerLocation);
            // Adds the player view
            add(playerView);
            refreshInterface();
            return playerView;
        }
        return null;
    }

    /**
     * Removes a PlayerView from the game table.
     *
     * @param slot the slot that should be emptied.
     */
    public void removePlayer(int slot) {
        // Fetches the PlayerView placed on the given slot
        PlayerView playerView = playerViews.stream()
                .filter(p -> p.getSlot() == (slot))
                .findFirst()
                .orElse(null);
        // Removes the PlayerView
        if (playerView != null) {
            remove(playerView);
            playerViews.remove(playerView);
        }
        refreshInterface();
    }

    // CONVENIENCE METHODS

    private void addDealer() {
        // Adds a new dealer view
        dealerView = new DealerView();
        dealerView.setLocation(Configs.DEALER_SLOT_LOCATION);
        add(dealerView);
    }

    private void refreshInterface() {
        // Notifies Swing that the interface should be redrawn immediately
        revalidate();
        repaint();
    }

}