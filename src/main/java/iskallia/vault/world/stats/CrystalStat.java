package iskallia.vault.world.stats;

import iskallia.vault.altar.RequiredItem;
import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.nbt.VListNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;

public class CrystalStat implements INBTSerializable<CompoundNBT> {
    private CrystalData.Type type;
    public VListNBT<RequiredItem, CompoundNBT> recipe;
    public long time;

    public CrystalStat() {
        this.recipe = new VListNBT<RequiredItem, CompoundNBT>(RequiredItem::serializeNBT, RequiredItem::deserializeNBT);
    }

    public CrystalStat(final List<RequiredItem> recipe, final CrystalData.Type type) {
        this.recipe = new VListNBT<RequiredItem, CompoundNBT>(RequiredItem::serializeNBT, RequiredItem::deserializeNBT);
        this.type = type;
        recipe.forEach(item -> this.recipe.add(item.copy()));
        this.time = System.currentTimeMillis();
    }

    public CompoundNBT serializeNBT() {
        final CompoundNBT nbt = new CompoundNBT();
        nbt.putString("Type", this.type.name());
        nbt.put("Recipe", (INBT) this.recipe.serializeNBT());
        nbt.putLong("Time", this.time);
        return nbt;
    }

    public void deserializeNBT(final CompoundNBT nbt) {
        this.type = (nbt.contains("Type", 8) ? Enum.valueOf(CrystalData.Type.class, nbt.getString("Type")) : CrystalData.Type.CLASSIC);
        this.recipe.deserializeNBT(nbt.getList("Recipe", 10));
        this.time = nbt.getLong("Time");
    }
}
