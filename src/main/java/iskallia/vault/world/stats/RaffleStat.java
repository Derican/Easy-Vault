// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.stats;

import iskallia.vault.util.data.WeightedList;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class RaffleStat implements INBTSerializable<CompoundNBT>
{
    private WeightedList<String> contributors;
    private String winner;
    
    public RaffleStat() {
        this.contributors = new WeightedList<String>();
        this.winner = "";
    }
    
    public RaffleStat(final WeightedList<String> contributors, final String winner) {
        this.contributors = new WeightedList<String>();
        this.winner = "";
        this.contributors = contributors.copy();
        this.winner = winner;
    }
    
    public CompoundNBT serializeNBT() {
        final CompoundNBT nbt = new CompoundNBT();
        final ListNBT contributorsList = new ListNBT();
        this.contributors.forEach(entry -> {
            final CompoundNBT tag = new CompoundNBT();
            tag.putString("Value", (String)entry.value);
            tag.putInt("Weight", entry.weight);
            contributorsList.add(tag);
            return;
        });
        nbt.put("Contributors", (INBT)contributorsList);
        if (this.winner != null) {
            nbt.putString("Winner", this.winner);
        }
        return nbt;
    }
    
    public void deserializeNBT(final CompoundNBT nbt) {
        this.contributors.clear();
        final ListNBT contributorsList = nbt.getList("Contributors", 9);
        contributorsList.stream().map(inbt -> (CompoundNBT)inbt).forEach(tag -> this.contributors.add(tag.getString("Value"), tag.getInt("Weight")));
        this.winner = nbt.getString("Winner");
    }
}
