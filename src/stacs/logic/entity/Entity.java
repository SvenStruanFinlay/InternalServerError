package stacs.logic.entity;

import java.awt.Image;
import java.io.Serializable;

import stacs.logic.room.Room;
import stacs.logic.room.Square;
import stacs.logic.turn.NullAction;
import stacs.server.ServerWorld;

public abstract class Entity implements Serializable {
    private static final long serialVersionUID = 8882148407207576417L;
    
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
