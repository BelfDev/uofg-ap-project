import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

class ClientView extends JFrame {

    private TopBarView topBar;
    private BJTableView mainContent;
    private BottomBarView bottomBar;

    private Map<Integer, PlayerView> playerViewMap;

    public ClientView() {
        setupWindow();
        setupPanels();
        setVisible(true);

        this.playerViewMap = new HashMap<>();
    }

    public Map<Integer, PlayerView> getPlayerViewMap() {
        return playerViewMap;
    }

    public DealerView getDealerView() {
        return mainContent.getDealerView();
    }

    public void setBalance(String balance) {
        topBar.setBalanceValue(balance);
    }

    public void setFeedback(String feedback) {
        topBar.setFeedback(feedback);
    }

    public void updateNumberOfPlayersLabel(String numberOfPlayers) {
        topBar.setPlayersValueLabelText(numberOfPlayers);
    }

    public PlayerView addNewPlayer(String name, int slot) {
        PlayerView playerView = mainContent.addPlayer(name, slot);
        if (playerView != null) {
            playerViewMap.put(slot, playerView);
        }
        return playerView;
    }

    public void removePlayer(int slot) {
        mainContent.removePlayer(slot);
        playerViewMap.remove(slot);
    }

    public void setActionListener(ActionListener listener) {
        JButton quitButton = bottomBar.getQuitButton();
        quitButton.setActionCommand(ClientActionType.QUIT_GAME.toString());
        quitButton.addActionListener(listener);

        setBetListeners(listener);

        JButton hitButton = bottomBar.getHitButton();
        hitButton.setActionCommand(ClientActionType.HIT.toString());
        hitButton.addActionListener(listener);
    }

    public void setBet(int value, int playerSlot) {
        String betValue = String.valueOf(value);
        PlayerView playerView = playerViewMap.get(playerSlot);
        playerView.setBetValue(betValue);
    }

    private void setupWindow() {
        // Sets the initial window size (golden ratio)
        this.setSize(Configs.INITIAL_WINDOW_WIDTH, Configs.INITIAL_WINDOW_HEIGHT);
        // Prevents the window from resizing
        this.setResizable(false);
        // Sets the window location to the center of the screen
        this.setLocationRelativeTo(null);
        // Prevent the window from closing unless the user confirms the exit
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    private void setupPanels() {
        this.topBar = new TopBarView();
        this.mainContent = new BJTableView();
        this.bottomBar = new BottomBarView();

        this.add(topBar, BorderLayout.NORTH);
        this.add(mainContent, BorderLayout.CENTER);
        this.add(bottomBar, BorderLayout.SOUTH);
    }

    private void setBetListeners(ActionListener listener) {
        bottomBar.getChipButtons().forEach(chip -> {
            chip.setActionCommand(ClientActionType.BET.toString());
            chip.addActionListener(listener);
        });
    }

}