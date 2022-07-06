// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.util;

import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.stream.StreamSupport;
import java.util.Map;

public class ListHelper
{
    public static <T> void traverseOccurrences(final Iterable<T> iterable, final OccurrenceConsumer<T> consumer) {
        final Map<T, Long> occurrenceMap = StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.groupingBy((Function<? super T, ? extends T>)Function.identity(), Collectors.counting()));
        int index = 0;
        for (final Map.Entry<T, Long> entry : occurrenceMap.entrySet()) {
            final T item = entry.getKey();
            final Long occurrence = entry.getValue();
            consumer.consume(index, item, occurrence);
            ++index;
        }
    }
    
    @FunctionalInterface
    public interface OccurrenceConsumer<T>
    {
        void consume(final int p0, final T p1, final long p2);
    }
}
