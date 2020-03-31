import java.util.List;

class BlackJackProtocol implements ApplicationProtocol {

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
                    gameState.setFeedbackText(GameState.AWAITING_PLAYER_MESSAGE + bottleneck.getSlot());
                }
                break;
            case BET:
                int newBet = (Integer) request.getPayload().get("bet");
                requestPlayer.increaseRoundBet(newBet);
                requestPlayer.setBalance(requestPlayer.getBalance() - newBet);
                break;
            case HIT:
                if (requestRoundPhase.equals(RoundPhase.INITIAL_BET)) {
                    passTurn(requestPlayer.getId(), requestRoundPhase);
//                    requestPlayer.setIsBottleneck(false);
//                    if (gameState.getBottlenecks().isEmpty()) {
//                        gameState.advanceRound();
//                        gameState.dealInitialCards();
//                        gameState.setFeedbackText(AWAITING_PLAYER_MESSAGE + gameState.getBottleneck().getSlot());
//                    }
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
                            gameState.setFeedbackText(GameState.AWAITING_PLAYER_MESSAGE + nextBottleneck.getSlot());
                        } else {
                            // If there are no more bottlenecks
                            gameState.advanceRound();
                        }
                    }

                    if (gameState.getEliminatedPlayers().size() == Configs.MAX_NUMBER_OF_PLAYERS) {
                        gameState.advanceRound();
                    }
                    updateDealerIfNeeded();

                }
                break;
            case STAND:
                passTurn(requestPlayer.getId(), requestRoundPhase);
                updateDealerIfNeeded();
                break;
        }

        return new ServerResponse(ResponseStatus.OK, gameState);
    }

    private void passTurn(String requestPlayerId, RoundPhase requestRoundPhase) {
        Player requestPlayer = gameState.getPlayer(requestPlayerId);
        requestPlayer.setIsBottleneck(false);
        if (gameState.getBottlenecks().isEmpty()) {
            gameState.advanceRound();
            if (requestRoundPhase.equals(RoundPhase.INITIAL_BET)) {
                gameState.dealInitialCards();
            }
        } else if (requestRoundPhase.equals(RoundPhase.PLAYER_ACTION)) {
            gameState.setFeedbackText(GameState.AWAITING_PLAYER_MESSAGE + gameState.getNextBottleneck().getSlot());
        }
    }

    private void updateDealerIfNeeded() {
        // Updates dealer in DEALER REVEAL phase
        if (gameState.getRoundPhase().equals(RoundPhase.DEALER_REVEAL)) {
            Dealer dealer = gameState.getDealer();
            // Draws cards until totals 17
            while (dealer.getHandScore() < 17) {
                PlayingCard dealerCard = gameState.dealCard();
                dealer.addCard(dealerCard);
            }
            processRoundOutcome();
        }
    }

    private void processRoundOutcome() {
        List<Player> players = gameState.getOngoingPlayers();
        Dealer dealer = gameState.getDealer();
        int dealerScore = dealer.getHandScore();
        // Checks if the dealer has a black jack
        if (dealerScore == 21 && dealer.getCards().size() == 2) {
            gameState.setFeedbackText("The dealer has a Black Jack!");
        } else {
            // TODO: Set game state feedback
            players.forEach(player -> {
                // Checks if ongoing players have won
                int playerScore = player.getHandScore();
                double previousBalance = player.getBalance();
                if (playerScore > dealerScore && dealerScore < 21) {
                    // Sets the player as a winner
                    player.setIsWinner(true);
                    // Calculates the reward
                    int numberOfCards = player.getCards().size();
                    double roundBet = player.getRoundBet();
                    // If player has a black jack, he gets paid 3 to 2.
                    // Otherwise, he gets paid 2 to 1.
                    double reward = numberOfCards == 2 ? roundBet * 2 : roundBet;
                    player.setBalance(previousBalance + reward);
                }
                // Resets the round bet
                player.resetRoundBet();
            });
        }
    }

}
