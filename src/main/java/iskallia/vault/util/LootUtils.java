package iskallia.vault.util;

import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameter;
import net.minecraft.loot.LootParameterSet;

public class LootUtils {
    public static boolean doesContextFulfillSet(LootContext ctx, LootParameterSet set) {
        for (LootParameter<?> required : (Iterable<LootParameter<?>>) set.getRequired()) {
            if (!ctx.hasParam(required)) {
                return false;
            }
        }
        return true;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\LootUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */