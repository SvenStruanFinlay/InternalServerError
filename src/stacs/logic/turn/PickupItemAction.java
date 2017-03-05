package stacs.logic.turn;

import java.util.Iterator;

import stacs.logic.entity.Entity;
import stacs.logic.entity.PlayerEntity;
import stacs.logic.item.Item;
import stacs.logic.room.Square;
import stacs.net.message.ChatMessage;
import stacs.server.ServerWorld;

public class PickupItemAction extends NextTurnAction {
    private static final long serialVersionUID = -7625125411240861042L;

    String rm;
    int x;
    int y;
    int itemid;
    
    public PickupItemAction(Square square, Item item) {
        rm = square.room.id;
        x = square.x;
        y = square.y;
        itemid = item.id;
    }
    
    @Override
    public void execute(Entity e, ServerWorld world) {
        PlayerEntity p = (PlayerEntity) e;
        
        Iterator<Item> itemIterator = world.roomMap.get(rm).squares[x][y].items.iterator();
        while(itemIterator.hasNext()){
            Item it = itemIterator.next();
            
            if(it.id == itemid){
                itemIterator.remove();
                p.inventory.add(it);
                p.serverData.sendMessage(new ChatMessage("You picked up " + it.getName()));
                break;
            }
        }
    }

}
