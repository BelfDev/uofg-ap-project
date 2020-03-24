class ClientController implements StateListener{

    private ClientView view;
    private RequestSender requestSender;

    public ClientController(ClientView view, RequestSender requestSender) {
        this.view = view;
        this.requestSender = requestSender;

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
        String numberOfPlayersText = String.valueOf(state.getNumberOfPlayers());
        view.setNumberOfPlayers(numberOfPlayersText);
        System.out.println("Received a nice response!");
        System.out.println(numberOfPlayersText);
    }

}