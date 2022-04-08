package iskallia.vault.world.vault.time.extension;

import iskallia.vault.Vault;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

public class FallbackExtension extends TimeExtension {
    public static final ResourceLocation ID = Vault.id("fallback");

    protected CompoundNBT fallback;

    public FallbackExtension() {
    }

    public FallbackExtension(CompoundNBT fallback) {
        super(ID, 0L);
        deserializeNBT(fallback);
    }

    public CompoundNBT getFallback() {
        return this.fallback;
    }


    public CompoundNBT serializeNBT() {
        return this.fallback;
    }


    public void deserializeNBT(CompoundNBT nbt) {
        this.fallback = nbt;
        this.extraTime = getFallback().getLong("ExtraTime");
        this.executionTime = getFallback().getLong("ExecutionTime");
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\time\extension\FallbackExtension.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */