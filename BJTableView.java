import javax.swing.*;
import java.awt.*;

class BJTableView extends JPanel {

    private Image backgroundImage;

    public BJTableView() {
        backgroundImage = Utils.loadImage("bj-table.png");
        this.setLayout(null);

        PlayerView p1 = new PlayerView();
        PlayerView p2 = new PlayerView();
        PlayerView p3 = new PlayerView();
        PlayerView p4 = new PlayerView();
        PlayerView p5 = new PlayerView();

        p1.setLocation(16, 200);
        p2.setLocation(265, 276);
        p3.setLocation(548, 312);
        p4.setLocation(830, 276);
        p5.setLocation(1064, 200);


        add(p1);
        add(p2);
        add(p3);
        add(p4);
        add(p5);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the background image.
        g.drawImage(backgroundImage, -2, -2, getWidth() + 4, getHeight() + 4, this);
    }


}