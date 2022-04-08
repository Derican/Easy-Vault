package iskallia.vault.nbt;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class VMapNBT<K, V> implements INBTSerializable<ListNBT>, Map<K, V> {
    private Map<K, V> delegate;
    private final BiConsumer<CompoundNBT, K> writeKey;
    private final BiConsumer<CompoundNBT, V> writeValue;
    private final Function<CompoundNBT, K> readKey;
    private final Function<CompoundNBT, V> readValue;

    public VMapNBT(Map<K, V> map, BiConsumer<CompoundNBT, K> writeKey, BiConsumer<CompoundNBT, V> writeValue, Function<CompoundNBT, K> readKey, Function<CompoundNBT, V> readValue) {
        this.delegate = map;
        this.writeKey = writeKey;
        this.writeValue = writeValue;
        this.readKey = readKey;
        this.readValue = readValue;
    }


    public VMapNBT(BiConsumer<CompoundNBT, K> writeKey, BiConsumer<CompoundNBT, V> writeValue, Function<CompoundNBT, K> readKey, Function<CompoundNBT, V> readValue) {
        this(new HashMap<>(), writeKey, writeValue, readKey, readValue);
    }


    public ListNBT serializeNBT() {
        ListNBT nbt = new ListNBT();

        this.delegate.forEach((key, value) -> {
            CompoundNBT entry = new CompoundNBT();

            this.writeKey.accept(entry, (K) key);
            this.writeValue.accept(entry, (V) value);
            nbt.add(entry);
        });
        return nbt;
    }


    public void deserializeNBT(ListNBT nbt) {
        this.delegate.clear();

        IntStream.range(0, nbt.size()).mapToObj(nbt::getCompound).forEach(entry -> this.delegate.put(this.readKey.apply(entry), this.readValue.apply(entry)));
    }


    public int size() {
        return this.delegate.size();
    }


    public boolean isEmpty() {
        return this.delegate.isEmpty();
    }


    public boolean containsKey(Object key) {
        return this.delegate.containsKey(key);
    }


    public boolean containsValue(Object value) {
        return this.delegate.containsValue(value);
    }


    public V get(Object key) {
        return this.delegate.get(key);
    }


    public V put(K key, V value) {
        return this.delegate.put(key, value);
    }


    public V remove(Object key) {
        return this.delegate.remove(key);
    }


    public void putAll(Map<? extends K, ? extends V> m) {
        this.delegate.putAll(m);
    }


    public void clear() {
        this.delegate.clear();
    }


    public Set<K> keySet() {
        return this.delegate.keySet();
    }


    public Collection<V> values() {
        return this.delegate.values();
    }


    public Set<Map.Entry<K, V>> entrySet() {
        return this.delegate.entrySet();
    }

    public static <N extends INBT, T extends INBTSerializable<N>> VMapNBT<UUID, T> ofUUID(Supplier<T> supplier) {
        return new VMapNBT<>((nbt, uuid) -> nbt.putString("Key", uuid.toString()), (nbt, value) -> nbt.put("Value", value.serializeNBT()), nbt -> UUID.fromString(nbt.getString("Key")), nbt -> {
            T iNBTSerializable = supplier.get();
            iNBTSerializable.deserializeNBT((N) nbt.get("Value"));
            return iNBTSerializable;
        });
    }


    public static <N extends INBT, T extends INBTSerializable<N>> VMapNBT<Integer, T> ofInt(Supplier<T> supplier) {
        return ofInt(new HashMap<>(), supplier);
    }

    public static <N extends INBT, T extends INBTSerializable<N>> VMapNBT<Integer, T> ofInt(Map<Integer, T> map, Supplier<T> supplier) {
        return new VMapNBT<>(map, (nbt, integer) -> nbt.putInt("Key", integer.intValue()), (nbt, value) -> nbt.put("Value", value.serializeNBT()), nbt -> Integer.valueOf(nbt.getInt("Key")), nbt -> {
            T iNBTSerializable = supplier.get();
            iNBTSerializable.deserializeNBT((N) nbt.get("Value"));
            return iNBTSerializable;
        });
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\nbt\VMapNBT.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */