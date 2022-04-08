package iskallia.vault.util;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;

public class AdvancementHelper {
    public static boolean grantCriterion(ServerPlayerEntity player, ResourceLocation advancementId, String criterion) {
        MinecraftServer server = player.getServer();

        if (server == null) {
            return false;
        }

        AdvancementManager advancementManager = server.getAdvancements();
        Advancement advancement = advancementManager.getAdvancement(advancementId);

        if (advancement == null) {
            return false;
        }

        player.getAdvancements().award(advancement, criterion);
        return true;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\AdvancementHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */