package iskallia.vault.world.raid;

import iskallia.vault.attribute.VAttribute;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class RaidProperties implements INBTSerializable<CompoundNBT> {
    protected CompoundNBT data;

    public RaidProperties() {
        this.data = new CompoundNBT();
    }

    public CompoundNBT getData() {
        return this.data;
    }

    public <T, I extends VAttribute.Instance<T>> Optional<I> get(final VAttribute<T, I> attribute) {
        return attribute.get(this.getData());
    }

    public <T, I extends VAttribute.Instance<T>> Optional<T> getBase(final VAttribute<T, I> attribute) {
        return attribute.get(this.getData()).map((Function<? super I, ? extends T>) VAttribute.Instance::getBaseValue);
    }

    public <T, I extends VAttribute.Instance<T>> T getValue(final VAttribute<T, I> attribute) {
        return attribute.get(this.getData()).map((Function<? super I, ? extends T>) VAttribute.Instance::getBaseValue).get();
    }

    public <T, I extends VAttribute.Instance<T>> boolean exists(final VAttribute<T, I> attribute) {
        return attribute.exists(this.getData());
    }

    public <T, I extends VAttribute.Instance<T>> I getOrDefault(final VAttribute<T, I> attribute, final T value) {
        return attribute.getOrDefault(this.getData(), value);
    }

    public <T, I extends VAttribute.Instance<T>> I getOrDefault(final VAttribute<T, I> attribute, final Supplier<T> value) {
        return attribute.getOrDefault(this.getData(), value);
    }

    public <T, I extends VAttribute.Instance<T>> T getBaseOrDefault(final VAttribute<T, I> attribute, final T value) {
        return attribute.getOrDefault(this.getData(), value).getBaseValue();
    }

    public <T, I extends VAttribute.Instance<T>> T getBaseOrDefault(final VAttribute<T, I> attribute, final Supplier<T> value) {
        return attribute.getOrDefault(this.getData(), value).getBaseValue();
    }

    public <T, I extends VAttribute.Instance<T>> I getOrCreate(final VAttribute<T, I> attribute, final T value) {
        return attribute.getOrCreate(this.getData(), value);
    }

    public <T, I extends VAttribute.Instance<T>> I getOrCreate(final VAttribute<T, I> attribute, final Supplier<T> value) {
        return attribute.getOrCreate(this.getData(), value);
    }

    public <T, I extends VAttribute.Instance<T>> I create(final VAttribute<T, I> attribute, final T value) {
        return attribute.create(this.getData(), value);
    }

    public <T, I extends VAttribute.Instance<T>> I create(final VAttribute<T, I> attribute, final Supplier<T> value) {
        return attribute.create(this.getData(), value);
    }

    public CompoundNBT serializeNBT() {
        return this.data;
    }

    public void deserializeNBT(final CompoundNBT nbt) {
        this.data = nbt;
    }
}
