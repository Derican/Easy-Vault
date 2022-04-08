package iskallia.vault.attribute;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.*;
import java.util.function.Supplier;

public class VAttribute<T, I extends VAttribute.Instance<T>> {
    private final ResourceLocation id;

    public VAttribute(ResourceLocation id, Supplier<I> instance) {
        this(id, instance, (VAttribute<T, I>[]) new VAttribute[0]);
    }

    private final Supplier<I> instance;
    private final List<VAttribute<T, I>> modifiers;

    public VAttribute(ResourceLocation id, Supplier<I> instance, VAttribute<T, I>... modifiers) {
        this.id = id;
        this.instance = instance;
        this.modifiers = new ArrayList<>(Arrays.asList(modifiers));
    }

    public ResourceLocation getId() {
        return this.id;
    }

    protected String getTagKey() {
        return "Attributes";
    }


    public Optional<I> get(CompoundNBT nbt) {
        if (nbt == null || !nbt.contains(getTagKey(), 9)) return Optional.empty();

        ListNBT attributesList = nbt.getList(getTagKey(), 10);

        for (INBT element : attributesList) {
            CompoundNBT tag = (CompoundNBT) element;

            if (tag.getString("Id").equals(getId().toString())) {
                Instance instance = (Instance) this.instance.get();
                instance.parent = this;
                instance.delegate = tag;
                instance.read(tag);
                return Optional.of((I) instance);
            }
        }

        return Optional.empty();
    }

    public boolean exists(CompoundNBT nbt) {
        return get(nbt).isPresent();
    }

    public I getOrDefault(CompoundNBT nbt, T value) {
        return getOrDefault(nbt, () -> value);
    }

    public I getOrDefault(CompoundNBT nbt, Supplier<T> value) {
        return get(nbt).orElse((I) ((Instance) this.instance.get()).setBaseValue(value.get()));
    }

    public I getOrCreate(CompoundNBT nbt, T value) {
        return getOrCreate(nbt, () -> value);
    }

    public I getOrCreate(CompoundNBT nbt, Supplier<T> value) {
        return get(nbt).orElseGet(() -> create(nbt, value));
    }

    public I create(CompoundNBT nbt, T value) {
        return create(nbt, () -> value);
    }

    public I create(CompoundNBT nbt, Supplier<T> value) {
        if (!nbt.contains(getTagKey(), 9)) nbt.put(getTagKey(), (INBT) new ListNBT());
        ListNBT attributesList = nbt.getList(getTagKey(), 10);


        CompoundNBT attributeNBT = attributesList.stream().map(element -> (CompoundNBT) element).filter(tag -> tag.getString("Id").equals(getId().toString())).findFirst().orElseGet(() -> {
            CompoundNBT tag = new CompoundNBT();

            attributesList.add(tag);
            return tag;
        });
        Instance instance = (Instance) this.instance.get();
        instance.parent = this;
        instance.delegate = attributeNBT;
        instance.setBaseValue(value.get());
        return (I) instance;
    }


    public Optional<I> get(ItemStack stack) {
        CompoundNBT nbt = stack.getTagElement("Vault");
        if (nbt == null || !nbt.contains(getTagKey(), 9)) return Optional.empty();

        ListNBT attributesList = nbt.getList(getTagKey(), 10);

        for (INBT element : attributesList) {
            CompoundNBT tag = (CompoundNBT) element;

            if (tag.getString("Id").equals(getId().toString())) {
                Instance instance = (Instance) this.instance.get();
                instance.parent = this;
                instance.delegate = tag;
                instance.read(tag);
                return Optional.of((I) instance);
            }
        }

        return Optional.empty();
    }

    public Optional<T> getBase(ItemStack stack) {
        return get(stack).map(Instance::getBaseValue);
    }

    public Optional<T> getValue(ItemStack stack) {
        return get(stack).map(attribute -> attribute.getValue(stack));
    }

    public boolean exists(ItemStack stack) {
        return get(stack).isPresent();
    }

    public I getOrDefault(ItemStack stack, T value) {
        return getOrDefault(stack, () -> value);
    }

