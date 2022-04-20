package iskallia.vault.skill.ability;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.skill.ability.config.AbilityConfig;
import iskallia.vault.skill.ability.effect.AbilityEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.Objects;

public class AbilityNode<T extends AbilityConfig, E extends AbilityEffect<T>> implements INBTSerializable<CompoundNBT> {
    private String groupName;
    private int level;
    private String specialization;

    public AbilityNode(final String groupName, final int level, @Nullable final String specialization) {
        this.level = 0;
        this.specialization = null;
        this.groupName = groupName;
        this.level = level;
        this.specialization = specialization;
    }

    private AbilityNode(final CompoundNBT nbt) {
        this.level = 0;
        this.specialization = null;
        this.deserializeNBT(nbt);
    }

    public AbilityGroup<T, E> getGroup() {
        return (AbilityGroup<T, E>) ModConfigs.ABILITIES.getAbilityGroupByName(this.groupName);
    }

    public int getLevel() {
        return this.level;
    }

    @Nullable
    public String getSpecialization() {
        return this.specialization;
    }

    public void setSpecialization(@Nullable final String specialization) {
        this.specialization = specialization;
    }

    public String getName() {
        if (!this.isLearned()) {
            return this.getGroup().getName(1);
        }
        return this.getGroup().getName(this.getLevel());
    }

    public String getSpecializationName() {
        final String specialization = this.getSpecialization();
        if (specialization == null) {
            return this.getGroup().getParentName();
        }
        return this.getGroup().getSpecializationName(specialization);
    }

    public boolean isLearned() {
        return this.getLevel() > 0;
    }

    @Nullable
    public T getAbilityConfig() {
        if (!this.isLearned()) {
            return this.getGroup().getAbilityConfig(null, -1);
        }
        return this.getGroup().getAbilityConfig(this.getSpecialization(), this.getLevel() - 1);
    }

    @Nullable
    public E getAbility() {
        return this.getGroup().getAbility(this.getSpecialization());
    }

    public void onAdded(final PlayerEntity player) {
        if (this.isLearned() && this.getAbility() != null) {
            this.getAbility().onAdded(this.getAbilityConfig(), player);
        }
    }

    public void onRemoved(final PlayerEntity player) {
        if (this.isLearned() && this.getAbility() != null) {
            this.getAbility().onRemoved(this.getAbilityConfig(), player);
        }
    }

    public void onFocus(final PlayerEntity player) {
        if (this.isLearned() && this.getAbility() != null) {
            this.getAbility().onFocus(this.getAbilityConfig(), player);
        }
    }

    public void onBlur(final PlayerEntity player) {
        if (this.isLearned() && this.getAbility() != null) {
            this.getAbility().onBlur(this.getAbilityConfig(), player);
        }
    }

    public void onTick(final PlayerEntity player, final boolean active) {
        if (this.isLearned() && this.getAbility() != null) {
            this.getAbility().onTick(this.getAbilityConfig(), player, active);
        }
    }

    public boolean onAction(final ServerPlayerEntity player, final boolean active) {
        return this.isLearned() && this.getAbility() != null && this.getAbility().onAction(this.getAbilityConfig(), player, active);
    }

    public CompoundNBT serializeNBT() {
        final CompoundNBT nbt = new CompoundNBT();
        nbt.putString("Name", this.getGroup().getParentName());
        nbt.putInt("Level", this.getLevel());
        if (this.getSpecialization() != null) {
            nbt.putString("Specialization", this.getSpecialization());
        }
        return nbt;
    }

    public void deserializeNBT(final CompoundNBT nbt) {
        this.groupName = nbt.getString("Name");
        this.level = nbt.getInt("Level");
        if (nbt.contains("Specialization", 8)) {
            this.specialization = nbt.getString("Specialization");
            if (this.specialization.equals("Rampage_Nocrit") || this.specialization.equals("Ghost Walk_Duration")) {
                this.specialization = null;
            }
        } else {
            this.specialization = null;
        }
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        final AbilityNode<?, ?> that = (AbilityNode<?, ?>) other;
        return this.level == that.level && Objects.equals(this.getGroup(), that.getGroup());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getGroup(), this.level);
    }

    public static <T extends AbilityConfig, E extends AbilityEffect<T>> AbilityNode<T, E> fromNBT(final CompoundNBT nbt) {
        return new AbilityNode<T, E>(nbt);
    }
}
