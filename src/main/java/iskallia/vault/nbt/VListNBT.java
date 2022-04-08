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
    private List<T> delegate;

    public VListNBT(List<T> list, Function<T, N> write, Function<N, T> read) {
        this.delegate = list;
        this.write = write;
        this.read = read;
    }

    private final Function<T, N> write;
    private final Function<N, T> read;

    public VListNBT(Function<T, N> write, Function<N, T> read) {
        this(new ArrayList<>(), write, read);
    }


    public ListNBT serializeNBT() {
        ListNBT nbt = new ListNBT();

        this.delegate.forEach(value -> nbt.add(this.write.apply((T) value)));


        return nbt;
    }


    public void deserializeNBT(ListNBT nbt) {
        this.delegate.clear();

        nbt.stream().map(tag -> tag).forEach(entry -> add(this.read.apply((N) entry)));
    }


    public int size() {
        return this.delegate.size();
    }


    public boolean isEmpty() {
        return this.delegate.isEmpty();
    }


    public boolean contains(Object o) {
        return this.delegate.contains(o);
    }


    public Iterator<T> iterator() {
        return this.delegate.iterator();
    }


    public Object[] toArray() {
        return this.delegate.toArray();
    }


    public <T1> T1[] toArray(T1[] a) {
        return this.delegate.toArray(a);
    }


    public boolean add(T t) {
        return this.delegate.add(t);
    }


    public boolean remove(Object o) {
        return this.delegate.remove(o);
    }


    public boolean containsAll(Collection<?> c) {
        return this.delegate.containsAll(c);
    }


    public boolean addAll(Collection<? extends T> c) {
        return this.delegate.addAll(c);
    }


    public boolean addAll(int index, Collection<? extends T> c) {
        return this.delegate.addAll(index, c);
    }


    public boolean removeAll(Collection<?> c) {
        return this.delegate.removeAll(c);
    }


    public boolean retainAll(Collection<?> c) {
        return this.delegate.retainAll(c);
    }


    public void clear() {
        this.delegate.clear();
    }


    public T get(int index) {
        return this.delegate.get(index);
    }


    public T set(int index, T element) {
        return this.delegate.set(index, element);
    }


    public void add(int index, T element) {
        this.delegate.add(index, element);
    }


    public T remove(int index) {
        return this.delegate.remove(index);
    }


    public int indexOf(Object o) {
        return this.delegate.indexOf(o);
    }


    public int lastIndexOf(Object o) {
        return this.delegate.lastIndexOf(o);
    }


    public ListIterator<T> listIterator() {
        return this.delegate.listIterator();
    }


    public ListIterator<T> listIterator(int index) {
        return this.delegate.listIterator(index);
    }


    public List<T> subList(int fromIndex, int toIndex) {
        return this.delegate.subList(fromIndex, toIndex);
    }

    public static <T extends INBTSerializable<N>, N extends INBT> VListNBT<T, N> of(Function<N, T> read) {
        return new VListNBT<>(INBTSerializable::serializeNBT, read);
    }

    public static <T extends INBTSerializable<N>, N extends INBT> VListNBT<T, N> of(List<T> list, Function<N, T> read) {
        return new VListNBT<>(list, INBTSerializable::serializeNBT, read);
    }

    public static <T extends INBTSerializable<N>, N extends INBT> VListNBT<T, N> of(Supplier<T> supplier) {
        return new VListNBT<>(INBTSerializable::serializeNBT, n -> {
            INBTSerializable iNBTSerializable = supplier.get();
            iNBTSerializable.deserializeNBT(n);
            return iNBTSerializable;
        });
    }


    public static VListNBT<UUID, StringNBT> ofUUID() {
        return new VListNBT<>(uuid -> StringNBT.valueOf(uuid.toString()), stringNBT -> UUID.fromString(stringNBT.getAsString()));
    }

    public static <T> VListNBT<T, CompoundNBT> ofCodec(Codec<T> codec, T defaultValue) {
        return new VListNBT<>(value -> {
            CompoundNBT tag = new CompoundNBT();
            tag.put("data", CodecUtils.writeNBT(codec, value));
            return tag;
        }tag -> CodecUtils.readNBT(codec, tag.get("data")).orElse(defaultValue));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\nbt\VListNBT.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */