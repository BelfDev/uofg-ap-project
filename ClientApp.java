import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * This class acts as the client application shell. It initiates views and
 * controllers, connects to the server, and implements how to send out
 * requests to the game server.
 */
class ClientApp implements RequestSender {

    // Guaranteed single application instance
    private static ClientApp instance;
    // Shared state notifier
    private StateBroadcaster broadcaster;
    // Object used as basis to send out requests
    private ObjectOutputStream output;

    /**
     * Constructs a client application shell that sets up views and controllers,
     * connects to the server, and implements the sending requests.
     */
    private ClientApp() {
        this.broadcaster = new StateBroadcaster();
        ClientView view = new ClientView();
        ClientController controller = new ClientController(view, this);
        broadcaster.addListener(controller);
        connectToServer();
    }

    /**
     * Returns a unique instance of the application shell
     *
     * @return a unique ClientApp instance
     */
    public static ClientApp launch() {
        if (instance == null) {
            instance = new ClientApp();
        }
        return instance;
    }

    @Override
    public void sendRequest(ClientRequest request) {
        // Sends out requests to the server
        try {
            output.writeObject(request);
            output.flush();
            // Resets ObjectOutputStream cache
            output.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void connectToServer() {
        try {
            // Connects to the server via a client socket
            Socket clientSocket = new Socket(Configs.SERVER_HOST, Configs.SERVER_PORT);
            // Outputs connection confirmation
            System.out.println("Connected to the server");
            // Initializes the object output stream to send out requests
            output = new ObjectOutputStream(clientSocket.getOutputStream());

            //TODO: Delete this writeThread afterwards.
//            Thread writeThread = new Thread(new Writer(clientSocket));
            // Creates worker to handle incoming server responses
            ResponseWorker responseWorker = new ResponseWorker(clientSocket, broadcaster);
            responseWorker.execute();
            // Sends out initial request notifying the server about a new player
            sendRequest(new ClientRequest.Builder(Command.JOIN, 1).build());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}