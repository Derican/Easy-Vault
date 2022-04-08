package iskallia.vault.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import iskallia.vault.Vault;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTDynamicOps;

import java.util.Optional;
import java.util.function.Consumer;


public class CodecUtils {
    public static <T> T readNBT(Codec<T> codec, CompoundNBT tag, String targetKey, T defaultValue) {
        return readNBT(codec, tag.get(targetKey)).orElse(defaultValue);
    }

    public static <T> T readNBT(Codec<T> codec, INBT nbt, T defaultValue) {
        return readNBT(codec, nbt).orElse(defaultValue);
    }

    public static <T> Optional<T> readNBT(Codec<T> codec, INBT nbt) {
        return codec.parse((DynamicOps) NBTDynamicOps.INSTANCE, nbt)
                .resultOrPartial(Vault.LOGGER::error);
    }

    public static <T> void writeNBT(Codec<T> codec, T value, CompoundNBT targetTag, String targetKey) {
        writeNBT(codec, value, nbt -> targetTag.put(targetKey, nbt));
    }

    public static <T> void writeNBT(Codec<T> codec, T value, Consumer<INBT> successConsumer) {
        codec.encodeStart((DynamicOps) NBTDynamicOps.INSTANCE, value)
                .resultOrPartial(Vault.LOGGER::error)
                .ifPresent(successConsumer);
    }


    public static <T> INBT writeNBT(Codec<T> codec, T value) {
        return (INBT) codec.encodeStart((DynamicOps) NBTDynamicOps.INSTANCE, value)
                .getOrThrow(false, Vault.LOGGER::error);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\CodecUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */