package stacs.logic.turn;

import java.util.List;

import stacs.logic.entity.Entity;
import stacs.logic.entity.LivingEntity;
import stacs.logic.room.Square;
import stacs.server.ServerWorld;
import stacs.util.PathFind;

public class MoveAction extends NextTurnAction {
    private static final long serialVersionUID = -599756317507874288L;

    String rm;
    int x, y;
    boolean teleport;

    public MoveAction(Square sq, boolean teleport) {
        this.rm = sq.room.id;
        this.x = sq.x;
        this.y = sq.y;
        this.teleport = teleport;
    }

    @Override
    public void execute(Entity in, ServerWorld world) {
        LivingEntity e = (LivingEntity) in;
        e.currentSquare.entities.remove(e);
        Square n = world.roomMap.get(rm).squares[x][y];
        
        List<Square> path = PathFind.findPath(e.currentSquare, n, s -> {
            if (!e.canNavigate(s))
                return 10000;
            return 1;
        }, 1000);
        
        for(Square s : path){
            s.stepOn();
        }
        
        n.entities.add(e);
        e.currentSquare = n;
        e.interpolate = !teleport;

        System.out.println(n);
    }

}
