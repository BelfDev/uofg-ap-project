import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server application entry point. Allows multiple client connections and serves
 * the BlackJack game service.
 * 
 * Author: Pedro Henrique Belfort Fernandes 
 * Matric: 2456943B
 */
class Server {

    public static void main(String[] args) {
        // Indicates that the server is open to clients through the SERVER_PORT
        boolean openToClients = true;
        // Creates a server socket with SERVER_PORT
        // The try-with-resources statement automatically closes the socket when
        // necessary in runtime
        try (ServerSocket serverSocket = new ServerSocket(Configs.SERVER_PORT)) {
            while (openToClients) {
                // Represents a new client connection via Socket
                Socket clientSocket = serverSocket.accept();
                // Creates a new GameServerThread passing in the clientSocket connection
                new GameServerThread(clientSocket);
            }
        } catch (IOException e) {
            // In case the SERVER_PORT cannot be read, print the stack trace
            e.printStackTrace();
        }
    }

}