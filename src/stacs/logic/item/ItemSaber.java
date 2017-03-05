package stacs.logic.item;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ItemSaber extends Item {
    private static final long serialVersionUID = 2805630651408829674L;

    @Override
    public Image getImage() {
        return img;
    }
    
    
     private static Image img;
    
    static{
        try {
            img = ImageIO.read(ItemTreasure.class.getResourceAsStream("/saber.png"));
        } catch (IOException e) {
        }
    }

    @Override
    public String getName() {
        return "saber";
    }

}
