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

    public ClientController(ClientView view, RequestSender requestSender) {
        this.view = view;
        this.requestSender = requestSender;
        this.activePlayer = null;
        this.playerList = null;
        this.roundPhase = null;
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
        // Updates the number of players
        updateNumberOfPlayersIfNeeded(previousPlayerState, state.getNumberOfPlayers());
        // Removes players who quit the game
        removeQuitPlayers(previousPlayerState);
        // Updates the dealer
        updateDealerView(state.getDealer());
        // Updates the players views
        updatePlayerViews();
        // Update feedback
        view.setFeedback(state.getFeedbackText());
    }

    private void updateDealerView(Dealer dealer) {
        DealerView dealerView = view.getDealerView();
        updateCards(dealer, dealerView);
        if (roundPhase == RoundPhase.PLAYER_ACTION) {
            dealerView.addCard(new CardView("assets/card_cover"));
        }
        dealerView.setScore(dealer.getHandScore());
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
            case HIT:
                requestCard();
                break;
            default:
                break;
        }
    }

    private void placeBet(int value) {
        int newRoundBet = activePlayer.getRoundBet() + value;
        int balance = activePlayer.getBalance();
        if (newRoundBet <= balance) {
            activePlayer.increaseRoundBet(value);
            requestSender.sendRequest(new ClientRequest.Builder(Command.BET, activePlayer.getId())
                    .withData("bet", value)
                    .build());
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
        // TODO: Create hit request
        requestSender.sendRequest(new ClientRequest.Builder(Command.HIT, activePlayer.getId()).build());
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

                if (player.getIsEliminated()) {
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
                if (player.getIsEliminated()) newPlayer.toggleRedLine();
            }

        }

    }

    private void updateCards(Player player, CardReceiver receiver) {
        List<PlayingCard> cards = player.getCards();
        int numberOfCards = receiver.getNumberOfCards();
        if (numberOfCards < cards.size()) {
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