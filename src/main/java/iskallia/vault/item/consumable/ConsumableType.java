package iskallia.vault.item.consumable;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum ConsumableType {
    BASIC,
    POWERUP;
    private static final Map<String, ConsumableType> STRING_TO_TYPE;

    static {
        STRING_TO_TYPE = (Map<String, ConsumableType>) Arrays.<ConsumableType>stream(values()).collect(Collectors.toMap(ConsumableType::toString, o -> o));
    }

    public static ConsumableType fromString(String name) {
        return STRING_TO_TYPE.get(name);
    }


    public String toString() {
        return name().toLowerCase();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\consumable\ConsumableType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */