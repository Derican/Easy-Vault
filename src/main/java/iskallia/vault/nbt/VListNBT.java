package iskallia.vault.nbt;

import com.mojang.serialization.Codec;
import iskallia.vault.util.CodecUtils;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class VListNBT<T, N extends INBT> implements INBTSerializable<ListNBT>, List<T> {
    private final List<T> delegate;
    private final Function<T, N> write;
    private final Function<N, T> read;

    public VListNBT(final List<T> list, final Function<T, N> write, final Function<N, T> read) {
        this.delegate = list;
        this.write = write;
        this.read = read;
    }

    public VListNBT(final Function<T, N> write, final Function<N, T> read) {
        this(new ArrayList(), write, read);
    }

    public ListNBT serializeNBT() {
        final ListNBT nbt = new ListNBT();
        this.delegate.forEach(value -> nbt.add(this.write.apply(value)));
        return nbt;
    }

    public void deserializeNBT(final ListNBT nbt) {
        this.delegate.clear();
        nbt.stream().map(tag -> tag).forEach(entry -> this.add(this.read.apply((N) entry)));
    }

    public int size() {
        return this.delegate.size();
    }

    public boolean isEmpty() {
        return this.delegate.isEmpty();
    }

    public boolean contains(final Object o) {
        return this.delegate.contains(o);
    }

    public Iterator<T> iterator() {
        return this.delegate.iterator();
    }

    public Object[] toArray() {
        return this.delegate.toArray();
    }

    public <T1> T1[] toArray(final T1[] a) {
        return this.delegate.toArray(a);
    }

    public boolean add(final T t) {
        return this.delegate.add(t);
    }

    public boolean remove(final Object o) {
        return this.delegate.remove(o);
    }

    public boolean containsAll(final Collection<?> c) {
        return this.delegate.containsAll(c);
    }

    public boolean addAll(final Collection<? extends T> c) {
        return this.delegate.addAll(c);
    }

    public boolean addAll(final int index, final Collection<? extends T> c) {
        return this.delegate.addAll(index, c);
    }

    public boolean removeAll(final Collection<?> c) {
        return this.delegate.removeAll(c);
    }

    public boolean retainAll(final Collection<?> c) {
        return this.delegate.retainAll(c);
    }

    public void clear() {
        this.delegate.clear();
    }

    public T get(final int index) {
        return this.delegate.get(index);
    }

    public T set(final int index, final T element) {
        return this.delegate.set(index, element);
    }

    public void add(final int index, final T element) {
        this.delegate.add(index, element);
    }

    public T remove(final int index) {
        return this.delegate.remove(index);
    }

    public int indexOf(final Object o) {
        return this.delegate.indexOf(o);
    }

    public int lastIndexOf(final Object o) {
        return this.delegate.lastIndexOf(o);
    }

    public ListIterator<T> listIterator() {
        return this.delegate.listIterator();
    }

    public ListIterator<T> listIterator(final int index) {
        return this.delegate.listIterator(index);
    }

    public List<T> subList(final int fromIndex, final int toIndex) {
        return this.delegate.subList(fromIndex, toIndex);
    }

    public static <T extends INBTSerializable<N>, N extends INBT> VListNBT<T, N> of(final Function<N, T> read) {
        return new VListNBT<T, N>(INBTSerializable::serializeNBT, read);
    }

    public static <T extends INBTSerializable<N>, N extends INBT> VListNBT<T, N> of(final List<T> list, final Function<N, T> read) {
        return new VListNBT<T, N>(list, INBTSerializable::serializeNBT, read);
    }

    public static <T extends INBTSerializable<N>, N extends INBT> VListNBT<T, N> of(final Supplier<T> supplier) {
        return new VListNBT<T, N>(INBTSerializable::serializeNBT, n -> {
            final T value = supplier.get();
            value.deserializeNBT(n);
            return value;
        });
    }

    public static VListNBT<UUID, StringNBT> ofUUID() {
        return new VListNBT<UUID, StringNBT>(uuid -> StringNBT.valueOf(uuid.toString()), stringNBT -> UUID.fromString(stringNBT.getAsString()));
    }

    public static <T> VListNBT<T, CompoundNBT> ofCodec(final Codec<T> codec, final T defaultValue) {
        return new VListNBT<T, CompoundNBT>(value -> {
            final CompoundNBT tag = new CompoundNBT();
            tag.put("data", CodecUtils.writeNBT((com.mojang.serialization.Codec<Object>) codec, value));
            return tag;
        }, tag -> CodecUtils.readNBT(codec, tag.get("data")).orElse(defaultValue));
    }
}
