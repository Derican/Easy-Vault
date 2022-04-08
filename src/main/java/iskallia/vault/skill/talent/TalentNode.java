package iskallia.vault.skill.talent;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.skill.talent.type.PlayerTalent;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.UUID;

public class TalentNode<T extends PlayerTalent>
        implements INBTSerializable<CompoundNBT> {
    private TalentGroup<T> group;
    private int level;

    public TalentNode(TalentGroup<T> group, int level) {
        this.group = group;
        this.level = MathHelper.clamp(level, 0, group.getMaxLevel());
    }

    public TalentGroup<T> getGroup() {
        return this.group;
    }

    public int getLevel() {
        return this.level;
    }

    public T getTalent() {
        if (!isLearned()) return null;
        return getGroup().getTalent(getLevel());
    }

    public String getName() {
        return getGroup().getName(getLevel());
    }

    public boolean isLearned() {
        return (getLevel() > 0);
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("Name", getGroup().getParentName());
        nbt.putInt("Level", getLevel());
        return nbt;
    }


    public void deserializeNBT(CompoundNBT nbt) {
        String groupName = nbt.getString("Name");
        this.group = ModConfigs.TALENTS.getByName(groupName);
        this.level = nbt.getInt("Level");
    }


    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        TalentNode<?> that = (TalentNode) other;

        return (this.level == that.level && this.group
                .getParentName().equals(that.group.getParentName()));
    }


    @Nullable
    public static TalentNode<?> fromNBT(@Nullable UUID playerId, CompoundNBT nbt, int currentVersion) {
        String talentName = nbt.getString("Name");
        int level = nbt.getInt("Level");
        return TalentTree.migrate(playerId, talentName, level, currentVersion);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\talent\TalentNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */