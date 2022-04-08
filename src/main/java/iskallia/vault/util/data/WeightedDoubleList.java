package iskallia.vault.util.data;

import com.google.gson.annotations.Expose;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class WeightedDoubleList<T> extends AbstractList<WeightedDoubleList.Entry<T>> implements RandomListAccess<T> {
    @Expose
    private final List<Entry<T>> entries = new ArrayList<>();


    public WeightedDoubleList<T> add(T value, double weight) {
        add(new Entry<>(value, weight));
        return this;
    }


    public int size() {
        return this.entries.size();
    }


    public Entry<T> get(int index) {
        return this.entries.get(index);
    }


    public boolean add(Entry<T> entry) {
        return this.entries.add(entry);
    }


    public Entry<T> remove(int index) {
        return this.entries.remove(index);
    }


    public boolean remove(Object o) {
        return this.entries.remove(o);
    }

    public boolean removeEntry(T t) {
        return removeIf(entry -> entry.value.equals(t));
    }


    public boolean removeAll(Collection<?> c) {
        return this.entries.removeAll(c);
    }


    public boolean removeIf(Predicate<? super Entry<T>> filter) {
        return this.entries.removeIf(filter);
    }

    public double getTotalWeight() {
        return this.entries.stream().mapToDouble(entry -> entry.weight).sum();
    }


    @Nullable
    public T getRandom(Random random) {
        double totalWeight = getTotalWeight();
        if (totalWeight <= 0.0D) {
            return null;
        }
        return getWeightedAt(random.nextDouble() * totalWeight);
    }

    private T getWeightedAt(double weight) {
        for (Entry<T> e : this.entries) {
            weight -= e.weight;
            if (weight < 0.0D) {
                return e.value;
            }
        }
        return null;
    }

    public WeightedDoubleList<T> copy() {
        WeightedDoubleList<T> copy = new WeightedDoubleList();
        this.entries.forEach(entry -> copy.add(entry.value, entry.weight));
        return copy;
    }

    public WeightedDoubleList<T> copyFiltered(Predicate<T> filter) {
        WeightedDoubleList<T> copy = new WeightedDoubleList();
        this.entries.forEach(entry -> {
            if (filter.test(entry.value)) {
                copy.add(entry);
            }
        });
        return copy;
    }

    public boolean containsValue(T value) {
        return stream().map(entry -> entry.value).anyMatch(t -> t.equals(value));
    }


    public void forEach(BiConsumer<T, Number> weightEntryConsumer) {
        forEach(entry -> weightEntryConsumer.accept(entry.value, Double.valueOf(entry.weight)));
    }

    public static class Entry<T> {
        @Expose
        public T value;
        @Expose
        public double weight;

        public Entry(T value, double weight) {
            this.value = value;
            this.weight = weight;
        }
    }

}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\data\WeightedDoubleList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */