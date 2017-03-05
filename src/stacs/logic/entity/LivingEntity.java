package stacs.logic.entity;

import java.util.ArrayList;
import java.util.List;

import stacs.logic.item.Item;
import stacs.logic.item.ItemSaber;
import stacs.logic.room.Square;
import stacs.logic.room.TerrainType;
import stacs.server.ServerWorld;

public abstract class LivingEntity extends Entity {
    public List<Item> inventory = new ArrayList<>();
    public int health;
    public int maxHealth;
    
    public void hurt(int hp){
        health--;
        if(health <= 0){
            die();
        }
    }
    
    public boolean canNavigate(Square square){
        return square.terrainType != TerrainType.trapWall && square.terrainType != TerrainType.wall && (square.terrainType != TerrainType.water || currentSquare.room.freeze);
    }
    
    public void die() {
        this.currentSquare.room.world.remove(this);
    }
    
    @Override
    public abstract void startNextTurn(ServerWorld world);

    public int getAttachStrength() {
        for(Item it : inventory){
            if(it instanceof ItemSaber)
                return 2;
        }
        return 1;
    }
}
