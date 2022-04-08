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

public class TauntAuraConfig
        extends EternalAuraConfig.AuraConfig {
    public TauntAuraConfig(int tauntInterval) {
        super("Taunt", "Taunt", "Periodically taunts enemies nearby", "taunt", 8.0F);
        this.tauntInterval = tauntInterval;
    }

    @Expose
    private final int tauntInterval;

    public void onTick(World world, ActiveAura aura) {
        super.onTick(world, aura);

        if (!(aura.getAuraProvider() instanceof EntityAuraProvider)) {
            return;
        }
        if (world.getGameTime() % this.tauntInterval != 0L) {
            return;
        }
        LivingEntity auraProvider = ((EntityAuraProvider) aura.getAuraProvider()).getSource();
        EntityHelper.getNearby((IWorld) world, (Vector3i) new BlockPos(aura.getOffset()), aura.getRadius(), MobEntity.class).forEach(mob -> mob.setTarget(auraProvider));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\aura\type\TauntAuraConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */