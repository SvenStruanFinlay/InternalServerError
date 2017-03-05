package stacs.logic.item;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ItemTreasure extends Item {

    private static Image img;
    
    static{
        try {
            img = ImageIO.read(ItemTreasure.class.getResourceAsStream("/cup.png"));
        } catch (IOException e) {
        }
    }
    
    @Override
    public Image getImage() {
        return img;
    }

    @Override
    public String getName() {
        return "treasure";
    }

}
