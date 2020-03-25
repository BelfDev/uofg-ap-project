import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

//TODO: Used for debugging purposes. Delete this afterwards.
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

            while (!(line = sc.nextLine()).equals("END")) {
                Command command;
                try {
                    command = Command.valueOf(line);
                } catch (IllegalArgumentException e) {
                    command = Command.QUIT;
                }
                ClientRequest request = new ClientRequest.Builder(command, null).build();
                os.writeObject(request);
            }
            sc.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}