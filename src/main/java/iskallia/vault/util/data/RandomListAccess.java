package iskallia.vault.util.data;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;
import java.util.function.BiConsumer;

public interface RandomListAccess<T> {
    @Nullable
    T getRandom(Random paramRandom);

    default Optional<T> getOptionalRandom(Random random) {
        return Optional.ofNullable(getRandom(random));
    }

    void forEach(BiConsumer<T, Number> paramBiConsumer);

    boolean removeEntry(T paramT);

    @Nullable
    default T removeRandom(Random random) {
        T element = getRandom(random);
        if (element != null) {
            removeEntry(element);
            return element;
        }
        return null;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\data\RandomListAccess.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */