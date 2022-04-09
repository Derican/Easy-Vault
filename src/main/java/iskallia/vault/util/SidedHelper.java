// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.util;

import com.google.common.collect.Lists;
import iskallia.vault.client.gui.overlay.VaultBarOverlay;
import iskallia.vault.world.data.PlayerVaultStatsData;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.common.thread.SidedThreadGroups;

import java.util.List;

public class SidedHelper
{
    public static int getVaultLevel(final PlayerEntity player) {
        if (player instanceof ServerPlayerEntity) {
            return PlayerVaultStatsData.get(((ServerPlayerEntity)player).getLevel()).getVaultStats(player).getVaultLevel();
        }
        return getClientVaultLevel();
    }
    
    @OnlyIn(Dist.CLIENT)
    private static int getClientVaultLevel() {
        return VaultBarOverlay.vaultLevel;
    }
    
    public static List<? extends PlayerEntity> getSidedPlayers() {
        if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER) {
            final MinecraftServer srv = (MinecraftServer)LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
            return srv.getPlayerList().getPlayers();
        }
        return getClientSidePlayers();
    }
    
    @OnlyIn(Dist.CLIENT)
    private static List<PlayerEntity> getClientSidePlayers() {
        return Lists.newArrayList(new PlayerEntity[] { Minecraft.getInstance().player });
    }
}
