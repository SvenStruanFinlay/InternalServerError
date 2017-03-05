package stacs.logic.turn;

import stacs.logic.entity.Entity;
import stacs.logic.room.Square;
import stacs.server.ServerWorld;

public class MoveAction extends NextTurnAction {
    private static final long serialVersionUID = -599756317507874288L;
    
    String rm;
    int x, y;
    boolean teleport;
    
    public MoveAction (Square sq, boolean teleport){
        this.rm = sq.room.id;
        this.x = sq.x;
        this.y = sq.y;
        this.teleport = teleport;
    }
    
    @Override
    public void execute(Entity e, ServerWorld world) {
       e.currentSquare.entities.remove(e);
       
       Square n = world.roomMap.get(rm).squares[x][y]; 
       n.entities.add(e);
       e.currentSquare = n;
       e.interpolate = !teleport;
       
       System.out.println(n);
    }

}
