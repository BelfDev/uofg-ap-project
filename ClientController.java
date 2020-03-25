import java.util.List;

class ClientController implements StateListener {

    private ClientView view;
    private RequestSender requestSender;
    private Player activePlayer;
    private List<Player> playerList;

    public ClientController(ClientView view, RequestSender requestSender) {
        this.view = view;
        this.requestSender = requestSender;
        this.activePlayer = null;
        this.playerList = null;

        view.setHitButtonActionListener(e -> sendRequest());
    }

    private void sendRequest() {
        ClientRequest request = new ClientRequest.Builder(Command.JOIN, 1)
                .withData("key", "value")
                .build();
        requestSender.sendRequest(request);
    }

    @Override
    public void onReceiveState(GameState state) {
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

}