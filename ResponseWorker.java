import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ResponseWorker extends SwingWorker<Void, Void> {

    private Socket clientSocket;
    private StateBroadcaster stateBroadcaster;

    public ResponseWorker(Socket clientSocket, StateBroadcaster stateBroadcaster) {
        this.clientSocket = clientSocket;
        this.stateBroadcaster = stateBroadcaster;
    }

    @Override
    protected synchronized Void doInBackground() throws Exception {
        try {
            ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
            System.out.println("Started read worker");
            ServerResponse serverResponse;
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
