package stacs.logic.entity;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

import stacs.logic.room.Square;
import stacs.server.ServerWorld;

public class PlayerEntity extends LivingEntity {
    private static final long serialVersionUID = -3637404852314186918L;
    
    public transient ServerPlayerData serverData;

    private String name;
    
    public PlayerEntity(String name) {
        this.name = name;
    }
    
    @Override
    public String getDisplayName() {
        return name;
    }

    @Override
    public void startNextTurn(ServerWorld world) {
        serverData.startNextTurn(world);
    }

    
    public boolean canMove(Square s){
        return true;
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
