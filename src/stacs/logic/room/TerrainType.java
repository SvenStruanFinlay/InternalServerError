package stacs.logic.room;

import java.awt.Color;

public enum TerrainType {
    dirt(Color.decode("#8B4513")),
    stone(Color.gray);
    
    public final Color c;
    
    TerrainType(Color c){
        this.c = c;
    }
}
