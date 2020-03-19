import javax.swing.*;
import java.awt.*;

class BJTableView extends JPanel {

    private Image backgroundImage;

    public BJTableView() {
        backgroundImage = Utils.loadImage("assets/bj-table.png");
        this.setLayout(null);

        DealerView dealer = new DealerView();

        dealer.setLocation(308, 100);

        PlayerView p1 = new PlayerView();
        PlayerView p2 = new PlayerView();
        PlayerView p3 = new PlayerView();
        PlayerView p4 = new PlayerView();
        PlayerView p5 = new PlayerView();

        p1.setLocation(8, 240);
        p2.setLocation(265, 306);
        p3.setLocation(524, 324);
        p4.setLocation(780, 306);
        p5.setLocation(1038, 240);


        add(dealer);
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