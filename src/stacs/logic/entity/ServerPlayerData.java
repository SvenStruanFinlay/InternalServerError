package stacs.logic.entity;

import java.io.IOException;

import stacs.net.server.ServerClientThread;
import stacs.server.ServerWorld;

public class ServerPlayerData {
    public ServerClientThread thread;
    public PlayerEntity player;
    
    public ServerPlayerData(ServerClientThread thread, PlayerEntity ent) {
        this.thread = thread;
        this.player = ent;
    }
    
    public void startNextTurn(ServerWorld world) {
        try {
            thread.doUpdate(player.currentSquare.room);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
