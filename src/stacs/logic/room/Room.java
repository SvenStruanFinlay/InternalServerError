package stacs.logic.room;

import java.io.Serializable;
import java.util.HashSet;

import stacs.server.ServerWorld;

public class Room implements Serializable {
    private static final long serialVersionUID = 7837788358931915072L;
    
    public boolean rain = false;
    
    public final int id;
    public final transient ServerWorld world;
    public final int w;
    public final int h;
    public Square[][] squares;
    
    public Room(int id, ServerWorld world, int w, int h) {
        this.id = id;
        this.world = world;
        this.w = w;
        this.h = h;
        
        squares = new Square[w][h];
    }
}
