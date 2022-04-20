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

    protected AuraProvider(final UUID id, final RegistryKey<World> world) {
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
        return this.getAura().getRadius();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final AuraProvider that = (AuraProvider) o;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
