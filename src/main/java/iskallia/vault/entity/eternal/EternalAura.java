package iskallia.vault.entity.eternal;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class EternalAura implements INBTSerializable<CompoundNBT> {
    private String auraName;

    public EternalAura(String auraName) {
        this.auraName = auraName;
    }

    public EternalAura(CompoundNBT tag) {
        deserializeNBT(tag);
    }

    public String getAuraName() {
        return this.auraName;
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.putString("auraName", this.auraName);
        return tag;
    }


    public void deserializeNBT(CompoundNBT tag) {
        this.auraName = tag.getString("auraName");
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\entity\eternal\EternalAura.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */