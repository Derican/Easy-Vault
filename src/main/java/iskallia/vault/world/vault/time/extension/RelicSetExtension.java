package iskallia.vault.world.vault.time.extension;

import iskallia.vault.Vault;
import iskallia.vault.util.RelicSet;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

public class RelicSetExtension extends TimeExtension {
    public static final ResourceLocation ID = Vault.id("relic_set");

    protected RelicSet relicSet;

    public RelicSetExtension() {
    }

    public RelicSetExtension(RelicSet relicSet, long extraTime) {
        this(ID, relicSet, extraTime);
    }

    public RelicSetExtension(ResourceLocation id, RelicSet relicSet, long extraTime) {
        super(id, extraTime);
        this.relicSet = relicSet;
    }

    public RelicSet getRelicSet() {
        return this.relicSet;
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        nbt.putString("RelicSet", getRelicSet().getId().toString());
        return nbt;
    }


    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        this.relicSet = (RelicSet) RelicSet.REGISTRY.get(new ResourceLocation(nbt.getString("RelicSet")));

        if (this.relicSet == null) {
            Vault.LOGGER.error("Relic set <" + nbt.getString("RelicSet") + "> is not defined.");
            throw new IllegalStateException();
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\time\extension\RelicSetExtension.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */