package iskallia.vault.world.vault.time.extension;

import iskallia.vault.Vault;
import iskallia.vault.world.vault.time.VaultTimer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public abstract class TimeExtension
        implements INBTSerializable<CompoundNBT> {
    public static final Map<ResourceLocation, Supplier<TimeExtension>> REGISTRY = new HashMap<>();

    protected ResourceLocation id;

    protected long extraTime;

    protected long executionTime;

    public TimeExtension() {
    }

    public TimeExtension(ResourceLocation id, long extraTime) {
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

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }

    public void apply(VaultTimer timer) {
        timer.totalTime = (int) (timer.totalTime + this.extraTime);
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("Id", getId().toString());
        nbt.putLong("ExtraTime", getExtraTime());
        nbt.putLong("ExecutionTime", getExecutionTime());
        return nbt;
    }


    public void deserializeNBT(CompoundNBT nbt) {
        this.id = new ResourceLocation(nbt.getString("Id"));
        this.extraTime = nbt.getLong("ExtraTime");
        this.executionTime = nbt.getLong("ExecutionTime");
    }

    public static TimeExtension fromNBT(CompoundNBT nbt) {
        ResourceLocation id = new ResourceLocation(nbt.getString("Id"));
        TimeExtension extension = ((Supplier<TimeExtension>) REGISTRY.getOrDefault(id, () -> null)).get();

        if (extension == null) {
            Vault.LOGGER.error("Vault time extension <" + id.toString() + "> is not defined, using fallback.");
            return new FallbackExtension(nbt);
        }

        try {
            extension.deserializeNBT(nbt);
        } catch (Exception e) {
            e.printStackTrace();
            Vault.LOGGER.error("Vault time extension <" + id.toString() + "> could not be deserialized, using fallback.");
            return new FallbackExtension(nbt);
        }

        return extension;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\time\extension\TimeExtension.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */