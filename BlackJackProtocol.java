import java.util.List;

class BlackJackProtocol implements ApplicationProtocol {

    private GameState gameState;

    public BlackJackProtocol(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public synchronized ServerResponse processInput(ClientRequest request) {
        Player requestPlayer = gameState.getPlayer(request.getPlayerId());
        RoundPhase phase = gameState.getRoundPhase();
        switch (request.getCommand()) {
            case JOIN:
                gameState.addNewPlayer();
                break;
            case QUIT:
                gameState.removePlayer(request.getPlayerId());
                String feedback = String.format("Player %s has left the game", request.getPlayerId());
                System.out.println(feedback);
                break;
            case BET:
                requestPlayer.setRoundBet((Integer) request.getPayload().get("bet"));
                break;
            case HIT:
                if (phase.equals(RoundPhase.INITIAL_BET)) {
                    requestPlayer.setIsBottleneck(false);
                    if (gameState.getBottlenecks().isEmpty()) {
                        gameState.advanceRound();
                    }
                }
                break;
        }

        return new ServerResponse(ResponseStatus.OK, gameState);
    }


}
