package stacs.logic.turn;

import stacs.logic.entity.Entity;
import stacs.server.ServerWorld;

public abstract class NextTurnAction {
    public abstract void execute(Entity e, ServerWorld world);
}
