import java.io.IOException;
import java.net.Socket;

class ClientApp {

    private static ClientApp instance;
    private Socket clientSocket = null;
    private StateBroadcaster broadcaster;

    private ClientApp() {
        this.broadcaster = new StateBroadcaster();
        ClientView view = new ClientView();
        ClientController controller = new ClientController(view);
        broadcaster.addListener(controller);
        connectToServer();
    }

    public static ClientApp launch() {
        if (instance == null) {
            instance = new ClientApp();
        }
        return instance;
    }

    private void connectToServer() {
        try {
            clientSocket = new Socket(Configs.SERVER_HOST, Configs.SERVER_PORT);
            Thread writeThread = new Thread(new Writer(clientSocket));
            ResponseWorker responseWorker = new ResponseWorker(clientSocket, broadcaster);

            writeThread.start();
            responseWorker.execute();

            writeThread.join();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}