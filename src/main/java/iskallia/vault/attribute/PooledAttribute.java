package iskallia.vault.attribute;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import iskallia.vault.util.gson.IgnoreEmpty;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.ToIntBiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class PooledAttribute<T> extends VAttribute.Instance<T> {
    protected PooledAttribute() {
    }

    protected PooledAttribute(final VAttribute.Modifier<T> modifier) {
        super(modifier);
    }

    public abstract static class Generator<T, O extends Generator.Operator<T>> implements VAttribute.Instance.Generator<T> {
        @Expose
        public List<Pool<T, O>> pools;
        @Expose
        public O collector;

        public Generator() {
            this.pools = new ArrayList<Pool<T, O>>();
        }

        public Generator<T, O> add(final T base, final Rolls rolls, final Consumer<Pool<T, O>> pool) {
            if (this.pools == null) {
                this.pools = new ArrayList<Pool<T, O>>();
            }
            final Pool<T, O> generated = new Pool<T, O>(base, rolls);
            this.pools.add(generated);
            pool.accept(generated);
            return this;
        }

        public Generator<T, O> collect(final O collector) {
            this.collector = collector;
            return this;
        }

        public abstract T getDefaultValue(final Random p0);

        @Override
        public T generate(final ItemStack stack, final Random random) {
            if (this.pools.size() == 0) {
                return this.getDefaultValue(random);
            }
            T value = (T) this.pools.get(0).generate(random);
            for (int i = 1; i < this.pools.size(); ++i) {
                value = this.collector.apply(value, (T) this.pools.get(i).generate(random));
            }
            return value;
        }

        public abstract static class Operator<T> extends Pool.Operator<T> {
        }
    }

    public static class Pool<T, O extends Generator.Operator<T>> {
        @Expose
        public T base;
        @Expose
        public Rolls rolls;
        @Expose
        public List<Entry<T, O>> entries;
        private int totalWeight;

        public Pool(final T base, final Rolls rolls) {
            this.entries = new ArrayList<Entry<T, O>>();
            this.base = base;
            this.rolls = rolls;
        }

        public Pool<T, O> add(final T value, final O operator, final int weight) {
            if (this.entries == null) {
                this.entries = new ArrayList<Entry<T, O>>();
            }
            final Entry<T, O> entry = new Entry<T, O>(value, operator, weight);
            this.entries.add(entry);
            return this;
        }

        public T generate(final Random random) {
            if (this.entries.isEmpty() || this.rolls.type.equals(Rolls.Type.EMPTY.name)) {
                return this.base;
            }
            final int roll = this.rolls.getRolls(random);
            T value = this.base;
            for (int i = 0; i < roll; ++i) {
                final Entry<T, O> entry = this.getRandom(random);
                value = entry.operator.apply(value, entry.value);
            }
            return value;
        }

        public Entry<T, O> getRandom(final Random random) {
            if (this.entries.size() == 0) {
                return null;
            }
            return this.getWeightedAt(random.nextInt(this.getTotalWeight()));
        }

        public Entry<T, O> getWeightedAt(int index) {
            Entry<T, O> current = null;
            final Iterator<Entry<T, O>> iterator = this.entries.iterator();
            while (iterator.hasNext()) {
                final Entry<T, O> entry = current = iterator.next();
                index -= current.weight;
                if (index < 0) {
                    break;
                }
            }
            return current;
        }

        private int getTotalWeight() {
            if (this.totalWeight == 0) {
                this.entries.forEach(entry -> this.totalWeight += entry.weight);
            }
            return this.totalWeight;
        }

        public List<T> getEntries() {
            return (List<T>) this.entries.stream()
                    .map(entry -> entry.value)
                    .collect(Collectors.toList());
        }

        public static class Entry<T, O extends Operator<T>> {
            @Expose
            public final T value;
            @Expose
            public final O operator;
            @Expose
            public final int weight;

            public Entry(final T value, final O operator, final int weight) {
                this.value = value;
                this.operator = operator;
                this.weight = weight;
            }
        }

        public abstract static class Operator<T> {
            public abstract T apply(final T p0, final T p1);
        }
    }

    public static class Rolls {
        @Expose
        public String type;
        @Expose
        @JsonAdapter(IgnoreEmpty.IntegerAdapter.class)
        public int value;
        @Expose
        @JsonAdapter(IgnoreEmpty.IntegerAdapter.class)
        public int min;
        @Expose
        @JsonAdapter(IgnoreEmpty.IntegerAdapter.class)
        public int max;
        @Expose
        @JsonAdapter(IgnoreEmpty.DoubleAdapter.class)
        public double chance;
        @Expose
        @JsonAdapter(IgnoreEmpty.IntegerAdapter.class)
        public int trials;
        @Expose
        @JsonAdapter(IgnoreEmpty.DoubleAdapter.class)
        public double probability;

        public static Rolls ofEmpty() {
            final Rolls rolls = new Rolls();
            rolls.type = Type.EMPTY.name;
            return rolls;
        }

        public static Rolls ofConstant(final int value) {
            final Rolls rolls = new Rolls();
            rolls.type = Type.CONSTANT.name;
            rolls.value = value;
            return rolls;
        }

        public static Rolls ofUniform(final int min, final int max) {
            final Rolls rolls = new Rolls();
            rolls.type = Type.UNIFORM.name;
            rolls.min = min;
            rolls.max = max;
            return rolls;
        }

        public static Rolls ofChance(final double chance, final int value) {
            final Rolls rolls = new Rolls();
            rolls.type = Type.CHANCE.name;
            rolls.value = value;
            rolls.chance = chance;
            return rolls;
        }

        public static Rolls ofBinomial(final int trials, final double probability) {
            final Rolls rolls = new Rolls();
            rolls.type = Type.BINOMIAL.name;
            rolls.trials = trials;
            rolls.probability = probability;
            return rolls;
        }

        public int getRolls(final Random random) {
            final Type type = Type.getByName(this.type);
            if (type == null) {
                throw new IllegalStateException("Unknown rolls type \"" + this.type + "\"");
            }
            return type.function.applyAsInt(this, random);
        }

        public enum Type {
            EMPTY("empty", (rolls, random) -> 0),
            CONSTANT("constant", (rolls, random) -> rolls.value),
            UNIFORM("uniform", (rolls, random) -> random.nextInt(rolls.max - rolls.min + 1) + rolls.min),
            CHANCE("chance", (rolls, random) -> (random.nextDouble() < rolls.chance) ? rolls.value : 0),
            BINOMIAL("binomial", (rolls, random) -> (int) IntStream.range(0, rolls.trials).filter(i -> random.nextDouble() < rolls.probability).count());

            public final String name;
            private final ToIntBiFunction<Rolls, Random> function;

            private Type(final String name, final ToIntBiFunction<Rolls, Random> function) {
                this.name = name;
                this.function = function;
            }

            public static Type getByName(final String name) {
                for (final Type value : values()) {
                    if (value.name.equals(name)) {
                        return value;
                    }
                }
                return null;
            }
        }
    }
}
