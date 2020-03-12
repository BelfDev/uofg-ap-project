import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class Server {

    public static void main(String[] args) {

        try {
            // Creates the server socket with SEVER_SOCKET_PORT
            final ServerSocket serverSocket = new ServerSocket(Configs.SERVER_PORT);
            // Awaits until client application connects to the server
            final Socket clientSocket = serverSocket.accept();
            // Delegates the client connection to the ServerHandler
            new ServerHandler(clientSocket);
            // Closes the connection
            serverSocket.close();
        } catch (IOException e) {
            // Prints the stack trace in case of IOException
            e.printStackTrace();
        }

    }

}