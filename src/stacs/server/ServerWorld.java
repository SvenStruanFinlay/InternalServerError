package stacs.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import stacs.logic.entity.Entity;
import stacs.logic.entity.PlayerEntity;
import stacs.logic.room.Room;
import stacs.logic.room.Square;
import stacs.logic.turn.NextTurnAction;

public class ServerWorld {
    public final Map<Integer, Room> roomMap = new HashMap<>();
    public final Map<Entity, NextTurnAction> nextActionMap = new ConcurrentHashMap();
    
    public HashSet<Entity> allEntities = new HashSet<>();
    public HashSet<Entity> toGetTurnEntities = new HashSet<>();
    
    public boolean turnStarted = false;

    public synchronized void startTurn() {
        for (Entity entry : allEntities) {
            entry.startNextTurn(this);
        }
        turnStarted = true;
        
        toGetTurnEntities.clear();
        toGetTurnEntities.addAll(allEntities);
    }
    
    private void update() {
        for(Entity e: allEntities) {
            if(e instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) e;
                player.serverData.updateWorld(this, false);
            }
        }
    }

    public synchronized void runCommand(String command){
        if(command.equals("rain")){
            for(Room room : roomMap.values()){
                room.rain = true;
            }
            update();
        }
        
        if(command.equals("thaw")){
            for(Room room : roomMap.values()){
                room.rain = false;
            }
            update();
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

    public synchronized void spawnPlayer(PlayerEntity e) {
        allEntities.add(e);
        Room rm = roomMap.values().iterator().next();
        
        Random rand = new Random();
        int x = rand.nextInt(rm.w);
        int y = rand.nextInt(rm.h);
        
        Square s = rm.squares[x][y]; 
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
