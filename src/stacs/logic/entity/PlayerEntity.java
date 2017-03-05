package stacs.logic.entity;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

import stacs.logic.item.Item;
import stacs.logic.item.ItemSaber;
import stacs.logic.room.Square;
import stacs.server.ServerWorld;

public class PlayerEntity extends LivingEntity {
    private static final long serialVersionUID = -3637404852314186918L;
    
    public transient ServerPlayerData serverData;

    private String name;
    
    public PlayerEntity(String name) {
        this.name = name;
        this.maxHealth = 10;
        this.health = 10;
    }
    
    @Override
    public String getDisplayName() {
        return name;
    }

    @Override
    public void startNextTurn(ServerWorld world) {
        serverData.updateWorld(world, true);
    }

    
    public boolean canMove(Square s){
        return true;
    }
    
    private static final Image img, imgSaber;
    
    static{
        try {
            img = ImageIO.read(PlayerEntity.class.getResourceAsStream("/player.png"));
            imgSaber = ImageIO.read(PlayerEntity.class.getResourceAsStream("/player-saber.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public int getAttachStrength(){
        for(Item it : inventory){
            if(it instanceof ItemSaber)
                return 2;
        }
        return 1;
    }
    
    @Override
    public Image getSprite(int tick) {
        // TODO Auto-generated method stub
        if(getAttachStrength() > 1)
            return imgSaber;
        return img;
    }
}
