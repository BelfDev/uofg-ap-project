import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class ClientController implements StateListener, ActionListener {

    private ClientView view;
    private RequestSender requestSender;
    private Player activePlayer;
    private List<Player> playerList;

    public ClientController(ClientView view, RequestSender requestSender) {
        this.view = view;
        this.requestSender = requestSender;
        this.activePlayer = null;
        this.playerList = null;
        view.setActionListener(this);
    }

    @Override
    public void onReceiveState(GameState state) {
        System.out.println("RECEIVED NEW STATE");
        System.out.println(state);
        // Stores previous player state
        List<Player> previousPlayerState = playerList;
        // Updates the player state
        playerList = state.getPlayers();
        // Initialized this client's player view
        initActivePlayerIfNeeded();
        // Updates the number of players
        updateNumberOfPlayersIfNeeded(previousPlayerState, state.getNumberOfPlayers());
        // Remove players who quit the game
        removeQuitPlayers(previousPlayerState);
        // Updates the players views
        updatePlayerViews();
    }

    private void updateNumberOfPlayersIfNeeded(List<Player> previousPlayerList, int currentNumberOfPlayers) {
        if (previousPlayerList == null || previousPlayerList.size() != currentNumberOfPlayers) {
            String numberOfPlayersText = String.valueOf(currentNumberOfPlayers);
            view.updateNumberOfPlayersLabel(numberOfPlayersText);
            System.out.println("Number of players changed to " + currentNumberOfPlayers);
        }
    }

    private void initActivePlayerIfNeeded() {
        if (activePlayer == null) {
            activePlayer = playerList.get(playerList.size() - 1);
            view.addNewPlayer("You", activePlayer.getSlot());
            System.out.println("Current player ID: " + activePlayer.getId());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ClientActionType action = ClientActionType.valueOf(e.getActionCommand());
        System.out.println(action.toString());
        switch (action) {
            case QUIT_GAME:
                quitGame();
                break;
            default:
                break;
        }
    }

    private void quitGame() {
        // Confirms whether the user want to quit the game
        int input = JOptionPane.showConfirmDialog(view, "Do you want to quit the game?");
        if (input == 0) {
            // Sends out a quit request to the server
            requestSender.sendRequest(
                    new ClientRequest.Builder(Command.QUIT, activePlayer.getId())
                            .build());
            // Closes the application
            view.dispatchEvent(new WindowEvent(view, WindowEvent.WINDOW_CLOSING));
        }
    }

    private void updatePlayerViews() {
        for (Player player : playerList) {
            boolean hasPlayerView = view.getPlayerViewMap().containsKey(player.getSlot());
            if (hasPlayerView) {
                // TODO: Update some player view
            } else if (!player.getId().equals(activePlayer.getId())) {
                // Add a new player view
                view.addNewPlayer("Someone", player.getSlot());
            }
        }

    }

    private void removeQuitPlayers(List<Player> previousPlayers) {
        // Remove players that quit
        if (previousPlayers != null && previousPlayers.size() != playerList.size()) {
            List<Player> oldPlayers = getPlayerDiff(previousPlayers);
            for (Player player : oldPlayers) {
                view.removePlayer(player.getSlot());
            }
        }
    }

    private List<Player> getPlayerDiff(List<Player> previousPlayers) {
        Predicate<Player> notSameId = p1 -> playerList.stream().noneMatch(p2 -> p2.getId().equals(p1.getId()));
        return previousPlayers.stream()
                .filter(notSameId)
                .collect(Collectors.toCollection(ArrayList::new));
    }

}