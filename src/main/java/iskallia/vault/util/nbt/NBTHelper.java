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
    public static CompoundNBT serializeBlockPos(BlockPos pos) {
        CompoundNBT tag = new CompoundNBT();
        tag.putInt("posX", pos.getX());
        tag.putInt("posY", pos.getY());
        tag.putInt("posZ", pos.getZ());
        return tag;
    }

    public static BlockPos deserializeBlockPos(CompoundNBT tag) {
        int x = tag.getInt("posX");
        int y = tag.getInt("posY");
        int z = tag.getInt("posZ");
        return new BlockPos(x, y, z);
    }

    public static <T, N extends INBT> Map<UUID, T> readMap(CompoundNBT nbt, String name, Class<N> nbtType, Function<N, T> mapper) {
        Map<UUID, T> res = new HashMap<>();
        ListNBT uuidList = nbt.getList(name + "Keys", 8);
        ListNBT valuesList = (ListNBT) nbt.get(name + "Values");

        if (uuidList.size() != valuesList.size()) {
            throw new IllegalStateException("Map doesn't have the same amount of keys as values");
        }

        for (int i = 0; i < uuidList.size(); i++) {
            res.put(UUID.fromString(uuidList.get(i).getAsString()), mapper.apply((N) valuesList.get(i)));
        }

        return res;
    }

    public static <T, N extends INBT> void writeMap(CompoundNBT nbt, String name, Map<UUID, T> map, Class<N> nbtType, Function<T, N> mapper) {
        ListNBT uuidList = new ListNBT();
        ListNBT valuesList = new ListNBT();
        map.forEach((key, value) -> {
            uuidList.add(StringNBT.valueOf(key.toString()));
            valuesList.add(mapper.apply(value));
        });
        nbt.put(name + "Keys", (INBT) uuidList);
        nbt.put(name + "Values", (INBT) valuesList);
    }

    public static <T, N extends INBT> List<T> readList(CompoundNBT nbt, String name, Class<N> nbtType, Function<N, T> mapper) {
        List<T> res = new LinkedList<>();
        ListNBT listNBT = (ListNBT) nbt.get(name);
        for (INBT inbt : listNBT) {
            res.add(mapper.apply((N) inbt));
        }
        return res;
    }

    public static <T, N extends INBT> void writeList(CompoundNBT nbt, String name, Collection<T> list, Class<N> nbtType, Function<T, N> mapper) {
        ListNBT listNBT = new ListNBT();
        list.forEach(item -> listNBT.add(mapper.apply(item)));
        nbt.put(name, (INBT) listNBT);
    }

    public static <T> void writeOptional(CompoundNBT nbt, String key, @Nullable T object, BiConsumer<CompoundNBT, T> writer) {
        nbt.putBoolean(key + "_present", (object != null));
        if (object != null) {
            CompoundNBT write = new CompoundNBT();
            writer.accept(write, object);
            nbt.put(key, (INBT) write);
        }
    }

    @Nullable
    public static <T> T readOptional(CompoundNBT nbt, String key, Function<CompoundNBT, T> reader) {
        return readOptional(nbt, key, reader, null);
    }

    @Nullable
    public static <T> T readOptional(CompoundNBT nbt, String key, Function<CompoundNBT, T> reader, T _default) {
        if (nbt.getBoolean(key + "_present")) {
            CompoundNBT read = nbt.getCompound(key);
            return reader.apply(read);
        }
        return _default;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\nbt\NBTHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */