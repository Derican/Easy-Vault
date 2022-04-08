package iskallia.vault.aura;

import iskallia.vault.config.EternalAuraConfig;
import net.minecraft.entity.Entity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;


public class ActiveAura {
    private final AuraProvider auraProvider;
    private RegistryKey<World> worldKey;
    private Vector3d offset;
    private float radius;
    private float radiusSq;

    public ActiveAura(AuraProvider auraProvider) {
        this.auraProvider = auraProvider;
        updateFromProvider();
    }

    public void updateFromProvider() {
        this.worldKey = this.auraProvider.getWorld();
        this.offset = this.auraProvider.getLocation();
        this.radius = this.auraProvider.getRadius();
        this.radiusSq = this.radius * this.radius;
    }

    public boolean canPersist() {
        return this.auraProvider.isValid();
    }

    public boolean isAffected(Entity entity) {
        RegistryKey<World> entityWorld = entity.getCommandSenderWorld().dimension();
        if (!this.worldKey.equals(entityWorld)) {
            return false;
        }
        Vector3d pos = entity.position();
        return (this.offset.distanceToSqr(pos) < this.radiusSq);
    }

    public RegistryKey<World> getWorldKey() {
        return this.worldKey;
    }

    public Vector3d getOffset() {
        return this.offset;
    }

    public float getRadius() {
        return this.radius;
    }

    public EternalAuraConfig.AuraConfig getAura() {
        return this.auraProvider.getAura();
    }

    public AuraProvider getAuraProvider() {
        return this.auraProvider;
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActiveAura that = (ActiveAura) o;
        return this.auraProvider.equals(that.auraProvider);
    }


    public int hashCode() {
        return this.auraProvider.hashCode();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\aura\ActiveAura.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */