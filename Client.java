import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class Client {

    private static Socket clientSocket;

    public static void main(String[] args) {

        try (
            Socket clientSocket = new Socket(Configs.SERVER_HOST, Configs.SERVER_PORT);

            InputStream inputStream = clientSocket.getInputStream();
            OutputStream outputStream = clientSocket.getOutputStream();

            BufferedReader input = new BufferedReader(new InputStreamReader(inputStream));
            PrintWriter output = new PrintWriter(outputStream, true);
        ) {

            BufferedReader clientInput = new BufferedReader(new InputStreamReader(System.in));
            String fromClient, fromServer;

            while ((fromServer = input.readLine()) != null) {
                System.out.println("Server: " + fromServer);

                fromClient = clientInput.readLine();
                if (fromClient != null) {
                    System.out.println("Client: " + fromClient);
                    output.println(fromClient);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // public static void main(String[] args) {
    //     try {
    //         clientSocket = new Socket(Configs.SERVER_ADDRESS, Configs.SERVER_PORT);
    //         Thread readerThread = new Thread(new Reader(clientSocket));
    //         Thread writerThread = new Thread(new Writer(clientSocket));

    //         readerThread.start();
    //         writerThread.start();
    //         readerThread.join();
    //         writerThread.join();

    //     } catch (InterruptedException e) {
    //         e.printStackTrace();
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     } finally {
    //         try {
    //             clientSocket.close();
    //         } catch (IOException e) {
    //             e.printStackTrace();
    //         }
    //     }

    // }

}