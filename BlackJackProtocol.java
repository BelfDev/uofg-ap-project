
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
                gameState.removePlayer(request.getPlayerId());
                String feedback = String.format("Player %s has left the game", request.getPlayerId());
                System.out.println(feedback);
                break;
        }

        return new ServerResponse(ResponseStatus.OK, gameState);
    }

}
