package stacs.main;

import java.util.HashSet;

public class Room {
    public final int id;
    public final ServerWorld world;
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
