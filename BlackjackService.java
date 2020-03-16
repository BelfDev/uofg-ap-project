import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

class BlackjackService implements Runnable, SomeInterface {

    private Socket client;
    private Scanner input;
    private PrintWriter output;

    public BlackjackService(Socket client) {
        this.client = client;
        try {
            // Gets data input and output streams
            InputStream inputStream = client.getInputStream();
            OutputStream outputStream = client.getOutputStream();
            // Converts streams into scanners and writers to manipulate strings
            input = new Scanner(inputStream);
            output = new PrintWriter(outputStream);
        } catch (IOException e) {
            // Handles exceptions
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("I am the black jack service!");
        while (input.hasNext()) {
            // Retrieves client command
            String command = input.next();
            // Processes commands
            if (command.equals("YO")) {
                output.println("What's up");
            } else {
                output.println("Something else");
            }
            // Sends output to the client
            output.flush();
        }
    }

    @Override
    public void doSomething() {
        System.out.println("I'll do something!");
    }

}