import java.io.IOException;
import java.net.Socket;

class ClientHandler extends Thread {

    private Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        super(new BlackjackService(clientSocket));
        this.clientSocket = clientSocket;
        // Starts the newly-created thread
        start();
    }

    @Override
    public void run() {
        super.run();
        System.out.println("New connection on thread " + this.getName());

        try {
            this.clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}