package iskallia.vault.attribute;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class VAttribute<T, I extends VAttribute.Instance<T>> {
    private final ResourceLocation id;
    private final Supplier<I> instance;
    private final List<VAttribute<T, I>> modifiers;

    public VAttribute(final ResourceLocation id, final Supplier<I> instance) {
        this(id, instance, (VAttribute[]) new VAttribute[0]);
    }

    public VAttribute(final ResourceLocation id, final Supplier<I> instance, final VAttribute<T, I>... modifiers) {
        this.id = id;
        this.instance = instance;
        this.modifiers = new ArrayList<VAttribute<T, I>>(Arrays.asList(modifiers));
    }

    public ResourceLocation getId() {
        return this.id;
    }

    protected String getTagKey() {
        return "Attributes";
    }

    public Optional<I> get(final CompoundNBT nbt) {
        if (nbt == null || !nbt.contains(this.getTagKey(), 9)) {
            return Optional.empty();
        }
        final ListNBT attributesList = nbt.getList(this.getTagKey(), 10);
        for (final INBT element : attributesList) {
            final CompoundNBT tag = (CompoundNBT) element;
            if (tag.getString("Id").equals(this.getId().toString())) {
                final I instance = this.instance.get();
                instance.parent = (VAttribute<T, ? extends Instance<T>>) this;
                instance.read(instance.delegate = tag);
                return Optional.of(instance);
            }
        }
        return Optional.empty();
    }

    public boolean exists(final CompoundNBT nbt) {
        return this.get(nbt).isPresent();
    }

    public I getOrDefault(final CompoundNBT nbt, final T value) {
        return this.getOrDefault(nbt, () -> value);
    }

    public I getOrDefault(final CompoundNBT nbt, final Supplier<T> value) {
        return this.get(nbt).orElse((I) this.instance.get().setBaseValue(value.get()));
    }

    public I getOrCreate(final CompoundNBT nbt, final T value) {
        return this.getOrCreate(nbt, () -> value);
    }

    public I getOrCreate(final CompoundNBT nbt, final Supplier<T> value) {
        return this.get(nbt).orElseGet(() -> this.create(nbt, value));
    }

    public I create(final CompoundNBT nbt, final T value) {
        return this.create(nbt, () -> value);
    }

    public I create(final CompoundNBT nbt, final Supplier<T> value) {
        if (!nbt.contains(this.getTagKey(), 9)) {
            nbt.put(this.getTagKey(), (INBT) new ListNBT());
        }
        final ListNBT attributesList = nbt.getList(this.getTagKey(), 10);
        final CompoundNBT attributeNBT = attributesList.stream().map(element -> (CompoundNBT) element).filter(tag -> tag.getString("Id").equals(this.getId().toString())).findFirst().orElseGet(() -> {
            final CompoundNBT tag2 = new CompoundNBT();
            attributesList.add((INBT) tag2);
            return tag2;
        });
        final I instance = this.instance.get();
        instance.parent = (VAttribute<T, ? extends Instance<T>>) this;
        instance.delegate = attributeNBT;
        instance.setBaseValue(value.get());
        return instance;
    }

    public Optional<I> get(final ItemStack stack) {
        final CompoundNBT nbt = stack.getTagElement("Vault");
        if (nbt == null || !nbt.contains(this.getTagKey(), 9)) {
            return Optional.empty();
        }
        final ListNBT attributesList = nbt.getList(this.getTagKey(), 10);
        for (final INBT element : attributesList) {
            final CompoundNBT tag = (CompoundNBT) element;
            if (tag.getString("Id").equals(this.getId().toString())) {
                final I instance = this.instance.get();
                instance.parent = (VAttribute<T, ? extends Instance<T>>) this;
                instance.read(instance.delegate = tag);
                return Optional.of(instance);
            }
        }
        return Optional.empty();
    }

    public Optional<T> getBase(final ItemStack stack) {
        return this.get(stack).map((Function<? super I, ? extends T>) Instance::getBaseValue);
    }

    public Optional<T> getValue(final ItemStack stack) {
        return this.get(stack).map(attribute -> attribute.getValue(stack));
    }

    public boolean exists(final ItemStack stack) {
        return this.get(stack).isPresent();
    }

    public I getOrDefault(final ItemStack stack, final T value) {
        return this.getOrDefault(stack, () -> value);
    }

    public I getOrDefault(final ItemStack stack, final Random random, final Instance.Generator<T> generator) {
        return this.getOrDefault(stack, () -> generator.generate(stack, random));
    }

    public I getOrDefault(final ItemStack stack, final Supplier<T> value) {
        return this.get(stack).orElse((I) this.instance.get().setBaseValue(value.get()));
    }

    public I getOrCreate(final ItemStack stack, final T value) {
        return this.getOrCreate(stack, () -> value);
    }

    public I getOrCreate(final ItemStack stack, final Random random, final Instance.Generator<T> generator) {
        return this.getOrCreate(stack, () -> generator.generate(stack, random));
    }

    public I getOrCreate(final ItemStack stack, final Supplier<T> value) {
        return this.get(stack).orElseGet(() -> this.create(stack, value));
    }

    public I create(final ItemStack stack, final T value) {
        return this.create(stack, () -> value);
    }

    public I create(final ItemStack stack, final Random random, final Instance.Generator<T> generator) {
        return this.create(stack, () -> generator.generate(stack, random));
    }

    public I create(final ItemStack stack, final Supplier<T> value) {
        final CompoundNBT nbt = stack.getOrCreateTagElement("Vault");
        if (!nbt.contains(this.getTagKey(), 9)) {
            nbt.put(this.getTagKey(), (INBT) new ListNBT());
        }
        final ListNBT attributesList = nbt.getList(this.getTagKey(), 10);
        final CompoundNBT attributeNBT = attributesList.stream().map(element -> (CompoundNBT) element).filter(tag -> tag.getString("Id").equals(this.getId().toString())).findFirst().orElseGet(() -> {
            final CompoundNBT tag2 = new CompoundNBT();
            attributesList.add((INBT) tag2);
            return tag2;
        });
        final I instance = this.instance.get();
        instance.parent = (VAttribute<T, ? extends Instance<T>>) this;
        instance.delegate = attributeNBT;
        instance.setBaseValue(value.get());
        return instance;
    }

    public abstract static class Instance<T> implements INBTSerializable<CompoundNBT>, Modifier<T> {
        protected VAttribute<T, ? extends Instance<T>> parent;
        protected T baseValue;
        private Modifier<T> modifier;
        protected CompoundNBT delegate;

        protected Instance() {
        }

        protected Instance(final Modifier<T> modifier) {
            this.modifier = modifier;
        }

        public final CompoundNBT serializeNBT() {
            final CompoundNBT nbt = new CompoundNBT();
            nbt.putString("Id", (this.parent).id.toString());
            this.write(nbt);
            return nbt;
        }

        public final void deserializeNBT(final CompoundNBT nbt) {
            this.read(nbt);
        }

        public abstract void write(final CompoundNBT p0);

        public abstract void read(final CompoundNBT p0);

        public T getBaseValue() {
            return this.baseValue;
        }

        public Instance<T> setBaseValue(final T baseValue) {
            this.baseValue = baseValue;
            this.updateNBT();
            return this;
        }

        public T getValue(final ItemStack stack) {
            T value = this.getBaseValue();
            if (this.parent == null) {
                return value;
            }
            for (final VAttribute<T, ? extends Instance<T>> modifier : (this.parent).modifiers) {
                final Optional<? extends Instance<T>> instance = modifier.get(stack);
                if (instance.isPresent()) {
                    value = (T) ((Instance) instance.get()).apply(stack, (Instance<Object>) instance.get(), value);
                }
            }
            return value;
        }

        public T apply(final ItemStack stack, final Instance<T> parent, final T value) {
            return (this.modifier == null) ? value : this.modifier.apply(stack, parent, value);
        }

        public void updateNBT() {
            if (this.delegate == null) {
                return;
            }
            final CompoundNBT nbt = this.serializeNBT();
            for (final String key : nbt.getAllKeys()) {
                final INBT value = nbt.get(key);
                if (value != null) {
                    this.delegate.put(key, value);
                }
            }
        }

        @FunctionalInterface
        public interface Generator<T> {
            T generate(final ItemStack p0, final Random p1);
        }
    }

    @FunctionalInterface
    public interface Modifier<T> {
        T apply(final ItemStack p0, final Instance<T> p1, final T p2);
    }
}
