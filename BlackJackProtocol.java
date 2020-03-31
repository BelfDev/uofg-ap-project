class BlackJackProtocol implements ApplicationProtocol {

    private static final String AWAITING_PLAYER_MESSAGE = "Awaiting for player in slot ";

    private GameState gameState;

    public BlackJackProtocol(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public synchronized ServerResponse processInput(ClientRequest request) {
        Player requestPlayer = gameState.getPlayer(request.getPlayerId());
        RoundPhase requestRoundPhase = gameState.getRoundPhase();

        switch (request.getCommand()) {
            case JOIN:
                gameState.addNewPlayer();
                break;
            case QUIT:
                gameState.removePlayer(request.getPlayerId());
                System.out.println(String.format("Player %s has left the game", request.getPlayerId()));
                if (requestRoundPhase.equals(RoundPhase.PLAYER_ACTION)) {
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
                if (requestRoundPhase.equals(RoundPhase.INITIAL_BET)) {
                    requestPlayer.setIsBottleneck(false);
                    if (gameState.getBottlenecks().isEmpty()) {
                        gameState.advanceRound();
                        gameState.dealInitialCards();
                        gameState.setFeedbackText(AWAITING_PLAYER_MESSAGE + gameState.getBottleneck().getSlot());
                    }
                } else if (requestPlayer.getId().equals(gameState.getBottleneck().getId()) && requestPlayer.getHandScore() < 21) {
                    // Deals a card
                    PlayingCard card = gameState.dealCard();
                    requestPlayer.addCard(card);
                    // Evaluates new card score
                    if (requestPlayer.getHandScore() > 21) {
                        // Request player lost on this turn
                        // Updates the request player state
                        requestPlayer.setIsEliminated(true);
                        requestPlayer.setIsBottleneck(false);
                        requestPlayer.resetRoundBet();
                        // Updates the overall game state
                        Player nextBottleneck = gameState.getNextBottleneck();
                        // If there are any bottlenecks
                        if (nextBottleneck != null) {
                            gameState.setBottleneck(nextBottleneck);
                            gameState.setFeedbackText(AWAITING_PLAYER_MESSAGE + nextBottleneck.getSlot());
                        } else {
                            // If there are no more bottlenecks
                            gameState.advanceRound();
                        }
                    } else if (requestPlayer.getHandScore() == 21) {
                        // Updates the winner player
                        requestPlayer.setIsWinner(true);
                        // Calculates the player reward
                        int simpleReward = requestPlayer.getRoundBet() * 2;
                        // Increases the player balance
                        requestPlayer.setBalance(simpleReward);
                        // Advances to a new round
                        gameState.advanceRound();
                    }

                    if (gameState.getEliminatedPlayers().size() == Configs.MAX_NUMBER_OF_PLAYERS) {
                        gameState.advanceRound();
                    }

                    // Updates dealer in DEALER REVEAL phase
                    if (gameState.getRoundPhase().equals(RoundPhase.DEALER_REVEAL)) {
                        Dealer dealer = gameState.getDealer();
                        // Draws cards until totals 17
                        while (dealer.getHandScore() < 17) {
                            PlayingCard dealerCard = gameState.dealCard();
                            dealer.addCard(dealerCard);
                        }
                    }
                }
                break;
        }

        return new ServerResponse(ResponseStatus.OK, gameState);
    }


}
