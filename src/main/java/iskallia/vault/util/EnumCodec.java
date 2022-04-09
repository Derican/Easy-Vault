// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

import java.util.function.Function;

public class EnumCodec<T extends Enum<T>> implements Codec<T>
{
    private final Class<T> enumClass;
    
    private EnumCodec(final Class<T> enumClass) {
        this.enumClass = enumClass;
    }
    
    public static <T extends Enum<T>> EnumCodec<T> of(final Class<T> clazz) {
        return new EnumCodec<T>(clazz);
    }
    
    public <V> DataResult<Pair<T, V>> decode(final DynamicOps<V> ops, final V input) {
        return (DataResult<Pair<T, V>>)ops.getNumberValue(input).map(Number::intValue).map(i -> MiscUtils.getEnumEntry(this.enumClass, i)).map(r -> Pair.of(r, ops.empty()));
    }
    
    public <V> DataResult<V> encode(final T input, final DynamicOps<V> ops, final V prefix) {
        return (DataResult<V>)ops.mergeToPrimitive(prefix, ops.createInt(input.ordinal()));
    }
}
