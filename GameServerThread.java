import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * This class starts the game server by creating a new ServerSocket and
 * listening for multiple client connections. Once a client connects, it serves
 * the BlackJackService.
 */
class GameServerThread extends Thread implements Observer {

    // Object that holds the shared game state
    private final GameState sharedState;
    // Indicates that the server is open for clients through the SERVER_PORT
    private boolean openToClients;

    public GameServerThread() {
        this.sharedState = new GameState();
        this.openToClients = true;
    }

    private ArrayList<BlackJackService> services = new ArrayList<>();

    @Override
    public void run() {
        // Creates a server socket with SERVER_PORT. The try-with-resources statement
        // automatically closes the socket when necessary in runtime
        try (ServerSocket serverSocket = new ServerSocket(Configs.SERVER_PORT)) {
            // Awaits indefinitely for a client connection
            while (openToClients) {
                // Establishes a new client socket connection
                Socket clientSocket = serverSocket.accept();
                // Creates a new black jack service
                BlackJackService blackJackService = new BlackJackService(clientSocket, sharedState);
                blackJackService.addObserver(this);
                services.add(blackJackService);
                // Creates a new black jack service Thread
                Thread blackJackServiceThread = new Thread(blackJackService);

                System.out.println("New client connected at " + blackJackServiceThread.getName());
                // Starts the black jack service thread thread execution
                blackJackServiceThread.start();
            }
        } catch (IOException e) {
            // In case the SERVER_PORT cannot be read, print the stack trace
            e.printStackTrace();
        }

    }

    @Override
	public void update(Observable o, Object arg) {
        ServerResponse r = (ServerResponse) arg;
        for (BlackJackService service : services) {
            if (service != null) {
                service.transmitMessage(r);
            }
        }
    }

}