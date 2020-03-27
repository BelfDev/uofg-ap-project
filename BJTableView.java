import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class BJTableView extends JPanel {

    private List<PlayerView> playerViews;
    private Image backgroundImage;

    private DealerView dealerView;

    public BJTableView() {
        this.playerViews = new ArrayList<>();
        this.backgroundImage = Utils.loadImage("assets/bj-table.png");
        this.setLayout(null);
        addDealer();
    }

    public PlayerView addPlayer(String name, int slot) {
        if (slot <= Configs.MAX_NUMBER_OF_PLAYERS) {
            PlayerView playerView = new PlayerView(slot);
            if (name != null) {
                playerView.setName(name);
            }
            playerViews.add(playerView);
            Point playerLocation = Configs.PLAYER_SLOT_LOCATIONS.get(slot);
            playerView.setLocation(playerLocation);
            add(playerView);
            refreshInterface();
            return playerView;
        }
        return null;
    }

    public void removePlayer(int slot) {
        PlayerView playerView = playerViews.stream()
                .filter(p -> p.getSlot() == (slot))
                .findFirst()
                .orElse(null);
        if (playerView != null) {
            remove(playerView);
            playerViews.remove(playerView);
        }
        refreshInterface();
    }

    private void addDealer() {
        dealerView = new DealerView();
        dealerView.setLocation(Configs.DEALER_SLOT_LOCATION);
        add(dealerView);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the background image.
        g.drawImage(backgroundImage, -2, -2, getWidth() + 4, getHeight() + 4, this);
    }

    private void refreshInterface() {
        revalidate();
        repaint();
    }

}