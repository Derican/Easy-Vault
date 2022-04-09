// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.talent;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.skill.talent.type.PlayerTalent;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.UUID;

public class TalentNode<T extends PlayerTalent> implements INBTSerializable<CompoundNBT>
{
    private TalentGroup<T> group;
    private int level;
    
    public TalentNode(final TalentGroup<T> group, final int level) {
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
        if (!this.isLearned()) {
            return null;
        }
        return this.getGroup().getTalent(this.getLevel());
    }
    
    public String getName() {
        return this.getGroup().getName(this.getLevel());
    }
    
    public boolean isLearned() {
        return this.getLevel() > 0;
    }
    
    public CompoundNBT serializeNBT() {
        final CompoundNBT nbt = new CompoundNBT();
        nbt.putString("Name", this.getGroup().getParentName());
        nbt.putInt("Level", this.getLevel());
        return nbt;
    }
    
    public void deserializeNBT(final CompoundNBT nbt) {
        final String groupName = nbt.getString("Name");
        this.group = (TalentGroup<T>)ModConfigs.TALENTS.getByName(groupName);
        this.level = nbt.getInt("Level");
    }
    
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        final TalentNode<?> that = (TalentNode<?>)other;
        return this.level == that.level && this.group.getParentName().equals(that.group.getParentName());
    }
    
    @Nullable
    public static TalentNode<?> fromNBT(@Nullable final UUID playerId, final CompoundNBT nbt, final int currentVersion) {
        final String talentName = nbt.getString("Name");
        final int level = nbt.getInt("Level");
        return TalentTree.migrate(playerId, talentName, level, currentVersion);
    }
}
