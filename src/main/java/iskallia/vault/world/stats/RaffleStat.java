package iskallia.vault.world.stats;

import iskallia.vault.util.data.WeightedList;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class RaffleStat
        implements INBTSerializable<CompoundNBT> {
    private WeightedList<String> contributors = new WeightedList();
    private String winner = "";


    public RaffleStat(WeightedList<String> contributors, String winner) {
        this.contributors = contributors.copy();
        this.winner = winner;
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();

        ListNBT contributorsList = new ListNBT();

        this.contributors.forEach(entry -> {
            CompoundNBT tag = new CompoundNBT();

            tag.putString("Value", (String) entry.value);
            tag.putInt("Weight", entry.weight);
            contributorsList.add(tag);
        });
        nbt.put("Contributors", (INBT) contributorsList);

        if (this.winner != null) {
            nbt.putString("Winner", this.winner);
        }

        return nbt;
    }


    public void deserializeNBT(CompoundNBT nbt) {
        this.contributors.clear();
        ListNBT contributorsList = nbt.getList("Contributors", 9);

        contributorsList.stream().map(inbt -> (CompoundNBT) inbt).forEach(tag -> this.contributors.add(tag.getString("Value"), tag.getInt("Weight")));


        this.winner = nbt.getString("Winner");
    }

    public RaffleStat() {
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\stats\RaffleStat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */