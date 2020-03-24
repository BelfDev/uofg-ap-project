import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Observable;

/**
 * This class handles the reception of client I/O streams and
 * outputs of the relevant game state based on the BlackJack protocol.
 */
class BlackJackService extends Observable implements Runnable {

    private final GameState gameState;
    private final Socket clientSocket;

    private ObjectOutputStream output;
    private ObjectInputStream input;

    /**
     * Constructs a BlackJackService that listens for client requests, processes
     * its content according to the BlackJackProtocol, and outputs a server response.
     *
     * @param clientSocket the client socket connection
     * @param state        the current shared game state
     * @throws IOException
     */
    public BlackJackService(Socket clientSocket, GameState state) throws IOException {
        // Sets the current client connection
        this.clientSocket = clientSocket;
        // Sets the relevant initial game state
        this.gameState = state;
        // Creates an ObjectOutputStream to send data from the server to the client
        this.output = new ObjectOutputStream(clientSocket.getOutputStream());
        // Creates an ObjectInputStream to retrieve data from the client
        this.input = new ObjectInputStream(clientSocket.getInputStream());
    }

    @Override
    public void run() {
        ClientRequest clientRequest;
        try {
            // Initiates the application communication protocol
            BlackJackProtocol blackJackProtocol = new BlackJackProtocol(gameState);
            // Continuously watches for client input
            while ((clientRequest = (ClientRequest) input.readObject()) != null) {
                // Generates a server response based on the BlackJackProtocol interpretation
                ServerResponse response = blackJackProtocol.processInput(clientRequest);
                // Notifies the generated response to observers
                notifyResponse(response);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                // Closes streams and socket connections
                input.close();
                output.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void notifyResponse(ServerResponse response) {
        setChanged();
        notifyObservers(response);
    }

    /**
     * Sends out the server response as an ObjectOutputStream
     *
     * @param response the response to be transmitted to the client
     */
    public void transmitMessage(ServerResponse response) {
        try {
            System.out.println(response);
            output.writeObject(response);
            output.flush();
            // Enforces ObjectOutputStream cache reset
            output.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}