package stacs.logic.entity;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

import stacs.server.ServerWorld;

public class PlayerEntity extends LivingEntity {

    @Override
    public void startNextTurn(ServerWorld world) {
        //do socket communication
    }

    
    private static final Image anim1[];
    
    static{
        try {
            anim1 = new Image[4];
            for(int i = 1; i <= 4; i++){
                anim1[i - 1] = ImageIO.read(PlayerEntity.class.getResourceAsStream("/player" + i + ".png"));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public Image getSprite(int tick) {
        // TODO Auto-generated method stub
        return anim1[(tick / 10) % 4];
    }
}
