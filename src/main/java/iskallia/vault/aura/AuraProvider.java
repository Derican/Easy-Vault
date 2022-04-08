package iskallia.vault.aura;

import iskallia.vault.config.EternalAuraConfig;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.UUID;


public abstract class AuraProvider {
    private final UUID id;
    private final RegistryKey<World> world;

    protected AuraProvider(UUID id, RegistryKey<World> world) {
        this.id = id;
        this.world = world;
    }

    public final RegistryKey<World> getWorld() {
        return this.world;
    }

    public final UUID getId() {
        return this.id;
    }

    public abstract boolean isValid();

    public abstract Vector3d getLocation();

    public abstract EternalAuraConfig.AuraConfig getAura();

    public float getRadius() {
        return getAura().getRadius();
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuraProvider that = (AuraProvider) o;
        return Objects.equals(this.id, that.id);
    }


    public int hashCode() {
        return Objects.hash(new Object[]{this.id});
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\aura\AuraProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */