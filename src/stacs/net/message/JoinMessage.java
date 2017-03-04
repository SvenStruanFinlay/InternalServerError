package stacs.net.message;

import stacs.logic.entity.PlayerEntity;
import stacs.logic.entity.ServerPlayerData;
import stacs.net.server.ServerClientThread;

public class JoinMessage extends Message {
    private static final long serialVersionUID = 1001343066105720976L;

    String name;
    
    public JoinMessage(String clientName){
        this.name = clientName;
    }
    
    @Override
    public void serverRecieved(ServerClientThread thread) {
        thread.player = new PlayerEntity(name);
        thread.player.serverData = new ServerPlayerData(thread, thread.player);
        
        thread.server.world.spawnPlayer(thread.player);
    }
}
