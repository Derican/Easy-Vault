package iskallia.vault.skill.set;

import iskallia.vault.init.ModConfigs;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class SetNode<T extends PlayerSet> implements INBTSerializable<CompoundNBT> {
    private SetGroup<T> group;
    private int level;

    public SetNode(final SetGroup<T> group, final int level) {
        this.group = group;
        this.level = level;
    }

    public SetGroup<T> getGroup() {
        return this.group;
    }

    public int getLevel() {
        return this.level;
    }

    public T getSet() {
        if (!this.isActive()) {
            return null;
        }
        return this.getGroup().getSet(this.getLevel());
    }

    public String getName() {
        return this.getGroup().getName(this.getLevel());
    }

    public boolean isActive() {
        return this.level != 0;
    }

    public CompoundNBT serializeNBT() {
        final CompoundNBT nbt = new CompoundNBT();
        nbt.putString("Name", this.getGroup().getParentName());
        nbt.putInt("Level", this.getLevel());
        return nbt;
    }

    public void deserializeNBT(final CompoundNBT nbt) {
        final String groupName = nbt.getString("Name");
        this.group = (SetGroup<T>) ModConfigs.SETS.getByName(groupName);
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
        final SetNode<?> that = (SetNode<?>) other;
        return this.level == that.level && this.group.getParentName().equals(that.group.getParentName());
    }

    public static <T extends PlayerSet> SetNode<T> fromNBT(final CompoundNBT nbt, final Class<T> clazz) {
        final SetGroup<T> group = (SetGroup<T>) ModConfigs.SETS.getByName(nbt.getString("Name"));
        final int level = nbt.getInt("Level");
        return new SetNode<T>(group, level);
    }
}
