import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Observer;

/**
 * Customized thread dedicated to the game server. This class handles the
 * gathering of client I/O streams and outputs of the relevant game state based
 * on the BlackJack protocol.
 */
class BlackJackService implements Runnable {

    private Socket client;
    private Observer server;

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
    public BlackJackService(Socket client) throws IOException {
        // super("GameServerThread");
        // Sets the current client
        this.client = client;
        // this.server = server;
        // Sets the stream input and output
        this.inputStream = client.getInputStream();
        this.outputStream = client.getOutputStream();
    }

    @Override
    public void run() {
        // Opens readers and writers on the client socket's input and output streams
        // The try-with-resources statement takes care of closing resources when
        // necessary
        try ( // Creates an ObjectOutputStream to send data from the server to the client
                ObjectOutputStream output = new ObjectOutputStream(outputStream);
                // Creates an ObjectInputStream to retrieve data from the client
                ObjectInputStream input = new ObjectInputStream(inputStream);) {

            Move inputMove, outputMove;

            // Initiates the application communication protocol
            BlackJackProtocol blackJackProtocol = new BlackJackProtocol();

            while ((inputMove = (Move) input.readObject()) != null) {
                outputMove = blackJackProtocol.processInput(inputMove);
                System.out.println(inputMove);
                output.writeObject(outputMove);
                output.flush();
            }

            // Closes the client connection
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}