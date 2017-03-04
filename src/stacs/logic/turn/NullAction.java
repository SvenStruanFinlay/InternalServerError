package stacs.logic.turn;

import stacs.logic.entity.Entity;
import stacs.server.ServerWorld;

public class NullAction extends NextTurnAction {

    @Override
    public void execute(Entity e, ServerWorld world) {
    }

}
