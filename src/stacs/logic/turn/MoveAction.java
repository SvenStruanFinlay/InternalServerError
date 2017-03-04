package stacs.logic.turn;

import stacs.logic.entity.Entity;
import stacs.logic.room.Square;
import stacs.server.ServerWorld;

public class MoveAction extends NextTurnAction {
    private static final long serialVersionUID = -599756317507874288L;
    
    int rm, x, y;
    boolean teleport;
    
    public MoveAction (int rm, int x, int y, boolean teleport){
        this.rm = rm;
        this.x = x;
        this.y = y;
        this.teleport = teleport;
    }
    
    @Override
    public void execute(Entity e, ServerWorld world) {
       e.lastSquare = e.currentSquare;
       e.currentSquare.entities.remove(e);
       
       Square n = world.roomMap.get(rm).squares[x][y]; 
       n.entities.add(e);
       e.currentSquare = n;
       if(teleport)
           e.lastSquare = null;
       
       System.out.println(n);
    }

}
