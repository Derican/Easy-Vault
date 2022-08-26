package iskallia.vault.world.vault.influence;

import iskallia.vault.Vault;
import net.minecraft.nbt.CompoundNBT;

import java.util.function.Supplier;

public class VaultAttributeInfluence extends VaultInfluence {
    private final Type type;
    private float value;
    private boolean isMultiplicative;

    public VaultAttributeInfluence(final Type type) {
        super(Vault.id("type_" + type.name().toLowerCase()));
        this.type = type;
    }

    public VaultAttributeInfluence(final Type type, final float value, final boolean isMultiplicative) {
        this(type);
        this.value = value;
        this.isMultiplicative = isMultiplicative;
    }

    public static Supplier<VaultInfluence> newInstance(final Type type) {
        return () -> new VaultAttributeInfluence(type);
    }

    public Type getType() {
        return this.type;
    }

    public float getValue() {
        return this.value;
    }

    public boolean isMultiplicative() {
        return this.isMultiplicative;
    }

    @Override
    public CompoundNBT serializeNBT() {
        final CompoundNBT tag = super.serializeNBT();
        tag.putFloat("value", this.value);
        tag.putBoolean("isMultiplicative", this.isMultiplicative);
        return tag;
    }

    @Override
    public void deserializeNBT(final CompoundNBT tag) {
        super.deserializeNBT(tag);
        this.value = tag.getFloat("value");
        this.isMultiplicative = tag.getBoolean("isMultiplicative");
    }

    public enum Type {
        RESISTANCE,
        PARRY,
        DURABILITY_DAMAGE,
        COOLDOWN_REDUCTION,
        CHEST_RARITY,
        HEALING_EFFECTIVENESS,
        SOUL_SHARD_DROPS,
        FATAL_STRIKE_CHANCE,
        FATAL_STRIKE_DAMAGE
    }
}
