import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * SwingWorker that reads incoming responses and notifies
 * observers through the StateBroadcaster.
 */
public class ResponseWorker extends SwingWorker<Void, Void> {

    private StateBroadcaster stateBroadcaster;
    private final Socket clientSocket;

    /**
     * Constructs a ResponseWorker that takes care of reading incoming
     * server responses and notifying observers.
     *
     * @param clientSocket     the client Socket connection to the server
     * @param stateBroadcaster the StateBroadcaster used to notify observers about new state
     */
    public ResponseWorker(Socket clientSocket, StateBroadcaster stateBroadcaster) {
        this.stateBroadcaster = stateBroadcaster;
        this.clientSocket = clientSocket;
    }

    @Override
    protected synchronized Void doInBackground() throws Exception {
        ServerResponse serverResponse;
        try {
            ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
            // Continuously watches for incoming server responses
            while ((serverResponse = (ServerResponse) input.readObject()) != null) {
                // Checks if server status is OK
                if (serverResponse.getStatus() == ResponseStatus.OK) {
                    // Notifies the new shared state received in the response
                    stateBroadcaster.notifyNewState(serverResponse.getGameState());
                }
                // Prints out the server response to the console
                System.out.println(serverResponse);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
