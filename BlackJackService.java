import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Observable;

/**
 * Customized thread dedicated to the game server. This class handles the
 * gathering of client I/O streams and outputs of the relevant game state based
 * on the BlackJack protocol.
 */
class BlackJackService extends Observable implements Runnable {

    private final GameState gameState;

    private Socket client;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerResponse response;

    /**
     * Creates a GameServer thread that listens to client input and outputs new game
     * state.
     * 
     * @param client the client socket connection
     * @param state the game state shared between all players
     * @throws IOException if an I/O error occurs when creating the input stream,
     *                     the socket is closed, the socket is not connected, or the
     *                     socket input has been shutdown using
     */
    public BlackJackService(Socket client, GameState state) throws IOException {
        // Sets the current client
        this.client = client;
        // Creates the game state
        this.gameState = state;
        // Creates an ObjectOutputStream to send data from the server to the client
        this.output = new ObjectOutputStream(client.getOutputStream());
        // Creates an ObjectInputStream to retrieve data from the client
        this.input = new ObjectInputStream(client.getInputStream());
    }

    @Override
    public void run() {
        // Opens readers and writers on the client socket's input and output streams
        try {
            ClientRequest clientRequest;

            // Initiates the application communication protocol
            BlackJackProtocol blackJackProtocol = new BlackJackProtocol(gameState);

            while ((clientRequest = (ClientRequest) input.readObject()) != null) {
                synchronized (this) {
                    ServerResponse response = blackJackProtocol.processInput(clientRequest);
                    setResponse(response);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
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

    public void setResponse(ServerResponse response) {
        this.response = response;
        setChanged();
        notifyObservers(response);
    }

    public void transmitMessage(ServerResponse response) {
        try {
            System.out.println(response);
            output.writeObject(response);
            output.flush();
            output.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}