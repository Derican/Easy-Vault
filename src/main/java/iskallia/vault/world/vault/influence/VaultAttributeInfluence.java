package iskallia.vault.world.vault.influence;

import iskallia.vault.Vault;
import net.minecraft.nbt.CompoundNBT;

import java.util.function.Supplier;

public class VaultAttributeInfluence
        extends VaultInfluence {
    private final Type type;
    private float value;
    private boolean isMultiplicative;

    public VaultAttributeInfluence(Type type) {
        super(Vault.id("type_" + type.name().toLowerCase()));
        this.type = type;
    }

    public VaultAttributeInfluence(Type type, float value, boolean isMultiplicative) {
        this(type);
        this.value = value;
        this.isMultiplicative = isMultiplicative;
    }

    public static Supplier<VaultInfluence> newInstance(Type type) {
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


    public CompoundNBT serializeNBT() {
        CompoundNBT tag = super.serializeNBT();
        tag.putFloat("value", this.value);
        tag.putBoolean("isMultiplicative", this.isMultiplicative);
        return tag;
    }


    public void deserializeNBT(CompoundNBT tag) {
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
        FATAL_STRIKE_DAMAGE;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\influence\VaultAttributeInfluence.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */