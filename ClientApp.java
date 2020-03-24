import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

class ClientApp implements RequestSender {

    private static ClientApp instance;
    private Socket clientSocket = null;
    private StateBroadcaster broadcaster;
    private ObjectOutputStream output;

    private ClientApp() {
        this.broadcaster = new StateBroadcaster();
        ClientView view = new ClientView();
        ClientController controller = new ClientController(view, this);
        broadcaster.addListener(controller);
        connectToServer();
    }

    public static ClientApp launch() {
        if (instance == null) {
            instance = new ClientApp();
        }
        return instance;
    }

    @Override
    public void sendRequest(ClientRequest request) {
        try {
            output.writeObject(request);
            output.flush();
            output.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void connectToServer() {
        try {
            clientSocket = new Socket(Configs.SERVER_HOST, Configs.SERVER_PORT);
            System.out.println(">>> Connected");

            output = new ObjectOutputStream(clientSocket.getOutputStream());

            //TODO: Delete this writeThread afterwards.
//            Thread writeThread = new Thread(new Writer(clientSocket));
            ResponseWorker responseWorker = new ResponseWorker(clientSocket, broadcaster);
            responseWorker.execute();
            
            sendRequest(new ClientRequest.Builder(Command.JOIN, 1).build());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}