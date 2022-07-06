package iskallia.vault.init;

import iskallia.vault.client.gui.overlay.*;
import iskallia.vault.client.gui.overlay.goal.ObeliskGoalOverlay;
import iskallia.vault.client.gui.screen.*;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.inventory.ChestScreen;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ModScreens {
    public static void register(final FMLClientSetupEvent event) {
        ScreenManager.register(ModContainers.SKILL_TREE_CONTAINER, SkillTreeScreen::new);
        ScreenManager.register(ModContainers.VAULT_CRATE_CONTAINER, VaultCrateScreen::new);
        ScreenManager.register(ModContainers.VENDING_MACHINE_CONTAINER, VendingMachineScreen::new);
        ScreenManager.register(ModContainers.ADVANCED_VENDING_MACHINE_CONTAINER, AdvancedVendingMachineScreen::new);
        ScreenManager.register(ModContainers.RENAMING_CONTAINER, RenameScreen::new);
        ScreenManager.register(ModContainers.KEY_PRESS_CONTAINER, KeyPressScreen::new);
        ScreenManager.register(ModContainers.OMEGA_STATUE_CONTAINER, OmegaStatueScreen::new);
        ScreenManager.register(ModContainers.TRANSMOG_TABLE_CONTAINER, TransmogTableScreen::new);
        ScreenManager.register(ModContainers.SCAVENGER_CHEST_CONTAINER, ChestScreen::new);
        ScreenManager.register(ModContainers.CATALYST_DECRYPTION_CONTAINER, CatalystDecryptionScreen::new);
        ScreenManager.register(ModContainers.SHARD_POUCH_CONTAINER, ShardPouchScreen::new);
        ScreenManager.register(ModContainers.SHARD_TRADE_CONTAINER, ShardTradeScreen::new);
        ScreenManager.register(ModContainers.CRYOCHAMBER_CONTAINER, CryochamberScreen::new);
        ScreenManager.register(ModContainers.GLOBAL_DIFFICULTY_CONTAINER, GlobalDifficultyScreen::new);
        ScreenManager.register(ModContainers.ETCHING_TRADE_CONTAINER, EtchingTradeScreen::new);
        ScreenManager.register(ModContainers.VAULT_CHARM_CONTROLLER_CONTAINER, VaultCharmControllerScreen::new);
    }

    public static void registerOverlays() {
        MinecraftForge.EVENT_BUS.register(VaultBarOverlay.class);
        MinecraftForge.EVENT_BUS.register(AbilitiesOverlay.class);
        MinecraftForge.EVENT_BUS.register(AbilityVignetteOverlay.class);
        MinecraftForge.EVENT_BUS.register(VaultRaidOverlay.class);
        MinecraftForge.EVENT_BUS.register(VaultPartyOverlay.class);
        MinecraftForge.EVENT_BUS.register(PlayerRageOverlay.class);
        MinecraftForge.EVENT_BUS.register(PlayerArmorOverlay.class);
        MinecraftForge.EVENT_BUS.register(PlayerDamageOverlay.class);
        MinecraftForge.EVENT_BUS.register(EyesoreBossOverlay.class);
        MinecraftForge.EVENT_BUS.register(VaultGoalBossBarOverlay.class);
        MinecraftForge.EVENT_BUS.register(ObeliskGoalOverlay.class);
    }
}
