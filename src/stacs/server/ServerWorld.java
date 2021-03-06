package stacs.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import stacs.logic.entity.Entity;
import stacs.logic.entity.LivingEntity;
import stacs.logic.entity.PlayerEntity;
import stacs.logic.room.Room;
import stacs.logic.room.Square;
import stacs.logic.turn.NextTurnAction;
import stacs.net.message.ChatMessage;

public class ServerWorld {
    public final Map<String, Room> roomMap = new HashMap<>();
    public final Map<Entity, NextTurnAction> nextActionMap = new ConcurrentHashMap();

    public HashMap<Integer, Entity> allEntities = new HashMap<>();
    public HashSet<Entity> toGetTurnEntities = new HashSet<>();

    public boolean turnStarted = false;

    public synchronized void startTurn() {
        for (Entity entry : allEntities.values()) {
            entry.startNextTurn(this);
        }
        turnStarted = true;

        toGetTurnEntities.clear();
        toGetTurnEntities.addAll(allEntities.values());
    }

    private void update() {
        for (Entity e : allEntities.values()) {
            if (e instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) e;
                player.serverData.updateWorld(this, false);
            }
        }
    }

    public synchronized void sendMessage(ChatMessage message) {
        for (Entity e : allEntities.values()) {
            if (e instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) e;
                player.serverData.sendMessage(message);
            }
        }
    }

    public synchronized void remove(Entity e) {
        e.currentSquare.entities.remove(e);
        allEntities.remove(e.id);
        toGetTurnEntities.remove(e);
        nextActionMap.remove(e);
    }

    public synchronized void runCommand(String command) {
        if (command.equals("freeze")) {
            for (Room room : roomMap.values()) {
                room.freeze = true;
            }
            update();
        }

        if (command.equals("thaw")) {
            for (Room room : roomMap.values()) {
                room.freeze = false;
            }
            update();
        }

        if (command.equals("lock")) {
        }

        if (command.equals("darkness")) {
        }

        if (command.equals("confuse")) {
        }
        
        if (command.equals("swapdr")) {
        }
    }

    public synchronized void attemptTurn() {
        if (!turnStarted)
            startTurn();

        if (nextActionMap.keySet().containsAll(toGetTurnEntities)) {
            // ready to do next turn

            for (Entry<Entity, NextTurnAction> entry : nextActionMap.entrySet()) {
                entry.getValue().execute(entry.getKey(), this);
            }

            nextActionMap.clear();
            turnStarted = false;
        }
    }

    public synchronized void addEntity(Entity e, Square s) {
        allEntities.put(e.id, e);
        s.entities.add(e);
        e.currentSquare = s;
    }

    public synchronized void spawnPlayer(LivingEntity e) {
        allEntities.put(e.id, e);
        Room rm = roomMap.get("roomMaze");

        Square s = rm.squares[0][0];
        s.entities.add(e);
        e.currentSquare = s;
        update();
    }

    public void runServerLoop() {
        while (true) {

            attemptTurn();

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
            }
        }
    }
}
