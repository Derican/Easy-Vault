package iskallia.vault.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;


public class EnumCodec<T extends Enum<T>>
        implements Codec<T> {
    private final Class<T> enumClass;

    private EnumCodec(Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    public static <T extends Enum<T>> EnumCodec<T> of(Class<T> clazz) {
        return new EnumCodec<>(clazz);
    }


    public <V> DataResult<Pair<T, V>> decode(DynamicOps<V> ops, V input) {
        return ops.getNumberValue(input)
                .map(Number::intValue)
                .map(i -> MiscUtils.getEnumEntry(this.enumClass, i.intValue()))
                .map(r -> Pair.of(r, ops.empty()));
    }


    public <V> DataResult<V> encode(T input, DynamicOps<V> ops, V prefix) {
        return ops.mergeToPrimitive(prefix, ops.createInt(input.ordinal()));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\EnumCodec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */