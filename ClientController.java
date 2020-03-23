class ClientController implements StateListener{

    private ClientView view;

    public ClientController(ClientView view) {
        this.view = view;
    }

    @Override
    public void onReceiveState(GameState state) {
        String numberOfPlayersText = String.valueOf(state.getNumberOfPlayers());
        view.setNumberOfPlayers(numberOfPlayersText);
        System.out.println("Received a nice response!");
        System.out.println(numberOfPlayersText);
    }


}