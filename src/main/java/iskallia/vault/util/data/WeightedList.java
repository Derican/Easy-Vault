package iskallia.vault.util.data;

import com.google.gson.annotations.Expose;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class WeightedList<T> extends AbstractList<WeightedList.Entry<T>> implements RandomListAccess<T> {
    @Expose
    private final List<Entry<T>> entries = new ArrayList<>();


    public WeightedList(Map<T, Integer> map) {
        this();
        map.forEach(this::add);
    }

    public WeightedList<T> add(T value, int weight) {
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

    public int getTotalWeight() {
        return this.entries.stream().mapToInt(entry -> entry.weight).sum();
    }


    @Nullable
    public T getRandom(Random random) {
        int totalWeight = getTotalWeight();
        if (totalWeight <= 0) {
            return null;
        }
        return getWeightedAt(random.nextInt(totalWeight));
    }

    private T getWeightedAt(int index) {
        for (Entry<T> e : this.entries) {
            index -= e.weight;
            if (index < 0) {
                return e.value;
            }
        }
        return null;
    }

    public WeightedList<T> copy() {
        WeightedList<T> copy = new WeightedList();
        this.entries.forEach(entry -> copy.add(entry.value, entry.weight));
        return copy;
    }

    public WeightedList<T> copyFiltered(Predicate<T> filter) {
        WeightedList<T> copy = new WeightedList();
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
        forEach(entry -> weightEntryConsumer.accept(entry.value, Integer.valueOf(entry.weight)));
    }

    public WeightedList() {
    }

    public static class Entry<T> {
        @Expose
        public T value;
        @Expose
        public int weight;

        public Entry(T value, int weight) {
            this.value = value;
            this.weight = weight;
        }
    }

}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\data\WeightedList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */