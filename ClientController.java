import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class ClientController implements StateListener, ActionListener {

    private ClientView view;
    private RequestSender requestSender;
    private Player activePlayer;
    private List<Player> playerList;

    private RoundPhase roundPhase;
    private boolean isBetDoubled;

    public ClientController(ClientView view, RequestSender requestSender) {
        this.view = view;
        this.requestSender = requestSender;
        this.activePlayer = null;
        this.playerList = null;
        this.roundPhase = null;
        this.isBetDoubled = false;
        view.setActionListener(this);
        view.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                quitGame();
            }
        });
    }

    @Override
    public void onReceiveState(GameState state) {
        System.out.println("RECEIVED NEW STATE");
        System.out.println(state);
        // Updates the round phase
        roundPhase = state.getRoundPhase();
        // Stores previous player state
        List<Player> previousPlayerState = playerList;
        // Updates the player state
        playerList = state.getPlayers();
        // Initializes this client's player view
        initActivePlayerIfNeeded();
        // Updates current player reference
        activePlayer = state.getPlayer(activePlayer.getId());
        // Updates the number of players
        updateNumberOfPlayersIfNeeded(previousPlayerState, state.getNumberOfPlayers());
        // Removes players who quit the game
        removeQuitPlayers(previousPlayerState);
        // Updates the dealer
        updateDealerView(state.getDealer());
        // Updates the players views
        updatePlayerViews();
        // Update buttons
        updateButtons(roundPhase);
        // Update feedback
        updateFeedback(state.getFeedbackText());
    }

    private void updateButtons(RoundPhase phase) {
        view.disableDoubleButton(!phase.equals(RoundPhase.PLAYER_ACTION));
        view.disableResetBetButton(!phase.equals(RoundPhase.INITIAL_BET));
        view.disableHitAndStand(phase.equals(RoundPhase.DEALER_REVEAL));
    }

    private void updateFeedback(String serverFeedback) {
        String feedback = serverFeedback;
        if (roundPhase.equals(RoundPhase.DEALER_REVEAL)) {
            if (activePlayer.isPush()) {
                feedback = "It's a draw! You don't lose anything.";
            } else {
                feedback = activePlayer.isWinner() ? "You win! " : "The Dealer takes it all! ";
                feedback += " The round will restart shortly.";
            }
        }
        view.setFeedback(feedback);
    }

    private void updateNumberOfPlayersIfNeeded(List<Player> previousPlayerList, int currentNumberOfPlayers) {
        if (previousPlayerList == null || previousPlayerList.size() != currentNumberOfPlayers) {
            String numberOfPlayersText = String.valueOf(currentNumberOfPlayers);
            view.updateNumberOfPlayersLabel(numberOfPlayersText);
            System.out.println("Number of players changed to " + currentNumberOfPlayers);
        }
    }

    private void initActivePlayerIfNeeded() {
        if (activePlayer == null) {
            activePlayer = playerList.get(playerList.size() - 1);
            view.addNewPlayer("You", activePlayer.getSlot());
            System.out.println("Current player ID: " + activePlayer.getId());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ClientActionType action = ClientActionType.valueOf(e.getActionCommand());
        System.out.println(action.toString());
        switch (action) {
            case QUIT_GAME:
                view.dispatchEvent(new WindowEvent(view, WindowEvent.WINDOW_CLOSING));
                break;
            case BET:
                if (roundPhase == RoundPhase.INITIAL_BET) {
                    JButton chipButton = (JButton) e.getSource();
                    int value = Integer.parseInt(chipButton.getName());
                    placeBet(value);
                }
                break;
            case RESET_BET:
                if (roundPhase == RoundPhase.INITIAL_BET) {
                    requestSender.sendRequest(new ClientRequest.Builder(Command.RESET_BET, activePlayer.getId()).build());
                }
                break;
            case HIT:
                requestCard();
                break;
            case STAND:
                passTurn();
                break;
            case DOUBLE:
                doubleBet();
                break;
            default:
                break;
        }
    }

    private void doubleBet() {
        double balance = activePlayer.getBalance();
        int roundBet = activePlayer.getRoundBet();
        if (!isBetDoubled && roundPhase.equals(RoundPhase.PLAYER_ACTION) && roundBet > 0 && (balance - roundBet * 2) >= 0) {
            requestSender.sendRequest(new ClientRequest.Builder(Command.DOUBLE_DOWN, activePlayer.getId()).build());
            isBetDoubled = true;
        }
    }

    private void passTurn() {
        if (roundPhase.equals(RoundPhase.PLAYER_ACTION)) {
            requestSender.sendRequest(new ClientRequest.Builder(Command.STAND, activePlayer.getId()).build());
        }
    }

    private void placeBet(int value) {
        double balance = activePlayer.getBalance();
        if (value == 0) {
            view.setFeedback("You must bet something!");
        } else if ((balance - value) >= 0) {
            activePlayer.increaseRoundBet(value);
            requestSender.sendRequest(new ClientRequest.Builder(Command.BET, activePlayer.getId())
                    .withData("bet", value)
                    .build());
            isBetDoubled = false;
        } else {
            view.setFeedback("You don't have enough credits to place this bet!");
        }
    }

    private void quitGame() {
        // Confirms whether the user want to quit the game
        int input = JOptionPane.showConfirmDialog(view, "Do you want to quit the game?");
        if (input == 0) {
            // Sends out a quit request to the server
            requestSender.sendRequest(new ClientRequest.Builder(Command.QUIT, activePlayer.getId()).build());
            // Exit the application
            System.exit(0);
        }
    }

    private void requestCard() {
        if (!isBetDoubled && !activePlayer.isEliminated() && activePlayer.getRoundBet() > 0) {
            requestSender.sendRequest(new ClientRequest.Builder(Command.HIT, activePlayer.getId()).build());
        }

    }

    private void updatePlayerViews() {
        for (Player player : playerList) {
            System.out.println("\n\n PLAYER ID: " + player.getId() + "\n SLOT: " + player.getSlot());
            boolean hasPlayerView = view.getPlayerViewMap().containsKey(player.getSlot());
            if (hasPlayerView) {
                // Retrieves player view
                PlayerView playerView = view.getPlayerViewMap().get(player.getSlot());
                // Updates the bets
                playerView.setBetValue(String.valueOf(player.getRoundBet()));
                // Update cards
                updateCards(player, playerView);
                // Updates score label
                playerView.setScore(player.getHandScore());
                // Displays a red line in case the player is eliminated
                if (player.isEliminated()) {
                    playerView.toggleRedLine();
                }
                // Updates the view related to the active player
                if (player.getId().equals(activePlayer.getId())) {
                    // Updates the balance
                    view.setBalance(String.valueOf(player.getBalance()));
                }
            } else if (!player.getId().equals(activePlayer.getId())) {
                // Add a new player view
                PlayerView newPlayer = view.addNewPlayer("Someone", player.getSlot());
                updateCards(player, newPlayer);
                if (player.isEliminated()) newPlayer.toggleRedLine();
            }

        }

    }

    private void updateDealerView(Dealer dealer) {
        DealerView dealerView = view.getDealerView();
        if (roundPhase == RoundPhase.DEALER_REVEAL) {
            dealerView.removeCards();
        }
        updateCards(dealer, dealerView);
        if (roundPhase == RoundPhase.PLAYER_ACTION && dealerView.getNumberOfCards() == 1) {
            dealerView.addCard(new CardView("assets/card_cover"));
        }
        dealerView.setScore(dealer.getHandScore());
    }

    private void updateCards(Player player, CardReceiver receiver) {
        List<PlayingCard> cards = player.getCards();
        int numberOfCards = receiver.getNumberOfCards();
        if (roundPhase.equals(RoundPhase.INITIAL_BET) && numberOfCards > 0) {
            // Remove player cards if the game was reset
            receiver.removeCards();
        } else if (numberOfCards < cards.size()) {
            for (int i = numberOfCards; i < cards.size(); i++) {
                CardView cardView = new CardView(cards.get(i).getAssetName());
                receiver.addCard(cardView);
            }
        }
    }

    private void removeQuitPlayers(List<Player> previousPlayers) {
        // Remove players that quit
        if (previousPlayers != null && previousPlayers.size() != playerList.size()) {
            List<Player> oldPlayers = getPlayerDiff(previousPlayers);
            for (Player player : oldPlayers) {
                view.removePlayer(player.getSlot());
            }
        }
    }

    private List<Player> getPlayerDiff(List<Player> previousPlayers) {
        Predicate<Player> notSameId = p1 -> playerList.stream().noneMatch(p2 -> p2.getId().equals(p1.getId()));
        return previousPlayers.stream()
                .filter(notSameId)
                .collect(Collectors.toCollection(ArrayList::new));
    }

}