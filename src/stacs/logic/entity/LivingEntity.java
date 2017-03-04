package stacs.logic.entity;

import java.util.ArrayList;
import java.util.List;

import stacs.logic.item.Item;
import stacs.server.ServerWorld;

public abstract class LivingEntity extends Entity {
    public List<Item> inventory = new ArrayList<>();
    public int health;
    
    @Override
    public abstract void startNextTurn(ServerWorld world);
}
