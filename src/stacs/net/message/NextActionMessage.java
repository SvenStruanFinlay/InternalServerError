package stacs.net.message;

import stacs.logic.turn.NextTurnAction;
import stacs.net.server.ServerClientThread;

public class NextActionMessage extends Message {
    private static final long serialVersionUID = 8517519373411912049L;

    NextTurnAction action;
    
    public NextActionMessage(NextTurnAction action) {
        this.action = action;
    }
    
    @Override
    public void serverRecieved(ServerClientThread thread) {
        thread.server.world.nextActionMap.put(thread.player, action);
    }
}
