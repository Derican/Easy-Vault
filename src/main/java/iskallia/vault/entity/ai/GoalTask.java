// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.entity.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.world.World;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Random;

public abstract class GoalTask<T extends LivingEntity> extends Goal
{
    private final T entity;
    
    public GoalTask(final T entity) {
        this.entity = entity;
    }
    
    public T getEntity() {
        return this.entity;
    }
    
    public World getWorld() {
        return this.getEntity().level;
    }
    
    public Random getRandom() {
        return (Random)ObjectUtils.firstNonNull((Object[])new Random[] { this.getWorld().getRandom(), this.getEntity().getRandom() });
    }
}
