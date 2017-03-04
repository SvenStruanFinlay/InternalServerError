package stacs.main;

import java.awt.Image;
import java.io.Serializable;

public abstract class Entity implements Serializable {
    private static final long serialVersionUID = 8882148407207576417L;
    
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
