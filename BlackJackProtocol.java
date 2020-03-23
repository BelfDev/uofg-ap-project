
class BlackJackProtocol implements ApplicationProtocol {

    private GameState gameState;

    public BlackJackProtocol(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public synchronized ServerResponse processInput(ClientRequest request) {
        if (request.getCommand() == Command.JOIN) {
            gameState.incrementNumberOfPlayers();
        } else {
            System.out.println("CAN'T DO MUCH");
        }
        return new ServerResponse(ResponseStatus.OK, gameState);
    }

}
