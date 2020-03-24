/**
 * Server application entry point. Creates and starts the server thread.
 * 
 * Author: Pedro Henrique Belfort Fernandes 
 * Matric: 2456943B
 */
class Server {


    public static void main(String[] args) {
        // Creates a new GameServerThread
        Thread serverThread = new GameServerThread();
        // Starts the server thread
        serverThread.start();

        try {
            // Guarantees that the server will terminate
            serverThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}