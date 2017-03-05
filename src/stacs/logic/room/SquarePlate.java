package stacs.logic.room;

public class SquarePlate extends Square {
    private static final long serialVersionUID = 1L;
    
    
    public Square dest;
    public TerrainType newType;
    
    public SquarePlate(int x, int y, int height, TerrainType terrainType, Room room) {
        super(x, y, height, terrainType, room);
    }
    

    @Override
    public double getH() {
        return super.getH() + .2;
    }

    @Override
    public void stepOn() {
        dest.terrainType = newType;
    }
}
