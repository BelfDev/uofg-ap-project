import java.net.Socket;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;

public class Writer implements Runnable {
    private Socket socket;

    public Writer(Socket s) {
        this.socket = s;
    }

    public void run() {
        try {
            Scanner sc = new Scanner(System.in);
            ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
            String line;
            System.out.println("What is your move?");
            while (!(line = sc.nextLine()).equals("END")) {
                os.writeObject(new Move(line));
            }
            sc.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}