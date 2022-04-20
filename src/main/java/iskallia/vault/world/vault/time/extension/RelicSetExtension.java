package iskallia.vault.world.vault.time.extension;

import iskallia.vault.Vault;
import iskallia.vault.util.RelicSet;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

public class RelicSetExtension extends TimeExtension {
    public static final ResourceLocation ID;
    protected RelicSet relicSet;

    public RelicSetExtension() {
    }

    public RelicSetExtension(final RelicSet relicSet, final long extraTime) {
        this(RelicSetExtension.ID, relicSet, extraTime);
    }

    public RelicSetExtension(final ResourceLocation id, final RelicSet relicSet, final long extraTime) {
        super(id, extraTime);
        this.relicSet = relicSet;
    }

    public RelicSet getRelicSet() {
        return this.relicSet;
    }

    @Override
    public CompoundNBT serializeNBT() {
        final CompoundNBT nbt = super.serializeNBT();
        nbt.putString("RelicSet", this.getRelicSet().getId().toString());
        return nbt;
    }

    @Override
    public void deserializeNBT(final CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        this.relicSet = RelicSet.REGISTRY.get(new ResourceLocation(nbt.getString("RelicSet")));
        if (this.relicSet == null) {
            Vault.LOGGER.error("Relic set <" + nbt.getString("RelicSet") + "> is not defined.");
            throw new IllegalStateException();
        }
    }

    static {
        ID = Vault.id("relic_set");
    }
}
