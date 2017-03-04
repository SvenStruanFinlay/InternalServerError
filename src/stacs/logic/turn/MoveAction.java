package stacs.logic.turn;

import stacs.logic.entity.Entity;
import stacs.logic.room.Square;
import stacs.server.ServerWorld;

public class MoveAction extends NextTurnAction {
    private static final long serialVersionUID = -599756317507874288L;
    
    int rm, x, y;
    
    public MoveAction (int rm, int x, int y){
        this.rm = rm;
        this.x = x;
        this.y = y;
    }
    
    @Override
    public void execute(Entity e, ServerWorld world) {
       e.currentSquare.entities.remove(e);
       
       Square n = world.roomMap.get(rm).squares[x][y]; 
       n.entities.add(e);
       e.currentSquare = n;
       
       System.out.println(n);
    }

}
