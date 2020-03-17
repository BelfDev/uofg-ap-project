import java.net.Socket;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Reader implements Runnable {
    private Socket socket;

    public Reader(Socket s) {
        this.socket = s;
    }

    public void run() {
        try {
            ObjectInputStream sc = new ObjectInputStream(this.socket.getInputStream());
            Move move;
            while ((move = (Move) sc.readObject()) != null) {
                System.out.println(move);
            }
            sc.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}