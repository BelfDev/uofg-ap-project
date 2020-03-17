import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class starts the game server by creating a new ServerSocket and
 * listening for multiple client connections. Once a client connects, it serves
 * the BlackJackService.
 */
class GameServer implements Runnable {

    // Indicates that the server is open for clients through the SERVER_PORT
    private boolean openToClients = true;

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
                BlackJackService blackJackService = new BlackJackService(clientSocket);
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

}