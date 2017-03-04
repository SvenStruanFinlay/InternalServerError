package stacs.main;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PlayerEntity extends LivingEntity {

    @Override
    public void startNextTurn(ServerWorld world) {
        //do socket communication
    }

    
    private static final Image img;
    
    static{
        try {
            img = ImageIO.read(PlayerEntity.class.getResourceAsStream("/player.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public Image getSprite(int tick) {
        // TODO Auto-generated method stub
        return img;
    }
}
