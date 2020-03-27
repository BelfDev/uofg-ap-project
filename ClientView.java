import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Set;

class ClientView extends JFrame {

    private TopBarView topBar;
    private BJTableView mainContent;
    private BottomBarView bottomBar;

    public ClientView() {
        setupWindow();
        setupPanels();
        setVisible(true);
    }

    public void updateNumberOfPlayersLabel(String numberOfPlayers) {
        topBar.setPlayersValueLabelText(numberOfPlayers);
    }

    public void addNewPlayer(String name, int slot) {
        mainContent.addPlayer(name, slot);
    }

    public void setActionListener(ActionListener listener) {
        JButton quitButton = bottomBar.getQuitButton();
        quitButton.setActionCommand(ClientActionType.QUIT_GAME.toString());
        quitButton.addActionListener(listener);
    }

    private void setupWindow() {
        // Sets the initial window size (golden ratio)
        this.setSize(Configs.INITIAL_WINDOW_WIDTH, Configs.INITIAL_WINDOW_HEIGHT);
        // Prevents the window from resizing
        this.setResizable(false);
        // Sets the window location to the center of the screen
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void setupPanels() {
        this.topBar = new TopBarView();
        this.mainContent = new BJTableView();
        this.bottomBar = new BottomBarView();

        this.add(topBar, BorderLayout.NORTH);
        this.add(mainContent, BorderLayout.CENTER);
        this.add(bottomBar, BorderLayout.SOUTH);
    }

}