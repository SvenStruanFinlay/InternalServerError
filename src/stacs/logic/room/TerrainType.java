package stacs.logic.room;

import java.awt.Color;

public enum TerrainType {
    dirt(Color.decode("#8B4513")),
    stone(Color.gray),
    water(Color.blue),
    wall(Color.cyan), trapWall(Color.cyan.darker());
    
    public final Color c;
    
    TerrainType(Color c){
        this.c = c;
    }
}
