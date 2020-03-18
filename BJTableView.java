import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.io.File;
import java.io.IOException;

class BJTableView extends JPanel {

    private Image backgroundImage;

    public BJTableView() {
        try {
            backgroundImage = ImageIO.read(new File("bj-table.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the background image.
        g.drawImage(backgroundImage, -2, -2, getWidth() + 4, getHeight() + 4, this);
    }

}