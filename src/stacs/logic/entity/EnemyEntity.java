package stacs.logic.entity;

import java.awt.Image;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import stacs.logic.room.Square;
import stacs.logic.room.TerrainType;
import stacs.logic.turn.AttackAction;
import stacs.logic.turn.MoveAction;
import stacs.logic.turn.NullAction;
import stacs.server.ServerWorld;
import stacs.util.PathFind;
import sun.dc.pr.PathFiller;

public class EnemyEntity extends LivingEntity {
    private static final long serialVersionUID = 2930657476704945395L;

    private LivingEntity target;

    public EnemyEntity(String name) {
        this.maxHealth = 5;
        this.health = 5;
    }

    @Override
    public String getDisplayName() {
        return "Enemy";
    }

    @Override
    public void startNextTurn(ServerWorld world) {
        LivingEntity found = null;
        Square targetSq = null;
        for (int x = 0; x < this.currentSquare.room.w; x++) {
            for (int y = 0; y < this.currentSquare.room.h; y++) {
                
                for(Entity e : this.currentSquare.room.squares[x][y].entities){
                    if(e instanceof PlayerEntity){
                        LivingEntity p = (LivingEntity) e;
                        
                        if(p == target || found == null){
                            found = p;
                            targetSq = this.currentSquare.room.squares[x][y];
                        }
                    }
                }
                
            }
        }
        
        target = found;
        
        if(target == null){
            world.nextActionMap.put(this, new NullAction());
            return;
        }
        
        List<Square> path = PathFind.findPath(this.currentSquare, targetSq, s -> {
                if(s.terrainType == TerrainType.water)
                    return s.room.freeze ? 1 : 10000;
                else
                    return 1;
            }, 1000);
        
        
        if(path != null){
            int x = Math.min(path.size() - 2, 4);
            if(x > 0){
                Square moveSq = path.get(x);
                world.nextActionMap.put(this, new MoveAction(moveSq, false));
                return;
            } else  {
                world.nextActionMap.put(this, new AttackAction(target));
                return;
            }
        }
        
        world.nextActionMap.put(this, new NullAction());
    }

    private static final Image img;

    static {
        try {
            img = ImageIO.read(PlayerEntity.class.getResourceAsStream("/player-saber.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getAttachStrength() {
        return 1;
    }

    @Override
    public Image getSprite(int tick) {
        return img;
    }
}
