
class BlackJackProtocol implements ApplicationProtocol {

    private GameState gameState;

    public BlackJackProtocol(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public synchronized ServerResponse processInput(ClientRequest request) {
        switch (request.getCommand()) {
            case JOIN:
                gameState.addNewPlayer();
                break;
            case QUIT:
                System.out.println("CAN'T DO MUCH");
                break;
        }

        return new ServerResponse(ResponseStatus.OK, gameState);
    }

}
