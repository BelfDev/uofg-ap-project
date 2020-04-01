import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class encapsulates the application main window and content.
 * It provides a single source that exposes methods to manipulate
 * the TopBar, BJTable, and BottomBar views.
 */
class ClientView extends JFrame {

    private TopBarView topBar;
    private BJTableView mainContent;
    private BottomBarView bottomBar;

    // Map to efficiently manipulate PlayerViews
    private Map<Integer, PlayerView> playerViewMap;

    /**
     * Constructs the client view with a TopBarView, BJTableView, and BottomBarView.
     */
    public ClientView() {
        setupWindow();
        setupPanels();
        setVisible(true);

        this.playerViewMap = new HashMap<>();
    }

    // GETTERS

    public Map<Integer, PlayerView> getPlayerViewMap() {
        return playerViewMap;
    }

    public DealerView getDealerView() {
        return mainContent.getDealerView();
    }

    // SETTERS

    public void setBalance(String balance) {
        topBar.setBalanceValue(balance);
    }

    public void setFeedback(String feedback) {
        topBar.setFeedback(feedback);
    }

    // BUTTONS

    /**
     * Deactivates the double button.
     *
     * @param disable boolean flag indicating whether to disable the button.
     */
    public void disableDoubleButton(boolean disable) {
        bottomBar.getDoubleButton().setEnabled(!disable);
    }

    /**
     * Deactivates the reset bet button.
     *
     * @param disable boolean flag indicating whether to disable the button.
     */
    public void disableResetBetButton(boolean disable) {
        bottomBar.getResetBetButton().setEnabled(!disable);
    }

    /**
     * Deactivates the hit and stand buttons.
     *
     * @param disable boolean flag indicating whether to disable the buttons.
     */
    public void disableHitAndStand(boolean disable) {
        bottomBar.getHitButton().setEnabled(!disable);
        bottomBar.getStandButton().setEnabled(!disable);
    }

    /**
     * Adds action listeners to the view buttons.
     *
     * @param listener a listener to handle actions coming from all view buttons.
     *                 Namely: quit, chips, hit, stand, double, and reset bet buttons.
     */
    public void setActionListener(ActionListener listener) {
        List<JButton> buttons = Arrays.asList(
                bottomBar.getQuitButton(),
                bottomBar.getHitButton(),
                bottomBar.getStandButton(),
                bottomBar.getDoubleButton(),
                bottomBar.getResetBetButton());

        buttons.forEach(b -> b.addActionListener(listener));
        setBetListeners(listener);
    }

    // PLAYERS

    /**
     * Updates the message displayed on the "Number of Players" panel.
     *
     * @param numberOfPlayers number of players currently in the game room.
     */
    public void updateNumberOfPlayersLabel(String numberOfPlayers) {
        topBar.setPlayersValueLabelText(numberOfPlayers);
    }

    /**
     * Creates a new PlayerView and adds to the table.
     *
     * @param name the player's name.
     * @param slot the slot number
     * @return the added PlayerView.
     */
    public PlayerView addNewPlayer(String name, int slot) {
        PlayerView playerView = mainContent.addPlayer(name, slot);
        if (playerView != null) {
            playerViewMap.put(slot, playerView);
        }
        return playerView;
    }

    /**
     * Removes the PlayerView which belongs to the given slot.
     *
     * @param slot the slot to be emptied.
     */
    public void removePlayer(int slot) {
        mainContent.removePlayer(slot);
        playerViewMap.remove(slot);
    }

    // CONVENIENCE METHODS

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
        // Adds the listener to all chip buttons
        bottomBar.getChipButtons().forEach(chip -> {
            chip.setActionCommand(ClientActionType.BET.toString());
            chip.addActionListener(listener);
        });
    }

}