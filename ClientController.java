import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.List;

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
        updateNumberOfPlayersIfNeeded(state.getNumberOfPlayers());
        playerList = state.getPlayers();
        initActivePlayerIfNeeded();
    }

    private void updateNumberOfPlayersIfNeeded(int numberOfPlayers) {
        if (playerList == null || playerList.size() != numberOfPlayers) {
            String numberOfPlayersText = String.valueOf(numberOfPlayers);
            view.updateNumberOfPlayersLabel(numberOfPlayersText);
            System.out.println("Number of players changed to " + numberOfPlayers);
        }
    }

    private void initActivePlayerIfNeeded() {
        if (activePlayer == null) {
            activePlayer = playerList.get(playerList.size() - 1);
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

}