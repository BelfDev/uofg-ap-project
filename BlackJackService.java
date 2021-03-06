import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.EOFException;
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

    private static final int RESTART_ROUND_DELAY = 4000;
    // Shared game state
    private final GameState gameState;
    // Client connection socket
    private final Socket clientSocket;
    // Boolean flag to indicate if a service is alive
    private boolean isAlive;
    // I/O object streams
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
        // Indicates if the current service is still being served
        this.isAlive = true;
        // Creates an ObjectOutputStream to send data from the server to the client
        this.output = new ObjectOutputStream(clientSocket.getOutputStream());
        // Creates an ObjectInputStream to retrieve data from the client
        this.input = new ObjectInputStream(clientSocket.getInputStream());
    }

    /**
     * Indicates if this service is still alive or if the user has quit.
     *
     * @return a boolean value indicating if the service is still alive.
     */
    public boolean isAlive() {
        return isAlive;
    }

    @Override
    public void run() {
        ClientRequest clientRequest;
        try {
            // Initiates the application protocol
            BlackJackProtocol blackJackProtocol = new BlackJackProtocol(gameState);
            // Continuously watches for client input
            while ((clientRequest = (ClientRequest) input.readObject()) != null) {
                // Generates a server response based on the BlackJackProtocol processing
                ServerResponse response = blackJackProtocol.processInput(clientRequest);
                // Checks whether the service should be torn down
                if (clientRequest.getCommand() == Command.QUIT) {
                    this.isAlive = false;
                }
                // Notifies the generated response to observers
                notifyResponse(response);
                endRoundIfNeeded();
            }
        } catch (EOFException ex1) {
            System.out.println("Service terminated");
        } catch (IOException | ClassNotFoundException ex2) {
            ex2.printStackTrace();
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

    // CONVENIENCE METHODS

    private void endRoundIfNeeded() {
        // Sets an asynchronous trigger that restarts the game after 4 seconds
        if (gameState.getRoundPhase().equals(RoundPhase.DEALER_REVEAL)) {
            ActionListener taskPerformer = evt -> {
                gameState.advanceRound();
                gameState.resetDealer();
                notifyResponse(new ServerResponse(ResponseStatus.OK, gameState));
            };
            Timer timer = new Timer(RESTART_ROUND_DELAY, taskPerformer);
            timer.setRepeats(false);
            timer.start();
        }
    }

    private void notifyResponse(ServerResponse response) {
        setChanged();
        notifyObservers(response);
    }

}