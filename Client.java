import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

class Client {

    private static Socket clientSocket;

    public static void main(String[] args) {
        try {
            clientSocket = new Socket(Configs.SERVER_ADDRESS, Configs.SERVER_PORT);
            Scanner reader = new Scanner(clientSocket.getInputStream());
            while(reader.hasNextLine()) {
                System.out.println(reader.nextLine());
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}