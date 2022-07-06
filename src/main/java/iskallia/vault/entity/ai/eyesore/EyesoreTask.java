package iskallia.vault.entity.ai.eyesore;

import org.apache.commons.lang3.ObjectUtils;
import java.util.Random;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.entity.LivingEntity;

public abstract class EyesoreTask<T extends LivingEntity>
{
    private final T entity;
    
    public EyesoreTask(final T entity) {
        this.entity = entity;
    }
    
    public T getEntity() {
        return this.entity;
    }
    
    public ServerWorld getWorld() {
        return (ServerWorld)this.getEntity().level;
    }
    
    public VaultRaid getVault() {
        return VaultRaidData.get(this.getWorld()).getAt(this.getWorld(), this.getEntity().blockPosition());
    }
    
    public Random getRandom() {
        return (Random)ObjectUtils.firstNonNull((Object[])new Random[] { this.getWorld().getRandom(), this.getEntity().getRandom() });
    }
    
    public abstract void tick();
    
    public abstract boolean isFinished();
    
    public abstract void reset();
}
