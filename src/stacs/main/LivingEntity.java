package stacs.main;

import java.util.ArrayList;
import java.util.List;

public abstract class LivingEntity extends Entity {
    public List<Item> inventory = new ArrayList<>();
    public int health;
    
    @Override
    public abstract void startNextTurn(ServerWorld world);
}
