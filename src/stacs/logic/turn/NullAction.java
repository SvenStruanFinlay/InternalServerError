package stacs.logic.turn;

import stacs.logic.entity.Entity;
import stacs.server.ServerWorld;

public class NullAction extends NextTurnAction {
    private static final long serialVersionUID = -665053210237187509L;

    @Override
    public void execute(Entity e, ServerWorld world) {
    }

}
