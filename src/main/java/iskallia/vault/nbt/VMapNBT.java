// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.nbt;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class VMapNBT<K, V> implements INBTSerializable<ListNBT>, Map<K, V>
{
    private Map<K, V> delegate;
    private final BiConsumer<CompoundNBT, K> writeKey;
    private final BiConsumer<CompoundNBT, V> writeValue;
    private final Function<CompoundNBT, K> readKey;
    private final Function<CompoundNBT, V> readValue;
    
    public VMapNBT(final Map<K, V> map, final BiConsumer<CompoundNBT, K> writeKey, final BiConsumer<CompoundNBT, V> writeValue, final Function<CompoundNBT, K> readKey, final Function<CompoundNBT, V> readValue) {
        this.delegate = map;
        this.writeKey = writeKey;
        this.writeValue = writeValue;
        this.readKey = readKey;
        this.readValue = readValue;
    }
    
    public VMapNBT(final BiConsumer<CompoundNBT, K> writeKey, final BiConsumer<CompoundNBT, V> writeValue, final Function<CompoundNBT, K> readKey, final Function<CompoundNBT, V> readValue) {
        this((Map)new HashMap(), writeKey, writeValue, readKey, readValue);
    }
    
    public ListNBT serializeNBT() {
        final ListNBT nbt = new ListNBT();
        this.delegate.forEach((key, value) -> {
            final CompoundNBT entry = new CompoundNBT();
            this.writeKey.accept(entry, (K)key);
            this.writeValue.accept(entry, (V)value);
            nbt.add(entry);
            return;
        });
        return nbt;
    }
    
    public void deserializeNBT(final ListNBT nbt) {
        this.delegate.clear();
        IntStream.range(0, nbt.size()).mapToObj(nbt::getCompound).forEach(entry -> this.delegate.put(this.readKey.apply(entry), this.readValue.apply(entry)));
    }
    
    public int size() {
        return this.delegate.size();
    }
    
    public boolean isEmpty() {
        return this.delegate.isEmpty();
    }
    
    public boolean containsKey(final Object key) {
        return this.delegate.containsKey(key);
    }
    
    public boolean containsValue(final Object value) {
        return this.delegate.containsValue(value);
    }
    
    public V get(final Object key) {
        return this.delegate.get(key);
    }
    
    public V put(final K key, final V value) {
        return this.delegate.put(key, value);
    }
    
    public V remove(final Object key) {
        return this.delegate.remove(key);
    }
    
    public void putAll(final Map<? extends K, ? extends V> m) {
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
    
    public Set<Entry<K, V>> entrySet() {
        return this.delegate.entrySet();
    }
    
    public static <N extends INBT, T extends INBTSerializable<N>> VMapNBT<UUID, T> ofUUID(final Supplier<T> supplier) {
        return new VMapNBT<UUID, T>((nbt, uuid) -> nbt.putString("Key", uuid.toString()), (nbt, value) -> nbt.put("Value", value.serializeNBT()), nbt -> UUID.fromString(nbt.getString("Key")), nbt -> {
            final INBTSerializable value2 = supplier.get();
            value2.deserializeNBT(nbt.get("Value"));
            return (T) value2;
        });
    }
    
    public static <N extends INBT, T extends INBTSerializable<N>> VMapNBT<Integer, T> ofInt(final Supplier<T> supplier) {
        return ofInt(new HashMap<Integer, T>(), supplier);
    }
    
    public static <N extends INBT, T extends INBTSerializable<N>> VMapNBT<Integer, T> ofInt(final Map<Integer, T> map, final Supplier<T> supplier) {
        return new VMapNBT<Integer, T>(map, (nbt, integer) -> nbt.putInt("Key", (int)integer), (nbt, value) -> nbt.put("Value", value.serializeNBT()), nbt -> nbt.getInt("Key"), nbt -> {
            final INBTSerializable value2 = supplier.get();
            value2.deserializeNBT(nbt.get("Value"));
            return (T) value2;
        });
    }
}
