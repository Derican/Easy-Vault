package iskallia.vault.attribute;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import iskallia.vault.util.gson.IgnoreEmpty;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.ToIntBiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public abstract class PooledAttribute<T>
        extends VAttribute.Instance<T> {
    protected PooledAttribute() {
    }

    protected PooledAttribute(VAttribute.Modifier<T> modifier) {
        super(modifier);
    }

    public static abstract class Generator<T, O extends Generator.Operator<T>> implements VAttribute.Instance.Generator<T> {
        @Expose
        public List<PooledAttribute.Pool<T, O>> pools = new ArrayList<>();
        @Expose
        public O collector;

        public Generator<T, O> add(T base, PooledAttribute.Rolls rolls, Consumer<PooledAttribute.Pool<T, O>> pool) {
            if (this.pools == null) {
                this.pools = new ArrayList<>();
            }

            PooledAttribute.Pool<T, O> generated = (PooledAttribute.Pool) new PooledAttribute.Pool<>(base, rolls);
            this.pools.add(generated);
            pool.accept(generated);
            return this;
        }

        public Generator<T, O> collect(O collector) {
            this.collector = collector;
            return this;
        }


        public abstract T getDefaultValue(Random param1Random);

        public T generate(ItemStack stack, Random random) {
            if (this.pools.size() == 0) {
                return getDefaultValue(random);
            }

            T value = (T) ((PooledAttribute.Pool) this.pools.get(0)).generate(random);

            for (int i = 1; i < this.pools.size(); i++) {
                value = this.collector.apply(value, (T) ((PooledAttribute.Pool) this.pools.get(i)).generate(random));
            }

            return value;
        }

        public static abstract class Operator<T> extends PooledAttribute.Pool.Operator<T> {
        }
    }


    public static class Pool<T, O extends Pool.Operator<T>> {
        @Expose
        public T base;
        @Expose
        public PooledAttribute.Rolls rolls;
        @Expose
        public List<Entry<T, O>> entries = new ArrayList<>();

        private int totalWeight;

        public Pool(T base, PooledAttribute.Rolls rolls) {
            this.base = base;
            this.rolls = rolls;
        }

        public Pool<T, O> add(T value, O operator, int weight) {
            if (this.entries == null) {
                this.entries = new ArrayList<>();
            }

            Entry<T, O> entry = new Entry<>(value, operator, weight);
            this.entries.add(entry);
            return this;
        }

        public T generate(Random random) {
            if (this.entries.isEmpty() || this.rolls.type.equals(PooledAttribute.Rolls.Type.EMPTY.name)) {
                return this.base;
            }

            int roll = this.rolls.getRolls(random);
            T value = this.base;

            for (int i = 0; i < roll; i++) {
                Entry<T, O> entry = getRandom(random);
                value = entry.operator.apply(value, entry.value);
            }

            return value;
        }

        public Entry<T, O> getRandom(Random random) {
            if (this.entries.size() == 0) return null;
            return getWeightedAt(random.nextInt(getTotalWeight()));
        }

        public Entry<T, O> getWeightedAt(int index) {
            Entry<T, O> current = null;

            for (Entry<T, O> entry : this.entries) {
                current = entry;
                index -= current.weight;
                if (index < 0)
                    break;
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

            public Entry(T value, O operator, int weight) {
                this.value = value;
                this.operator = operator;
                this.weight = weight;
            }
        }


        public static abstract class Operator<T> {
            public abstract T apply(T param2T1, T param2T2);
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

        public static Rolls ofEmpty() {
            Rolls rolls = new Rolls();
            rolls.type = Type.EMPTY.name;
            return rolls;
        }

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

        public static Rolls ofConstant(int value) {
            Rolls rolls = new Rolls();
            rolls.type = Type.CONSTANT.name;
            rolls.value = value;
            return rolls;
        }


        public static Rolls ofUniform(int min, int max) {
            Rolls rolls = new Rolls();
            rolls.type = Type.UNIFORM.name;
            rolls.min = min;
            rolls.max = max;
            return rolls;
        }

        public static Rolls ofChance(double chance, int value) {
            Rolls rolls = new Rolls();
            rolls.type = Type.CHANCE.name;
            rolls.value = value;
            rolls.chance = chance;
            return rolls;
        }

        public static Rolls ofBinomial(int trials, double probability) {
            Rolls rolls = new Rolls();
            rolls.type = Type.BINOMIAL.name;
            rolls.trials = trials;
            rolls.probability = probability;
            return rolls;
        }

        public int getRolls(Random random) {
            Type type = Type.getByName(this.type);

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
            private final ToIntBiFunction<PooledAttribute.Rolls, Random> function;
            public final String name;


            Type(String name, ToIntBiFunction<PooledAttribute.Rolls, Random> function) {
                this.name = name;
                this.function = function;
            }

            public static Type getByName(String name) {
                for (Type value : values()) {
                    if (value.name.equals(name)) return value;

                }
                return null;
            }
        }

    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\attribute\PooledAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */