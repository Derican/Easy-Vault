// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.util;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;

public class AdvancementHelper
{
    public static boolean grantCriterion(final ServerPlayerEntity player, final ResourceLocation advancementId, final String criterion) {
        final MinecraftServer server = player.getServer();
        if (server == null) {
            return false;
        }
        final AdvancementManager advancementManager = server.getAdvancements();
        final Advancement advancement = advancementManager.getAdvancement(advancementId);
        if (advancement == null) {
            return false;
        }
        player.getAdvancements().award(advancement, criterion);
        return true;
    }
}
