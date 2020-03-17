import javax.swing.*;
import java.awt.*;

class ClientView extends JFrame {

    private JPanel topBar;
    private JPanel mainContent;
    private JPanel bottomBar;

    public ClientView() {
        setupWindow();
        setupPanels();
        setVisible(true);
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
        this.topBar = new JPanel();
        this.mainContent = new JPanel();
        this.bottomBar = new JPanel();

        topBar.setBackground(Color.blue);
        mainContent.setBackground(Color.red);
        bottomBar.setBackground(Color.green);

        this.add(topBar, BorderLayout.NORTH);
        this.add(mainContent, BorderLayout.CENTER);
        this.add(bottomBar, BorderLayout.SOUTH);
    }
}