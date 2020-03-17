import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Client application entry point. Connects to the game Server and allows the
 * user to play a BlackJack game.
 */
class Client {

    public static void main(String[] args) {

        // Opens a socket that is connected to the Server on SERVER_HOST and SERVER_PORT
        try (Socket clientSocket = new Socket(Configs.SERVER_HOST, Configs.SERVER_PORT);
                // Gets the I/O streams of the connection
                InputStream inputStream = clientSocket.getInputStream();
                OutputStream outputStream = clientSocket.getOutputStream();
                // Turns the I/O streams into a reader and a writer
                BufferedReader input = new BufferedReader(new InputStreamReader(inputStream));
                PrintWriter output = new PrintWriter(outputStream, true);) {
            // Retrieves input from the client user
            BufferedReader clientInput = new BufferedReader(new InputStreamReader(System.in));
            String fromClient, fromServer;

            // Awaits incoming data from the server
            while ((fromServer = input.readLine()) != null) {
                // Sends the user input to the server
                System.out.println("Server: " + fromServer);
                // Retrieves user input
                fromClient = clientInput.readLine();
                // Sends user input to the server
                if (fromClient != null) {
                    System.out.println("Client: " + fromClient);
                    output.println(fromClient);
                }
            }

            clientInput.close();

        } catch (IOException e) {
            System.out.println("Server Terminated");
            e.printStackTrace();
        }

    }

}
