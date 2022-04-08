package iskallia.vault.world.stats;

import iskallia.vault.altar.RequiredItem;
import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.nbt.VListNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;

public class CrystalStat
        implements INBTSerializable<CompoundNBT> {
    private CrystalData.Type type;
    public VListNBT<RequiredItem, CompoundNBT> recipe = new VListNBT(RequiredItem::serializeNBT, RequiredItem::deserializeNBT);


    public long time;


    public CrystalStat(List<RequiredItem> recipe, CrystalData.Type type) {
        this.type = type;
        recipe.forEach(item -> this.recipe.add(item.copy()));
        this.time = System.currentTimeMillis();
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("Type", this.type.name());
        nbt.put("Recipe", (INBT) this.recipe.serializeNBT());
        nbt.putLong("Time", this.time);
        return nbt;
    }


    public void deserializeNBT(CompoundNBT nbt) {
        this.type = nbt.contains("Type", 8) ? Enum.<CrystalData.Type>valueOf(CrystalData.Type.class, nbt.getString("Type")) : CrystalData.Type.CLASSIC;
        this.recipe.deserializeNBT(nbt.getList("Recipe", 10));
        this.time = nbt.getLong("Time");
    }

    public CrystalStat() {
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\stats\CrystalStat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */