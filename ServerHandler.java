import java.io.IOException;
import java.net.Socket;

class ServerHandler extends Thread {

    private Socket socket;

    public ServerHandler(Socket socket) {
        this.socket = socket;
        // Starts the newly-created thread
        start();
    }

    @Override
    public void run() {
        super.run();
        System.out.println("New connection on thread " + this.getName());

        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}