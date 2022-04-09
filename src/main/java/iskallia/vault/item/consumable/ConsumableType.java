// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.item.consumable;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ConsumableType
{
    BASIC, 
    POWERUP;
    
    private static final Map<String, ConsumableType> STRING_TO_TYPE;
    
    public static ConsumableType fromString(final String name) {
        return ConsumableType.STRING_TO_TYPE.get(name);
    }
    
    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
    
    static {
        STRING_TO_TYPE = Arrays.stream(values()).collect(Collectors.toMap((Function<? super ConsumableType, ? extends String>)ConsumableType::toString, o -> o));
    }
}
