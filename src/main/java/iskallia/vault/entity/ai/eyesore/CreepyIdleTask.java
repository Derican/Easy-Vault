package iskallia.vault.entity.ai.eyesore;

import net.minecraft.network.datasync.DataParameter;
import java.util.function.Supplier;
import net.minecraftforge.fml.network.PacketDistributor;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.EnteredEyesoreDomainMessage;
import net.minecraft.entity.LivingEntity;
import iskallia.vault.entity.EyesoreEntity;

public class CreepyIdleTask extends EyesoreTask<EyesoreEntity>
{
    public int tick;
    public boolean finished;
    
    public CreepyIdleTask(final EyesoreEntity entity) {
        super(entity);
        this.tick = 0;
        this.finished = false;
    }
    
    @Override
    public void tick() {
        if (this.isFinished()) {
            return;
        }
        if (this.tick == 40) {
            final EnteredEyesoreDomainMessage packet = new EnteredEyesoreDomainMessage();
            ModNetwork.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with((Supplier)this::getEntity), (Object)packet);
        }
        this.getEntity().getEntityData().set((DataParameter)EyesoreEntity.WATCH_CLIENT, (Object)true);
        ++this.tick;
        if (this.tick >= 300 || this.getEntity().getLastDamageSource() != null) {
            this.getEntity().getEntityData().set((DataParameter)EyesoreEntity.WATCH_CLIENT, (Object)false);
            this.finished = true;
        }
    }
    
    @Override
    public boolean isFinished() {
        return this.finished;
    }
    
    @Override
    public void reset() {
        this.tick = 0;
        this.finished = false;
    }
}
