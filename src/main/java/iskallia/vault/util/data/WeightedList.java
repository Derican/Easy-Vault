package iskallia.vault.util.data;

import com.google.gson.annotations.Expose;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class WeightedList<T> extends AbstractList<WeightedList.Entry<T>> implements RandomListAccess<T> {
    @Expose
    private final List<Entry<T>> entries;

    public WeightedList() {
        this.entries = new ArrayList<Entry<T>>();
    }

    public WeightedList(final Map<T, Integer> map) {
        this();
        map.forEach(this::add);
    }

    public WeightedList<T> add(final T value, final int weight) {
        this.add(new Entry<T>(value, weight));
        return this;
    }

    @Override
    public int size() {
        return this.entries.size();
    }

    @Override
    public Entry<T> get(final int index) {
        return this.entries.get(index);
    }

    @Override
    public boolean add(final Entry<T> entry) {
        return this.entries.add(entry);
    }

    @Override
    public Entry<T> remove(final int index) {
        return this.entries.remove(index);
    }

    @Override
    public boolean remove(final Object o) {
        return this.entries.remove(o);
    }

    @Override
    public boolean removeEntry(final T t) {
        return this.removeIf(entry -> entry.value.equals(t));
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        return this.entries.removeAll(c);
    }

    @Override
    public boolean removeIf(final Predicate<? super Entry<T>> filter) {
        return this.entries.removeIf(filter);
    }

    public int getTotalWeight() {
        return this.entries.stream().mapToInt(entry -> entry.weight).sum();
    }

    @Nullable
    @Override
    public T getRandom(final Random random) {
        final int totalWeight = this.getTotalWeight();
        if (totalWeight <= 0) {
            return null;
        }
        return this.getWeightedAt(random.nextInt(totalWeight));
    }

    private T getWeightedAt(int index) {
        for (final Entry<T> e : this.entries) {
            index -= e.weight;
            if (index < 0) {
                return e.value;
            }
        }
        return null;
    }

    public WeightedList<T> copy() {
        final WeightedList<T> copy = new WeightedList<T>();
        this.entries.forEach(entry -> copy.add(entry.value, entry.weight));
        return copy;
    }

    public WeightedList<T> copyFiltered(final Predicate<T> filter) {
        final WeightedList<T> copy = new WeightedList<T>();
        this.entries.forEach(entry -> {
            if (filter.test(entry.value)) {
                copy.add(entry);
            }
            return;
        });
        return copy;
    }

    public boolean containsValue(final T value) {
        return this.stream().map(entry -> entry.value).anyMatch(t -> t.equals(value));
    }

    @Override
    public void forEach(final BiConsumer<T, Number> weightEntryConsumer) {
        this.forEach(entry -> weightEntryConsumer.accept(entry.value, entry.weight));
    }

    public static class Entry<T> {
        @Expose
        public T value;
        @Expose
        public int weight;

        public Entry(final T value, final int weight) {
            this.value = value;
            this.weight = weight;
        }
    }
}
