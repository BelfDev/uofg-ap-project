import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * This class starts a game server by creating a new ServerSocket and
 * listening for multiple client connections. Once a client connects
 * this class serves a BlackJackService.
 */
class GameServerThread extends Thread implements Observer {

    // Object that holds the shared game state
    private final GameState sharedState;
    // A collection of live black jack services
    private ArrayList<BlackJackService> services = new ArrayList<>();
    // Controls the number of clients
    private int numberOfClients = 0;

    public GameServerThread() {
        this.sharedState = new GameState();
    }

    @Override
    public void run() {
        // Creates a server socket with SERVER_PORT. The try-with-resources statement
        // automatically closes the socket when necessary in runtime
        try (ServerSocket serverSocket = new ServerSocket(Configs.SERVER_PORT)) {
            // Awaits indefinitely for a client connection
            while (numberOfClients <= Configs.MAX_NUMBER_OF_PLAYERS) {
                // Establishes a new client socket connection
                Socket clientSocket = serverSocket.accept();
                // Creates a new black jack service
                BlackJackService blackJackService = new BlackJackService(clientSocket, sharedState);
                // Registers this service as an observer
                blackJackService.addObserver(this);
                services.add(blackJackService);
                // Creates a new thread for the black jack service
                Thread blackJackServiceThread = new Thread(blackJackService);
                // Indicates in the console that a new client has connected to the server
                System.out.println("New client connected at " + blackJackServiceThread.getName());
                // Increases the number of clients counter
                numberOfClients++;
                // Starts the black jack service thread execution
                blackJackServiceThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void update(Observable o, Object arg) {
        // This method is automatically triggered when there are changes to observables
        BlackJackService origin = (BlackJackService) o;
        ServerResponse response = (ServerResponse) arg;
        // Transmits the generated response to all live services
        for (BlackJackService service : services) {
            if (service != null && service.isAlive()) {
                service.transmitMessage(response);
            }
        }
        // Tears down legacy services and connections
        if (!origin.isAlive()) {
            services.remove(origin);
            numberOfClients--;
        }
    }

}