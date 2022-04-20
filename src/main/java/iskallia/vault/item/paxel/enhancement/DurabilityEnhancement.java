package iskallia.vault.item.paxel.enhancement;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.Color;

public class DurabilityEnhancement extends PaxelEnhancement {
    protected int extraDurability;

    @Override
    public Color getColor() {
        return Color.fromRgb(-5888257);
    }

    public DurabilityEnhancement(final int extraDurability) {
        this.extraDurability = extraDurability;
    }

    public int getExtraDurability() {
        return this.extraDurability;
    }

    @Override
    public CompoundNBT serializeNBT() {
        final CompoundNBT nbt = super.serializeNBT();
        nbt.putInt("ExtraDurability", this.extraDurability);
        return nbt;
    }

    @Override
    public void deserializeNBT(final CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        this.extraDurability = nbt.getInt("ExtraDurability");
    }
}
