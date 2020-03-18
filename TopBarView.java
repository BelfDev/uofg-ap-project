import javax.swing.*;
import java.awt.*;

class TopBarView extends JPanel {

    public TopBarView() {
        // Sets the bottom bar height
        Dimension size = new Dimension(0, Configs.TOP_BAR_HEIGHT);
        this.setPreferredSize(size);
        // Sets the layout manager to a GridLayout
        GridLayout gridLayout = new GridLayout(0, 3);
        this.setLayout(gridLayout);
        // Sets the bottom bar top border
        this.setBorder(BorderFactory.createMatteBorder(0, 0, Configs.BAR_BORDER_SIZE, 0, Configs.TOP_BAR_BOTTOM_BORDER_COLOR));
        // Sets top bar colors
        this.setBackground(Configs.TOP_BAR_COLOR);
    }

}