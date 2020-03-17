/**
 * Server application entry point. Creates and starts the server thread.
 * 
 * Author: Pedro Henrique Belfort Fernandes 
 * Matric: 2456943B
 */
class Server {

    public static void main(String[] args) {
        // Creates a new thread to run the GameServer
        Thread serverThread = new Thread(new GameServer());
        // Starts the server thread
        serverThread.start();
        // Guarantees that the serverThread will terminate
        try {
            serverThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}