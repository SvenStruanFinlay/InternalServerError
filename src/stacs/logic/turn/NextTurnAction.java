package stacs.logic.turn;

import java.io.Serializable;

import stacs.logic.entity.Entity;
import stacs.server.ServerWorld;

public abstract class NextTurnAction implements Serializable {
    private static final long serialVersionUID = 5713963241236597584L;

    public abstract void execute(Entity e, ServerWorld world);
}
