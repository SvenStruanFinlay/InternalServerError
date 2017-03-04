package stacs.main;

import java.util.HashSet;

public class Room {
    public final int id;
    public final ServerWorld world;
    public Square[][] squares;
    
    public Room(int id, ServerWorld world) {
        this.id = id;
        this.world = world;
    }
}
