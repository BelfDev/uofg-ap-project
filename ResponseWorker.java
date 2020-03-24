import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ResponseWorker extends SwingWorker<Void, Void> {

    private StateBroadcaster stateBroadcaster;
    private ObjectInputStream input = null;

    public ResponseWorker(Socket clientSocket, StateBroadcaster stateBroadcaster) {
        this.stateBroadcaster = stateBroadcaster;
        try {
            input = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected synchronized Void doInBackground() throws Exception {
        System.out.println("Started read worker");
        ServerResponse serverResponse;
        try {
            while ((serverResponse = (ServerResponse) input.readObject()) != null) {
                if (serverResponse.getStatus() == ResponseStatus.OK) {
                    stateBroadcaster.notifyNewState(serverResponse.getGameState());
                }
                System.out.println(serverResponse);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
