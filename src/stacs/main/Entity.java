package stacs.main;


public abstract class Entity {

    public Room currentRoom;
    public Square currentSquare;
    
    public String getDisplayName() {
        return null;
    }
    
    public void startNextTurn(ServerWorld world) {
        world.nextActionMap.put(this, new NullAction());
    }
}
