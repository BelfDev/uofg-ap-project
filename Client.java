import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Client application entry point. Connects to the game Server and allows the
 * user to play a BlackJack game.
 */
class Client {

    public static void main(String[] args) {
        ClientApp.launch();
        // connectToServer();
    }

    private static void connectToServer() {
        Socket server = null;
        try {
            server = new Socket(Configs.SERVER_HOST, Configs.SERVER_PORT);
            Thread readThread = new Thread(new Reader(server));
            Thread writeThread = new Thread(new Writer(server));
            readThread.start();
            writeThread.start();
            readThread.join();
            writeThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // public static void main(String[] args) {

    // // Opens a socket that is connected to the Server on SERVER_HOST and
    // SERVER_PORT
    // try (Socket clientSocket = new Socket(Configs.SERVER_HOST,
    // Configs.SERVER_PORT);
    // // Gets the I/O streams of the connection
    // OutputStream outputStream = clientSocket.getOutputStream();
    // InputStream inputStream = clientSocket.getInputStream();
    // // Turns the I/O streams into object I/O streams
    // ObjectOutputStream output = new ObjectOutputStream(outputStream);
    // ObjectInputStream input = new ObjectInputStream(inputStream);) {

    // // Retrieves input from the client user
    // BufferedReader clientInput = new BufferedReader(new
    // InputStreamReader(System.in));
    // Move fromClient, fromServer;

    // Move initialMove = new Move("Let's do it");
    // output.writeObject(initialMove);

    // // Awaits incoming data from the server
    // while ((fromServer = (Move) input.readObject()) != null) {
    // // Sends the user input to the server
    // System.out.println("Server: " + fromServer);
    // // Retrieves user input
    // String line = clientInput.readLine();
    // fromClient = new Move(line);
    // // Sends user input to the server
    // if (fromClient != null) {
    // System.out.println("Client: " + fromClient);
    // output.writeObject(fromClient);
    // output.flush();
    // }
    // }

    // clientInput.close();

    // } catch (IOException e) {
    // System.out.println("Server Terminated");
    // e.printStackTrace();
    // } catch (ClassNotFoundException e) {
    // e.printStackTrace();
    // }

    // }

}
