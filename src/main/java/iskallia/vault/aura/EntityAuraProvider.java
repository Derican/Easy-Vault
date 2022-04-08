package iskallia.vault.aura;

import iskallia.vault.config.EternalAuraConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3d;


public class EntityAuraProvider
        extends AuraProvider {
    private final EternalAuraConfig.AuraConfig aura;
    private final LivingEntity entity;

    protected EntityAuraProvider(LivingEntity entity, EternalAuraConfig.AuraConfig aura) {
        super(entity.getUUID(), entity.getCommandSenderWorld().dimension());
        this.aura = aura;
        this.entity = entity;
    }

    public static EntityAuraProvider ofEntity(LivingEntity entity, EternalAuraConfig.AuraConfig aura) {
        return new EntityAuraProvider(entity, aura);
    }

    public LivingEntity getSource() {
        return this.entity;
    }


    public boolean isValid() {
        return this.entity.isAlive();
    }


    public Vector3d getLocation() {
        return new Vector3d(this.entity.getX(), this.entity.getY(), this.entity.getZ());
    }


    public EternalAuraConfig.AuraConfig getAura() {
        return this.aura;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\aura\EntityAuraProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */