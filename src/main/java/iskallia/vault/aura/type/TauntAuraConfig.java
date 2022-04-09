// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.aura.type;

import com.google.gson.annotations.Expose;
import iskallia.vault.aura.ActiveAura;
import iskallia.vault.aura.EntityAuraProvider;
import iskallia.vault.config.EternalAuraConfig;
import iskallia.vault.util.EntityHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class TauntAuraConfig extends EternalAuraConfig.AuraConfig
{
    @Expose
    private final int tauntInterval;
    
    public TauntAuraConfig(final int tauntInterval) {
        super("Taunt", "Taunt", "Periodically taunts enemies nearby", "taunt", 8.0f);
        this.tauntInterval = tauntInterval;
    }
    
    @Override
    public void onTick(final World world, final ActiveAura aura) {
        super.onTick(world, aura);
        if (!(aura.getAuraProvider() instanceof EntityAuraProvider)) {
            return;
        }
        if (world.getGameTime() % this.tauntInterval != 0L) {
            return;
        }
        final LivingEntity auraProvider = ((EntityAuraProvider)aura.getAuraProvider()).getSource();
        EntityHelper.getNearby((IWorld)world, (Vector3i)new BlockPos(aura.getOffset()), aura.getRadius(), MobEntity.class).forEach(mob -> mob.setTarget(auraProvider));
    }
}
