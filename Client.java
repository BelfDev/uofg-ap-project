import java.io.IOException;
import java.net.Socket;

class Client {

    public static void main(String[] args) {
        try {
            final Socket clientSocket = new Socket(Configs.SERVER_ADDRESS, Configs.SERVER_PORT);
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}