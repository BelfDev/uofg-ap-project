import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

/**
 * Customized thread dedicated to the game server. This class handles the
 * gathering of client I/O streams and outputs of the relevant game state based
 * on the BlackJack protocol.
 */
class BlackJackService extends Observable implements Runnable {

    private Socket client;

    private ObjectOutputStream output = null;
    private ObjectInputStream input = null;

    private Move move;

    GameServer server;

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
        // Creates an ObjectOutputStream to send data from the server to the client
        output = new ObjectOutputStream(client.getOutputStream());
        // Creates an ObjectInputStream to retrieve data from the client
        input = new ObjectInputStream(client.getInputStream());
    }

    @Override
    public void run() {
        // Opens readers and writers on the client socket's input and output streams
        try {
            Move inputMove, outputMove;

            // Initiates the application communication protocol
            BlackJackProtocol blackJackProtocol = new BlackJackProtocol();

            while ((inputMove = (Move) input.readObject()) != null) {
                outputMove = blackJackProtocol.processInput(inputMove);
                setMove(outputMove);
                
                // System.out.println(inputMove);
                // output.writeObject(outputMove);
                // output.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                // Closes streams and sockets
                client.close();
                output.close();
                input.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void setMove(Move move) {
        this.move = move;
        setChanged();
        notifyObservers(move);
    }

    public void transmitMessage(Move m) {
        try {
            output.writeObject(m);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}