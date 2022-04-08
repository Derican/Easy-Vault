package iskallia.vault.world.raid;

import iskallia.vault.attribute.VAttribute;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Optional;
import java.util.function.Supplier;

public class RaidProperties
        implements INBTSerializable<CompoundNBT> {
    protected CompoundNBT data = new CompoundNBT();


    public CompoundNBT getData() {
        return this.data;
    }

    public <T, I extends VAttribute.Instance<T>> Optional<I> get(VAttribute<T, I> attribute) {
        return attribute.get(getData());
    }

    public <T, I extends VAttribute.Instance<T>> Optional<T> getBase(VAttribute<T, I> attribute) {
        return attribute.get(getData()).map(VAttribute.Instance::getBaseValue);
    }

    public <T, I extends VAttribute.Instance<T>> T getValue(VAttribute<T, I> attribute) {
        return attribute.get(getData()).map(VAttribute.Instance::getBaseValue).get();
    }

    public <T, I extends VAttribute.Instance<T>> boolean exists(VAttribute<T, I> attribute) {
        return attribute.exists(getData());
    }

    public <T, I extends VAttribute.Instance<T>> I getOrDefault(VAttribute<T, I> attribute, T value) {
        return (I) attribute.getOrDefault(getData(), value);
    }

    public <T, I extends VAttribute.Instance<T>> I getOrDefault(VAttribute<T, I> attribute, Supplier<T> value) {
        return (I) attribute.getOrDefault(getData(), value);
    }

    public <T, I extends VAttribute.Instance<T>> T getBaseOrDefault(VAttribute<T, I> attribute, T value) {
        return (T) attribute.getOrDefault(getData(), value).getBaseValue();
    }

    public <T, I extends VAttribute.Instance<T>> T getBaseOrDefault(VAttribute<T, I> attribute, Supplier<T> value) {
        return (T) attribute.getOrDefault(getData(), value).getBaseValue();
    }

    public <T, I extends VAttribute.Instance<T>> I getOrCreate(VAttribute<T, I> attribute, T value) {
        return (I) attribute.getOrCreate(getData(), value);
    }

    public <T, I extends VAttribute.Instance<T>> I getOrCreate(VAttribute<T, I> attribute, Supplier<T> value) {
        return (I) attribute.getOrCreate(getData(), value);
    }

    public <T, I extends VAttribute.Instance<T>> I create(VAttribute<T, I> attribute, T value) {
        return (I) attribute.create(getData(), value);
    }

    public <T, I extends VAttribute.Instance<T>> I create(VAttribute<T, I> attribute, Supplier<T> value) {
        return (I) attribute.create(getData(), value);
    }


    public CompoundNBT serializeNBT() {
        return this.data;
    }


    public void deserializeNBT(CompoundNBT nbt) {
        this.data = nbt;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\raid\RaidProperties.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */