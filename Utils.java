import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;

public class Utils {

    private Utils() {
    }

    public static Image loadImage(String fileName) {
        try {
            return ImageIO.read(new File(fileName));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Font getBoldFont(Font currentFont, int fontSize) {
        return new Font(currentFont.getName(), Font.BOLD, fontSize);
    }

}