    public I getOrDefault(ItemStack stack, Random random, Instance.Generator<T> generator) {
        return getOrDefault(stack, () -> generator.generate(stack, random));
    }

    public I getOrDefault(ItemStack stack, Supplier<T> value) {
        return get(stack).orElse((I) ((Instance) this.instance.get()).setBaseValue(value.get()));
    }

    public I getOrCreate(ItemStack stack, T value) {
        return getOrCreate(stack, () -> value);
    }

    public I getOrCreate(ItemStack stack, Random random, Instance.Generator<T> generator) {
        return getOrCreate(stack, () -> generator.generate(stack, random));
    }

    public I getOrCreate(ItemStack stack, Supplier<T> value) {
        return get(stack).orElseGet(() -> create(stack, value));
    }

    public I create(ItemStack stack, T value) {
        return create(stack, () -> value);
    }

    public I create(ItemStack stack, Random random, Instance.Generator<T> generator) {
        return create(stack, () -> generator.generate(stack, random));
    }

    public I create(ItemStack stack, Supplier<T> value) {
        CompoundNBT nbt = stack.getOrCreateTagElement("Vault");
        if (!nbt.contains(getTagKey(), 9)) nbt.put(getTagKey(), (INBT) new ListNBT());
        ListNBT attributesList = nbt.getList(getTagKey(), 10);


        CompoundNBT attributeNBT = attributesList.stream().map(element -> (CompoundNBT) element).filter(tag -> tag.getString("Id").equals(getId().toString())).findFirst().orElseGet(() -> {
            CompoundNBT tag = new CompoundNBT();

            attributesList.add(tag);
            return tag;
        });
        Instance instance = (Instance) this.instance.get();
        instance.parent = this;
        instance.delegate = attributeNBT;
        instance.setBaseValue(value.get());
        return (I) instance;
    }


    public static abstract class Instance<T>
            implements INBTSerializable<CompoundNBT>, Modifier<T> {
        protected VAttribute<T, ? extends Instance<T>> parent;


        protected T baseValue;


        private VAttribute.Modifier<T> modifier;

        protected CompoundNBT delegate;


        protected Instance() {
        }


        protected Instance(VAttribute.Modifier<T> modifier) {
            this.modifier = modifier;
        }


        public final CompoundNBT serializeNBT() {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putString("Id", this.parent.id.toString());
            write(nbt);
            return nbt;
        }


        public final void deserializeNBT(CompoundNBT nbt) {
            read(nbt);
        }


        public T getBaseValue() {
            return this.baseValue;
        }

        public Instance<T> setBaseValue(T baseValue) {
            this.baseValue = baseValue;
            updateNBT();
            return this;
        }

        public T getValue(ItemStack stack) {
            T value = getBaseValue();
            if (this.parent == null) return value;

            for (VAttribute<T, ? extends Instance<T>> modifier : this.parent.modifiers) {
                Optional<? extends Instance<T>> instance = modifier.get(stack);

                if (instance.isPresent()) {
                    value = ((Instance<T>) instance.get()).apply(stack, instance.get(), value);
                }
            }

            return value;
        }


        public T apply(ItemStack stack, Instance<T> parent, T value) {
            return (this.modifier == null) ? value : this.modifier.apply(stack, parent, value);
        }

        public void updateNBT() {
            if (this.delegate == null)
                return;
            CompoundNBT nbt = serializeNBT();

            for (String key : nbt.getAllKeys()) {
                INBT value = nbt.get(key);

                if (value != null)
                    this.delegate.put(key, value);
            }
        }

        public abstract void write(CompoundNBT param1CompoundNBT);

        public abstract void read(CompoundNBT param1CompoundNBT);

        @FunctionalInterface
        public static interface Generator<T> {
            T generate(ItemStack param2ItemStack, Random param2Random);
        }
    }

    @FunctionalInterface
    public static interface Modifier<T> {
        T apply(ItemStack param1ItemStack, VAttribute.Instance<T> param1Instance, T param1T);
    }

    @FunctionalInterface
    public static interface Generator<T> {
        T generate(ItemStack param1ItemStack, Random param1Random);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\attribute\VAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */