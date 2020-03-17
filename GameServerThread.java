import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Customized thread dedicated to the game server. This class handles the
 * gathering of client I/O streams and the output of the relevant game state
 * based on the BlackJack protocol.
 */
class GameServerThread extends Thread {

    private Socket client;

    private InputStream inputStream;
    private OutputStream outputStream;

    /**
     * Creates a GameServer thread that listens to client input and outputs new game
     * state.
     * 
     * @param client the client socket connection
     * @throws IOException if an I/O error occurs when creating the input stream,
     *                     the socket is closed, the socket is not connected, or the
     *                     socket input has been shutdown using
     */
    public GameServerThread(Socket client) throws IOException {
        // super("GameServerThread");
        // Sets the current client
        this.client = client;
        // Sets the stream input and output
        this.inputStream = client.getInputStream();
        this.outputStream = client.getOutputStream();
        // Starts the thread execution
        start();
    }

    @Override
    public void run() {
        // Opens readers and writers on the client socket's input and output streams
        // The try-with-resources statement takes care of closing resources when
        // necessary
        try (InputStreamReader reader = new InputStreamReader(inputStream);
                BufferedReader input = new BufferedReader(reader);
                // Creates a writer that automatically flushes the output buffer
                PrintWriter output = new PrintWriter(outputStream, true);) {
            String inputLine, outputLine;
            output.println("New client connected at " + this.getName());

            // Initiates the application communication protocol
            BlackJackProtocol blackJackProtocol = new BlackJackProtocol();

            while ((inputLine = input.readLine()) != null) {
                outputLine = blackJackProtocol.processInput(inputLine);
                System.out.println(inputLine);
                output.println(outputLine);
                output.println(inputLine);
            }

            // Closes the client connection
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}