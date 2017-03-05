package stacs.logic.item;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ItemApple extends Item {
    private static final long serialVersionUID = 7901168897705466332L;

    private static Image img;
    
    static{
        try {
            img = ImageIO.read(ItemTreasure.class.getResourceAsStream("/apple.png"));
        } catch (IOException e) {
        }
    }
    
    @Override
    public Image getImage() {
        return img;
    }

    @Override
    public String getName() {
        return "apple";
    }

}
