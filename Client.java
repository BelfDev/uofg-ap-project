import java.io.IOException;
import java.net.Socket;

/**
 * Client application entry point. Connects to the game Server and allows the
 * user to play a BlackJack game.
 */
class Client {

    public static void main(String[] args) {
//        ClientApp.launch();
        connectToServer();
    }

    private static void connectToServer() {
        Socket clientSocket = null;
        try {
            clientSocket = new Socket(Configs.SERVER_HOST, Configs.SERVER_PORT);
            Thread writeThread = new Thread(new Writer(clientSocket));
            ReadWorker readWorker = new ReadWorker(clientSocket);

            writeThread.start();
            readWorker.execute();

            writeThread.join();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
