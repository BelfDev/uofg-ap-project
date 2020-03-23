import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ReadWorker extends SwingWorker<Void, Void> {

    private Socket clientSocket;

    public ReadWorker(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

//    @Override
//    protected Void doInBackground() throws Exception {
//        try (ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream())) {
//            System.out.println("Started read worker");
//            ServerResponse serverResponse;
//            while ((serverResponse = (ServerResponse) input.readObject()) != null) {
//                System.out.println(serverResponse);
//            }
//            return null;
//        }
//    }

    @Override
    protected synchronized Void doInBackground() throws Exception {
        try {
            ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
            System.out.println("Started read worker");
            ServerResponse serverResponse;
            while ((serverResponse = (ServerResponse) input.readObject()) != null) {
                System.out.println(serverResponse);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
