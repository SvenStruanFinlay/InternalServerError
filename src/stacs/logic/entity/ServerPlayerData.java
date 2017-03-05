package stacs.logic.entity;

import java.io.IOException;

import stacs.net.message.ChatMessage;
import stacs.net.server.ServerClientThread;
import stacs.server.ServerWorld;

public class ServerPlayerData {
    public ServerClientThread thread;
    public LivingEntity player;

    public ServerPlayerData(ServerClientThread thread, LivingEntity ent) {
        this.thread = thread;
        this.player = ent;
    }

    public void updateWorld(ServerWorld world, boolean turn) {
        try {
            thread.doUpdate(player.currentSquare.room, turn);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(ChatMessage message) {
        try {
            thread.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
