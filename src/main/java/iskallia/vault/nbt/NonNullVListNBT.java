package iskallia.vault.nbt;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class NonNullVListNBT<T, N extends INBT>
        extends VListNBT<T, N> {
    public NonNullVListNBT(List<T> list, Function<T, N> write, Function<N, T> read) {
        super(list, write, read);
    }

    public NonNullVListNBT(Function<T, N> write, Function<N, T> read) {
        super(write, read);
    }


    public boolean add(T t) {
        if (t == null) {
            return false;
        }
        return super.add(t);
    }


    public void add(int index, T element) {
        if (element == null) {
            return;
        }
        super.add(index, element);
    }


    public boolean addAll(Collection<? extends T> c) {
        return super.addAll((Collection<? extends T>) c.stream().filter(Objects::nonNull).collect(Collectors.toList()));
    }


    public boolean addAll(int index, Collection<? extends T> c) {
        return super.addAll(index, (Collection<? extends T>) c.stream().filter(Objects::nonNull).collect(Collectors.toList()));
    }


    public T set(int index, T element) {
        if (element == null) {
            return null;
        }
        return super.set(index, element);
    }

    public static <T extends INBTSerializable<N>, N extends INBT> VListNBT<T, N> of(Function<N, T> read) {
        return new NonNullVListNBT<>(INBTSerializable::serializeNBT, read);
    }

    public static <T extends INBTSerializable<N>, N extends INBT> VListNBT<T, N> of(List<T> list, Function<N, T> read) {
        return new NonNullVListNBT<>(list, INBTSerializable::serializeNBT, read);
    }

    public static <T extends INBTSerializable<N>, N extends INBT> VListNBT<T, N> of(Supplier<T> supplier) {
        return new NonNullVListNBT<>(INBTSerializable::serializeNBT, n -> {
            T iNBTSerializable = supplier.get();
            iNBTSerializable.deserializeNBT(n);
            return iNBTSerializable;
        });
    }


    public static VListNBT<UUID, StringNBT> ofUUID() {
        return new NonNullVListNBT<>(uuid -> StringNBT.valueOf(uuid.toString()), stringNBT -> UUID.fromString(stringNBT.getAsString()));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\nbt\NonNullVListNBT.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */