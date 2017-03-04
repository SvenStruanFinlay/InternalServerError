package stacs.main;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class ServerWorld {
    public final Map<Integer, Room> roomMap = new HashMap<>();
    public final Map<Entity, NextTurnAction> nextActionMap = new ConcurrentHashMap();
    public HashSet<Entity> allEntities = new HashSet<>();
    public boolean turnStarted = false;

    public void startTurn() {
        for (Entity entry : allEntities) {
            entry.startNextTurn(this);
        }
        turnStarted = true;
    }

    public void runServerLoop() {
        while (true) {

            if(!turnStarted)
                startTurn();
            
            if (nextActionMap.keySet().containsAll(allEntities)) {
                // ready to do next turn
                
                for (Entry<Entity, NextTurnAction> entry : nextActionMap.entrySet()) {
                    entry.getValue().execute(entry.getKey(), this);
                }
                 
                nextActionMap.clear();
                turnStarted = false;
            }

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
            }
        }
    }
}
