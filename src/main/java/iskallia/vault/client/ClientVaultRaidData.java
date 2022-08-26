package iskallia.vault.client;

import iskallia.vault.world.vault.modifier.VaultModifier;
import iskallia.vault.network.message.VaultModifierMessage;
import iskallia.vault.network.message.BossMusicMessage;
import net.minecraft.world.World;
import iskallia.vault.Vault;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import iskallia.vault.client.vault.goal.VaultGoalData;
import iskallia.vault.client.vault.VaultMusicHandler;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import iskallia.vault.world.vault.modifier.VaultModifiers;
import iskallia.vault.network.message.VaultOverlayMessage;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class ClientVaultRaidData {
    private static int remainingTicks;
    private static boolean canGetRecordTime;
    private static VaultOverlayMessage.OverlayType type;
    private static boolean showTimer;
    private static VaultModifiers modifiers;
    private static boolean inBossFight;

    public static int getRemainingTicks() {
        return ClientVaultRaidData.remainingTicks;
    }

    public static boolean canGetRecordTime() {
        return ClientVaultRaidData.canGetRecordTime;
    }

    public static VaultOverlayMessage.OverlayType getOverlayType() {
        return ClientVaultRaidData.type;
    }

    public static boolean showTimer() {
        return ClientVaultRaidData.showTimer;
    }

    public static VaultModifiers getModifiers() {
        return ClientVaultRaidData.modifiers;
    }

    public static boolean isInBossFight() {
        return ClientVaultRaidData.inBossFight;
    }

    @SubscribeEvent
    public static void onDisconnect(final ClientPlayerNetworkEvent.LoggedOutEvent event) {
        ClientVaultRaidData.inBossFight = false;
        VaultMusicHandler.stopBossLoop();
        ClientVaultRaidData.type = VaultOverlayMessage.OverlayType.NONE;
        VaultGoalData.CURRENT_DATA = null;
    }

    @SubscribeEvent
    public static void onTick(final TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            return;
        }
        final World clientWorld = Minecraft.getInstance().level;
        if (clientWorld == null || clientWorld.dimension() == Vault.VAULT_KEY) {
            return;
        }
        ClientVaultRaidData.type = VaultOverlayMessage.OverlayType.NONE;
        ClientVaultRaidData.modifiers = new VaultModifiers();
        ClientVaultRaidData.inBossFight = false;
        VaultMusicHandler.stopBossLoop();
        VaultGoalData.CURRENT_DATA = null;
    }

    public static void receiveBossUpdate(final BossMusicMessage bossMessage) {
        ClientVaultRaidData.inBossFight = bossMessage.isInFight();
    }

    public static void receiveOverlayUpdate(final VaultOverlayMessage overlayMessage) {
        ClientVaultRaidData.remainingTicks = overlayMessage.getRemainingTicks();
        ClientVaultRaidData.canGetRecordTime = overlayMessage.canGetRecordTime();
        ClientVaultRaidData.type = overlayMessage.getOverlayType();
        ClientVaultRaidData.showTimer = overlayMessage.showTimer();
    }

    public static void receiveModifierUpdate(final VaultModifierMessage message) {
        ClientVaultRaidData.modifiers = new VaultModifiers();
        message.getGlobalModifiers().forEach(modifier -> ClientVaultRaidData.modifiers.addTemporaryModifier(modifier, 0));
        message.getPlayerModifiers().forEach(modifier -> ClientVaultRaidData.modifiers.addTemporaryModifier(modifier, 0));
    }

    static {
        ClientVaultRaidData.remainingTicks = 0;
        ClientVaultRaidData.canGetRecordTime = false;
        ClientVaultRaidData.type = VaultOverlayMessage.OverlayType.NONE;
        ClientVaultRaidData.showTimer = true;
        ClientVaultRaidData.modifiers = new VaultModifiers();
        ClientVaultRaidData.inBossFight = false;
    }
}
