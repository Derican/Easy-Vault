package iskallia.vault.util.nbt;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class NBTHelper {
    public static CompoundNBT serializeBlockPos(final BlockPos pos) {
        final CompoundNBT tag = new CompoundNBT();
        tag.putInt("posX", pos.getX());
        tag.putInt("posY", pos.getY());
        tag.putInt("posZ", pos.getZ());
        return tag;
    }

    public static BlockPos deserializeBlockPos(final CompoundNBT tag) {
        final int x = tag.getInt("posX");
        final int y = tag.getInt("posY");
        final int z = tag.getInt("posZ");
        return new BlockPos(x, y, z);
    }

    public static <T, N extends INBT> Map<UUID, T> readMap(final CompoundNBT nbt, final String name, final Class<N> nbtType, final Function<N, T> mapper) {
        final Map<UUID, T> res = new HashMap<UUID, T>();
        final ListNBT uuidList = nbt.getList(name + "Keys", 8);
        final ListNBT valuesList = (ListNBT) nbt.get(name + "Values");
        if (uuidList.size() != valuesList.size()) {
            throw new IllegalStateException("Map doesn't have the same amount of keys as values");
        }
        for (int i = 0; i < uuidList.size(); ++i) {
            res.put(UUID.fromString(uuidList.get(i).getAsString()), mapper.apply((N) valuesList.get(i)));
        }
        return res;
    }

    public static <T, N extends INBT> void writeMap(final CompoundNBT nbt, final String name, final Map<UUID, T> map, final Class<N> nbtType, final Function<T, N> mapper) {
        final ListNBT uuidList = new ListNBT();
        final ListNBT valuesList = new ListNBT();
        map.forEach((key, value) -> {
            uuidList.add(StringNBT.valueOf(key.toString()));
            valuesList.add(mapper.apply(value));
            return;
        });
        nbt.put(name + "Keys", uuidList);
        nbt.put(name + "Values", valuesList);
    }

    public static <T, N extends INBT> List<T> readList(final CompoundNBT nbt, final String name, final Class<N> nbtType, final Function<N, T> mapper) {
        final List<T> res = new LinkedList<T>();
        final ListNBT listNBT = (ListNBT) nbt.get(name);
        for (final INBT inbt : listNBT) {
            res.add(mapper.apply((N) inbt));
        }
        return res;
    }

    public static <T, N extends INBT> void writeList(final CompoundNBT nbt, final String name, final Collection<T> list, final Class<N> nbtType, final Function<T, N> mapper) {
        final ListNBT listNBT = new ListNBT();
        list.forEach(item -> listNBT.add(mapper.apply(item)));
        nbt.put(name, listNBT);
    }

    public static <T extends Enum<T>> void writeEnum(final CompoundNBT nbt, final String key, final T enumValue) {
        nbt.putInt(key, enumValue.ordinal());
    }

    public static <T extends Enum<T>> T readEnum(final CompoundNBT nbt, final String key, final Class<T> enumClazz) {
        if (!enumClazz.isEnum()) {
            throw new IllegalArgumentException("Passed class is not an enum!");
        }
        return enumClazz.getEnumConstants()[nbt.getInt(key)];
    }

    public static <T> void writeOptional(final CompoundNBT nbt, final String key, @Nullable final T object, final BiConsumer<CompoundNBT, T> writer) {
        nbt.putBoolean(key + "_present", object != null);
        if (object != null) {
            final CompoundNBT write = new CompoundNBT();
            writer.accept(write, object);
            nbt.put(key, write);
        }
    }

    @Nullable
    public static <T> T readOptional(final CompoundNBT nbt, final String key, final Function<CompoundNBT, T> reader) {
        return readOptional(nbt, key, reader, null);
    }

    @Nullable
    public static <T> T readOptional(final CompoundNBT nbt, final String key, final Function<CompoundNBT, T> reader, final T _default) {
        if (nbt.getBoolean(key + "_present")) {
            final CompoundNBT read = nbt.getCompound(key);
            return reader.apply(read);
        }
        return _default;
    }
}
