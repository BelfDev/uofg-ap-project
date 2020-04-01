import java.util.List;

/**
 * This class encapsulates the application protocol guidelines for
 * communication between client-server and the processing of requests.
 * The protocol receives a client request, interprets it according to
 * game rules, and outputs a server response containing a new game new state.
 */
class BlackJackProtocol implements ApplicationProtocol {

    private GameState gameState;

    /**
     * Constructs the black jack application protocol.
     *
     * @param gameState the shared game state.
     */
    public BlackJackProtocol(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public synchronized ServerResponse processInput(ClientRequest request) {
        Player requestPlayer = gameState.getPlayer(request.getPlayerId());
        RoundPhase requestRoundPhase = gameState.getRoundPhase();
        // Checks which command has been issued
        switch (request.getCommand()) {
            case JOIN:
                addNewPlayer();
                break;
            case QUIT:
                removePlayer(request, requestRoundPhase);
                break;
            case BET:
                updateBet(request, requestPlayer);
                break;
            case HIT:
                handleHitAction(requestPlayer, requestRoundPhase);
                break;
            case STAND:
                handleStandAction(requestPlayer, requestRoundPhase);
                break;
            case DOUBLE_DOWN:
                handleDoubleDownAction(requestPlayer, requestRoundPhase);
                break;
            case RESET_BET:
                resetBet(requestPlayer);
                break;
        }

        return new ServerResponse(ResponseStatus.OK, gameState);
    }

    // PROTOCOL PROCESSING

    private void addNewPlayer() {
        // Checks if the current number of players has reached the max
        if (gameState.getNumberOfPlayers() <= Configs.MAX_NUMBER_OF_PLAYERS) {
            // Adds a new player to the shared game state
            gameState.addNewPlayer();
        }
    }

    private void removePlayer(ClientRequest request, RoundPhase requestRoundPhase) {
        // Remove a player who issued the QUIT command from the game
        gameState.removePlayer(request.getPlayerId());
        System.out.println(String.format("Player %s has left the game", request.getPlayerId()));
        if (requestRoundPhase.equals(RoundPhase.PLAYER_ACTION)) {
            // Updates the game state after the player has left
            Player bottleneck = gameState.getNextBottleneck();
            gameState.setFeedbackText(GameState.AWAITING_PLAYER_MESSAGE + bottleneck.getSlot());
        }
    }

    private void updateBet(ClientRequest request, Player requestPlayer) {
        // Updates the request player bet in the shared game state
        int newBet = (Integer) request.getPayload().get("bet");
        requestPlayer.increaseRoundBet(newBet);
        requestPlayer.setBalance(requestPlayer.getBalance() - newBet);
    }

    private void handleHitAction(Player requestPlayer, RoundPhase requestRoundPhase) {
        if (requestRoundPhase.equals(RoundPhase.INITIAL_BET)) {
            passTurn(requestPlayer.getId(), requestRoundPhase);
        } else if (gameState.getBottleneck().getId().equals(requestPlayer.getId()) && requestPlayer.getHandScore() < 21) {
            dealOneCard(requestPlayer, requestRoundPhase);
        }
        updateDealerIfNeeded();
    }

    private void handleStandAction(Player requestPlayer, RoundPhase requestRoundPhase) {
        if (gameState.getBottleneck().getId().equals(requestPlayer.getId())) {
            passTurn(requestPlayer.getId(), requestRoundPhase);
            updateDealerIfNeeded();
        }
    }

    private void handleDoubleDownAction(Player requestPlayer, RoundPhase requestRoundPhase) {
        if (gameState.getBottleneck().getId().equals(requestPlayer.getId())) {
            int currentBet = requestPlayer.getRoundBet();
            double balance = requestPlayer.getBalance();

            requestPlayer.increaseRoundBet(currentBet);
            requestPlayer.setBalance(balance - currentBet);

            dealOneCard(requestPlayer, requestRoundPhase);
            if (gameState.getRoundPhase().equals(RoundPhase.PLAYER_ACTION)) {
                passTurn(requestPlayer.getId(), requestRoundPhase);
            }
            updateDealerIfNeeded();
        }
    }

    private void resetBet(Player requestPlayer) {
        if (gameState.getRoundPhase().equals(RoundPhase.INITIAL_BET)) {
            double balance = requestPlayer.getBalance();
            int currentBet = requestPlayer.getRoundBet();
            requestPlayer.setBalance(balance + currentBet);
            requestPlayer.resetRoundBet();
        }
    }

    // OPERATIONS

    private void dealOneCard(Player requestPlayer, RoundPhase requestRoundPhase) {
        PlayingCard card = gameState.dealCard();
        requestPlayer.addCard(card);
        // Evaluates new card score
        if (requestPlayer.getHandScore() > 21) {
            // Request player lost on this turn
            // Updates the request player state
            requestPlayer.setIsEliminated(true);
            requestPlayer.resetRoundBet();
            // If there are any bottlenecks
            passTurn(requestPlayer.getId(), requestRoundPhase);
        }
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
            gameState.setBottleneck(gameState.getNextBottleneck());
            gameState.setFeedbackText(GameState.AWAITING_PLAYER_MESSAGE + gameState.getNextBottleneck().getSlot());
        } else if (gameState.getEliminatedPlayers().size() == Configs.MAX_NUMBER_OF_PLAYERS) {
            gameState.advanceRound();
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
        boolean dealerHasBlackJack = dealerScore == 21 && dealer.getCards().size() == 2;
        System.out.println("\n\n DEALER SCORE:");
        System.out.println(dealerScore);

        players.forEach(player -> {
            // Checks if ongoing players have won
            int playerScore = player.getHandScore();
            double previousBalance = player.getBalance();

            boolean playerHasBlackJack = playerScore == 21 && player.getCards().size() == 2;
            boolean dealerHasTwentyOneSum = !dealerHasBlackJack && dealerScore == 21;
            boolean playerHasHigherScore = playerScore > dealerScore && dealerScore < 21;

            if (playerHasHigherScore || (playerHasBlackJack && dealerHasTwentyOneSum)) {
                // Sets the player as a winner
                player.setIsWinner(true);
                // Calculates the reward
                int numberOfCards = player.getCards().size();
                double roundBet = player.getRoundBet();
                // If player has a black jack, he gets paid 3 to 2.
                // Otherwise, he gets paid 2 to 1.
                double reward = numberOfCards == 2 ? roundBet * 1.5 : roundBet;
                player.setBalance(previousBalance + roundBet + reward);
            } else if (playerScore == dealerScore && dealerScore <= 21) {
                // This is a draw (push)
                player.setIsPush(true);
                player.setBalance(previousBalance + player.getRoundBet());
            } else if (playerScore <= 21 && dealerScore > 21) {
                // The dealer busted
                player.setIsWinner(true);
                player.setBalance(previousBalance + player.getRoundBet() * 2);
            }
            // Resets the round bet
            player.resetRoundBet();
        });

    }

}
