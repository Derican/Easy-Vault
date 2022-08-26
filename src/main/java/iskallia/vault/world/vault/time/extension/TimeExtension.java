package iskallia.vault.world.vault.time.extension;

import iskallia.vault.Vault;
import iskallia.vault.world.vault.time.VaultTimer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public abstract class TimeExtension implements INBTSerializable<CompoundNBT> {
    public static final Map<ResourceLocation, Supplier<TimeExtension>> REGISTRY;
    protected ResourceLocation id;
    protected long extraTime;
    protected long executionTime;

    public TimeExtension() {
    }

    public TimeExtension(final ResourceLocation id, final long extraTime) {
        this.id = id;
        this.extraTime = extraTime;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public long getExtraTime() {
        return this.extraTime;
    }

    public long getExecutionTime() {
        return this.executionTime;
    }

    public void setExecutionTime(final long executionTime) {
        this.executionTime = executionTime;
    }

    public void apply(final VaultTimer timer) {
        timer.totalTime += (int) this.extraTime;
    }

    public CompoundNBT serializeNBT() {
        final CompoundNBT nbt = new CompoundNBT();
        nbt.putString("Id", this.getId().toString());
        nbt.putLong("ExtraTime", this.getExtraTime());
        nbt.putLong("ExecutionTime", this.getExecutionTime());
        return nbt;
    }

    public void deserializeNBT(final CompoundNBT nbt) {
        this.id = new ResourceLocation(nbt.getString("Id"));
        this.extraTime = nbt.getLong("ExtraTime");
        this.executionTime = nbt.getLong("ExecutionTime");
    }

    public static TimeExtension fromNBT(final CompoundNBT nbt) {
        final ResourceLocation id = new ResourceLocation(nbt.getString("Id"));
        final TimeExtension extension = TimeExtension.REGISTRY.getOrDefault(id, () -> null).get();
        if (extension == null) {
            Vault.LOGGER.error("Vault time extension <" + id + "> is not defined, using fallback.");
            return new FallbackExtension(nbt);
        }
        try {
            extension.deserializeNBT(nbt);
        } catch (final Exception e) {
            e.printStackTrace();
            Vault.LOGGER.error("Vault time extension <" + id + "> could not be deserialized, using fallback.");
            return new FallbackExtension(nbt);
        }
        return extension;
    }

    static {
        REGISTRY = new HashMap<ResourceLocation, Supplier<TimeExtension>>();
    }
}
