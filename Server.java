import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

class Server {

    public static void main(String[] args) {
        // Server socket
        ServerSocket serverSocket = null;

        try {
            // Initiates the server socket with SEVER_SOCKET_PORT
            serverSocket = new ServerSocket(Configs.SERVER_PORT);

            while (true) {
                // Awaits until client application connects to the server
                final Socket clientSocket = serverSocket.accept();
                // Delegates the client connection to the ServerHandler
                new ClientHandler(clientSocket);
            }

        } catch (IOException e) {
            // Handles IO exception
            e.printStackTrace();
        } finally {

            try {
                // Closes the connection
                serverSocket.close();
            } catch (Exception e) {
                // Handles any exception
                e.printStackTrace();
            }
        }

    }

}