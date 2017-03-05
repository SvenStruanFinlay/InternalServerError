package stacs.logic.turn;

import java.util.Random;

import stacs.logic.entity.Entity;
import stacs.logic.entity.LivingEntity;
import stacs.net.message.ChatMessage;
import stacs.server.ServerWorld;

public class AttackAction extends NextTurnAction {
    private static final long serialVersionUID = -1116119616467244837L;

    private int toAttackId;
    private String rm;
    private int x;
    private int y;
    
    public AttackAction(Entity e){
        this.toAttackId = e.id;
        this.rm = e.currentSquare.room.id;
        this.x = e.currentSquare.x;
        this.y = e.currentSquare.y;
    }
    
    @Override
    public void execute(Entity e, ServerWorld world) {
        Random rand = new Random();
        Entity a = world.allEntities.get(toAttackId);
        LivingEntity p = (LivingEntity) e;
        
//        if(rm.equals(a.currentSquare.room.id) && x == a.currentSquare.x && y == a.currentSquare.y){
            if(a instanceof LivingEntity){
                LivingEntity liv = (LivingEntity) a;
                int damage = rand.nextInt(p.getAttachStrength()) + 1;
                liv.hurt(damage);
                
                world.sendMessage(new ChatMessage(p.getDisplayName() + " attacks " + a.getDisplayName() + " (" + damage +"hp)"));
            }
//        }
    }

}
