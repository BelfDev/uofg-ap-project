
class ClientApp {

    private static ClientApp instance;

    private ClientApp() {
        ClientView view = new ClientView();
        new ClientController(view);
    }

    public static ClientApp launch() {
        if (instance == null) {
            instance = new ClientApp();
        }
        return instance;
    }

}