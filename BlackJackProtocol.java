class BlackJackProtocol implements ApplicationProtocol {

    private static final String AWAITING_PLAYER_MESSAGE = "Awaiting for player in slot ";

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
                System.out.println(String.format("Player %s has left the game", request.getPlayerId()));
                if (phase.equals(RoundPhase.CARD_DEAL)) {
                    Player bottleneck = gameState.getBottleneck();
                    gameState.setFeedbackText(AWAITING_PLAYER_MESSAGE + bottleneck.getSlot());
                }
                break;
            case BET:
                int newBet = (Integer) request.getPayload().get("bet");
                requestPlayer.increaseRoundBet(newBet);
                requestPlayer.setBalance(requestPlayer.getBalance() - newBet);
                break;
            case HIT:
                if (phase.equals(RoundPhase.INITIAL_BET)) {
                    requestPlayer.setIsBottleneck(false);
                    if (gameState.getBottlenecks().isEmpty()) {
                        gameState.advanceRound();
                        gameState.dealCards();
                        Player bottleneck = gameState.getBottleneck();
                        gameState.setFeedbackText(AWAITING_PLAYER_MESSAGE + bottleneck.getSlot());
                    }
                }
                break;
        }

        return new ServerResponse(ResponseStatus.OK, gameState);
    }


}
