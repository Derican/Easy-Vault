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

public class AbilityNode<T extends AbilityConfig, E extends AbilityEffect<T>>
        implements INBTSerializable<CompoundNBT> {
    private String groupName;
    private int level = 0;
    private String specialization = null;

    public AbilityNode(String groupName, int level, @Nullable String specialization) {
        this.groupName = groupName;
        this.level = level;
        this.specialization = specialization;
    }

    private AbilityNode(CompoundNBT nbt) {
        deserializeNBT(nbt);
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

    public void setSpecialization(@Nullable String specialization) {
        this.specialization = specialization;
    }

    public String getName() {
        if (!isLearned()) {
            return getGroup().getName(1);
        }
        return getGroup().getName(getLevel());
    }

    public String getSpecializationName() {
        String specialization = getSpecialization();
        if (specialization == null) {
            return getGroup().getParentName();
        }
        return getGroup().getSpecializationName(specialization);
    }

    public boolean isLearned() {
        return (getLevel() > 0);
    }

    @Nullable
    public T getAbilityConfig() {
        if (!isLearned()) {
            return getGroup().getAbilityConfig(null, -1);
        }
        return getGroup().getAbilityConfig(getSpecialization(), getLevel() - 1);
    }

    @Nullable
    public E getAbility() {
        return getGroup().getAbility(getSpecialization());
    }

    public void onAdded(PlayerEntity player) {
        if (isLearned() && getAbility() != null) {
            getAbility().onAdded( getAbilityConfig(), player);
        }
    }

    public void onRemoved(PlayerEntity player) {
        if (isLearned() && getAbility() != null) {
            getAbility().onRemoved( getAbilityConfig(), player);
        }
    }

    public void onFocus(PlayerEntity player) {
        if (isLearned() && getAbility() != null) {
            getAbility().onFocus( getAbilityConfig(), player);
        }
    }

    public void onBlur(PlayerEntity player) {
        if (isLearned() && getAbility() != null) {
            getAbility().onBlur( getAbilityConfig(), player);
        }
    }

    public void onTick(PlayerEntity player, boolean active) {
        if (isLearned() && getAbility() != null) {
            getAbility().onTick( getAbilityConfig(), player, active);
        }
    }

    public boolean onAction(ServerPlayerEntity player, boolean active) {
        if (isLearned() && getAbility() != null) {
            return getAbility().onAction( getAbilityConfig(), player, active);
        }
        return false;
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("Name", getGroup().getParentName());
        nbt.putInt("Level", getLevel());
        if (getSpecialization() != null) {
            nbt.putString("Specialization", getSpecialization());
        }
        return nbt;
    }


    public void deserializeNBT(CompoundNBT nbt) {
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


    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        AbilityNode<?, ?> that = (AbilityNode<?, ?>) other;
        return (this.level == that.level && Objects.equals(getGroup(), that.getGroup()));
    }


    public int hashCode() {
        return Objects.hash(new Object[]{getGroup(), Integer.valueOf(this.level)});
    }


    public static <T extends AbilityConfig, E extends AbilityEffect<T>> AbilityNode<T, E> fromNBT(CompoundNBT nbt) {
        return new AbilityNode<>(nbt);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\AbilityNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */