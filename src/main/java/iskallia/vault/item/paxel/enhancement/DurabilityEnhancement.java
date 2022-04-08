package iskallia.vault.item.paxel.enhancement;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.Color;

public class DurabilityEnhancement
        extends PaxelEnhancement {
    public Color getColor() {
        return Color.fromRgb(-5888257);
    }

    protected int extraDurability;

    public DurabilityEnhancement(int extraDurability) {
        this.extraDurability = extraDurability;
    }

    public int getExtraDurability() {
        return this.extraDurability;
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        nbt.putInt("ExtraDurability", this.extraDurability);
        return nbt;
    }


    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        this.extraDurability = nbt.getInt("ExtraDurability");
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\paxel\enhancement\DurabilityEnhancement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */