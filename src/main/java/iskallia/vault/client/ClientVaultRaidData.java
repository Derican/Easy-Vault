package iskallia.vault.client;

import iskallia.vault.Vault;
import iskallia.vault.client.vault.VaultMusicHandler;
import iskallia.vault.client.vault.goal.VaultGoalData;
import iskallia.vault.network.message.BossMusicMessage;
import iskallia.vault.network.message.VaultModifierMessage;
import iskallia.vault.network.message.VaultOverlayMessage;
import iskallia.vault.world.vault.modifier.VaultModifiers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber({Dist.CLIENT})
public class ClientVaultRaidData {
    private static int remainingTicks = 0;
    private static boolean canGetRecordTime = false;
    private static VaultOverlayMessage.OverlayType type = VaultOverlayMessage.OverlayType.NONE;
    private static VaultModifiers modifiers = new VaultModifiers();
    private static boolean inBossFight = false;

    public static int getRemainingTicks() {
        return remainingTicks;
    }

    public static boolean canGetRecordTime() {
        return canGetRecordTime;
    }

    public static VaultOverlayMessage.OverlayType getOverlayType() {
        return type;
    }

    public static VaultModifiers getModifiers() {
        return modifiers;
    }

    public static boolean isInBossFight() {
        return inBossFight;
    }

    @SubscribeEvent
    public static void onDisconnect(ClientPlayerNetworkEvent.LoggedOutEvent event) {
        inBossFight = false;
        VaultMusicHandler.stopBossLoop();
        type = VaultOverlayMessage.OverlayType.NONE;
        VaultGoalData.CURRENT_DATA = null;
    }

    @SubscribeEvent
    public static void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            return;
        }
        ClientWorld clientWorld = (Minecraft.getInstance()).level;
        if (clientWorld == null || clientWorld.dimension() == Vault.VAULT_KEY) {
            return;
        }
        type = VaultOverlayMessage.OverlayType.NONE;
        modifiers = new VaultModifiers();
        inBossFight = false;
        VaultMusicHandler.stopBossLoop();
        VaultGoalData.CURRENT_DATA = null;
    }

    public static void receiveBossUpdate(BossMusicMessage bossMessage) {
        inBossFight = bossMessage.isInFight();
    }

    public static void receiveOverlayUpdate(VaultOverlayMessage overlayMessage) {
        remainingTicks = overlayMessage.getRemainingTicks();
        canGetRecordTime = overlayMessage.canGetRecordTime();
        type = overlayMessage.getOverlayType();
    }

    public static void receiveModifierUpdate(VaultModifierMessage message) {
        modifiers = new VaultModifiers();
        message.getGlobalModifiers().forEach(modifier -> modifiers.addTemporaryModifier(modifier, 0));
        message.getPlayerModifiers().forEach(modifier -> modifiers.addTemporaryModifier(modifier, 0));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\ClientVaultRaidData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */