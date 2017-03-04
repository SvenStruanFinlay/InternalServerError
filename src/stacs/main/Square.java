package stacs.main;

import java.util.ArrayList;
import java.util.List;

public class Square {
    
    public final int x;
    public final int y;
    
    public final List<Entity> entities = new ArrayList<>();
    public final List<Item> items = new ArrayList<>();
    
    public final int height;
    public final TerrainType terrainType;
    public final Room room;
    
    public Square(int x, int y, int height, TerrainType terrainType, Room room) {
        super();
        this.x = x;
        this.y = y;
        this.height = height;
        this.terrainType = terrainType;
        this.room = room;
    }
}
