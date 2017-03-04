package stacs.logic.room;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import stacs.logic.entity.Entity;
import stacs.logic.item.Item;

public class Square implements Serializable {
    private static final long serialVersionUID = 8941433203101625178L;
    
    public final int x;
    public final int y;
    
    public final List<Entity> entities = new ArrayList<>();
    public final List<Item> items = new ArrayList<>();
    
    public Square teleport = null;
    
    public int height;
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
