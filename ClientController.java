import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

class ClientController implements StateListener, ActionListener {

    private ClientView view;
    private RequestSender requestSender;
    private Integer activePlayerSlot;
    private Player activePlayer;
    private List<Player> playerList;
    private Stack<Integer> availableSlots;

    public ClientController(ClientView view, RequestSender requestSender) {
        this.view = view;
        this.requestSender = requestSender;
        this.activePlayer = null;
        this.playerList = null;
        this.activePlayerSlot = null;
        this.availableSlots = createSlots();

        view.setActionListener(this);
    }

    @Override
    public void onReceiveState(GameState state) {
        System.out.println("RECEIVED NEW STATE");
        System.out.println(state);

        updateNumberOfPlayersIfNeeded(state.getNumberOfPlayers());
//        updatePlayerViews(state.getPlayers());
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
            activePlayerSlot = availableSlots.pop();
            view.addNewPlayer("You", activePlayerSlot);
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

    private Stack<Integer> createSlots() {
        // Creates a stack with available slot numbers that will be consulted when positioning players
        Stack<Integer> availableSlots = new Stack<>();
        for (int i = Configs.MAX_NUMBER_OF_PLAYERS; i >= 1; i--) {
            availableSlots.add(i);
        }
        return availableSlots;
    }

    private void updatePlayerViews(List<Player> newPlayerList) {
        List<Player> playerDiff = playerList.stream()
                .filter(newPlayerList::contains)
                .collect(Collectors.toList());
        System.out.println(playerDiff);
    }

}