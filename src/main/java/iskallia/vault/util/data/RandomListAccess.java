// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.util.data;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;
import java.util.function.BiConsumer;

public interface RandomListAccess<T>
{
    @Nullable
    T getRandom(final Random p0);
    
    default Optional<T> getOptionalRandom(final Random random) {
        return Optional.ofNullable(this.getRandom(random));
    }
    
    void forEach(final BiConsumer<T, Number> p0);
    
    boolean removeEntry(final T p0);
    
    @Nullable
    default T removeRandom(final Random random) {
        final T element = this.getRandom(random);
        if (element != null) {
            this.removeEntry(element);
            return element;
        }
        return null;
    }
}
