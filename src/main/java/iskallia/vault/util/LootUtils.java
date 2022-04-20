package iskallia.vault.util;

import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameter;
import net.minecraft.loot.LootParameterSet;

public class LootUtils {
    public static boolean doesContextFulfillSet(final LootContext ctx, final LootParameterSet set) {
        for (final LootParameter<?> required : set.getRequired()) {
            if (!ctx.hasParam((LootParameter) required)) {
                return false;
            }
        }
        return true;
    }
}
