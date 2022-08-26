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

public class NonNullVListNBT<T, N extends INBT> extends VListNBT<T, N> {
    public NonNullVListNBT(final List<T> list, final Function<T, N> write, final Function<N, T> read) {
        super(list, write, read);
    }

    public NonNullVListNBT(final Function<T, N> write, final Function<N, T> read) {
        super(write, read);
    }

    @Override
    public boolean add(final T t) {
        return t != null && super.add(t);
    }

    @Override
    public void add(final int index, final T element) {
        if (element == null) {
            return;
        }
        super.add(index, element);
    }

    @Override
    public boolean addAll(final Collection<? extends T> c) {
        return super.addAll(c.stream().filter(Objects::nonNull).collect(Collectors.toList()));
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends T> c) {
        return super.addAll(index, c.stream().filter(Objects::nonNull).collect(Collectors.toList()));
    }

    @Override
    public T set(final int index, final T element) {
        if (element == null) {
            return null;
        }
        return super.set(index, element);
    }

    public static <T extends INBTSerializable<N>, N extends INBT> VListNBT<T, N> of(final Function<N, T> read) {
        return new NonNullVListNBT<T, N>(INBTSerializable::serializeNBT, read);
    }

    public static <T extends INBTSerializable<N>, N extends INBT> VListNBT<T, N> of(final List<T> list, final Function<N, T> read) {
        return new NonNullVListNBT<T, N>(list, INBTSerializable::serializeNBT, read);
    }

    public static <T extends INBTSerializable<N>, N extends INBT> VListNBT<T, N> of(final Supplier<T> supplier) {
        return new NonNullVListNBT<T, N>(INBTSerializable::serializeNBT, n -> {
            final T value = supplier.get();
            value.deserializeNBT(n);
            return value;
        });
    }

    public static VListNBT<UUID, StringNBT> ofUUID() {
        return new NonNullVListNBT<UUID, StringNBT>(uuid -> StringNBT.valueOf(uuid.toString()), stringNBT -> UUID.fromString(stringNBT.getAsString()));
    }
}
