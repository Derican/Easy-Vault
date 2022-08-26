package iskallia.vault.aura;

import iskallia.vault.config.EternalAuraConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class EntityAuraProvider extends AuraProvider {
    private final EternalAuraConfig.AuraConfig aura;
    private final LivingEntity entity;

    protected EntityAuraProvider(final LivingEntity entity, final EternalAuraConfig.AuraConfig aura) {
        super(entity.getUUID(), entity.getCommandSenderWorld().dimension());
        this.aura = aura;
        this.entity = entity;
    }

    public static EntityAuraProvider ofEntity(final LivingEntity entity, final EternalAuraConfig.AuraConfig aura) {
        return new EntityAuraProvider(entity, aura);
    }

    public LivingEntity getSource() {
        return this.entity;
    }

    @Override
    public boolean isValid() {
        return this.entity.isAlive();
    }

    @Override
    public Vector3d getLocation() {
        return new Vector3d(this.entity.getX(), this.entity.getY(), this.entity.getZ());
    }

    @Override
    public EternalAuraConfig.AuraConfig getAura() {
        return this.aura;
    }
}
