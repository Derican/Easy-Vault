// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import iskallia.vault.Vault;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTDynamicOps;

import java.util.Optional;
import java.util.function.Consumer;

public class CodecUtils
{
    public static <T> T readNBT(final Codec<T> codec, final CompoundNBT tag, final String targetKey, final T defaultValue) {
        return readNBT(codec, tag.get(targetKey)).orElse(defaultValue);
    }
    
    public static <T> T readNBT(final Codec<T> codec, final INBT nbt, final T defaultValue) {
        return readNBT(codec, nbt).orElse(defaultValue);
    }
    
    public static <T> Optional<T> readNBT(final Codec<T> codec, final INBT nbt) {
        return codec.parse((DynamicOps)NBTDynamicOps.INSTANCE, nbt).resultOrPartial(Vault.LOGGER::error);
    }
    
    public static <T> void writeNBT(final Codec<T> codec, final T value, final CompoundNBT targetTag, final String targetKey) {
        writeNBT(codec, value, nbt -> targetTag.put(targetKey, nbt));
    }
    
    public static <T> void writeNBT(final Codec<T> codec, final T value, final Consumer<INBT> successConsumer) {
        codec.encodeStart((DynamicOps)NBTDynamicOps.INSTANCE, value).resultOrPartial(Vault.LOGGER::error).ifPresent(successConsumer);
    }
    
    public static <T> INBT writeNBT(final Codec<T> codec, final T value) {
        return (INBT)codec.encodeStart((DynamicOps)NBTDynamicOps.INSTANCE, value).getOrThrow(false, Vault.LOGGER::error);
    }
}
