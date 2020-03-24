import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ClientController implements StateListener{

    private ClientView view;
    private RequestSender sender;

    public ClientController(ClientView view, RequestSender sender) {
        this.view = view;
        this.sender= sender;

        view.setHitButtonActionListener(e -> sendRequest());
    }

    private void sendRequest() {
        ClientRequest request = new ClientRequest.Builder(Command.JOIN, 1)
                .withData("key", "value")
                .build();
        sender.sendRequest(request);
    }

    @Override
    public void onReceiveState(GameState state) {
        String numberOfPlayersText = String.valueOf(state.getNumberOfPlayers());
        view.setNumberOfPlayers(numberOfPlayersText);
        System.out.println("Received a nice response!");
        System.out.println(numberOfPlayersText);
    }

}