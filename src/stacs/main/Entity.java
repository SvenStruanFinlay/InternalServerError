package stacs.main;

import java.awt.Image;

public abstract class Entity {

    public Room currentRoom;
    public Square currentSquare;
    
    public String getDisplayName() {
        return null;
    }
    
    public void startNextTurn(ServerWorld world) {
        world.nextActionMap.put(this, new NullAction());
    }
    
    public Image getSprite(int tick){
        return null;
    }
}
