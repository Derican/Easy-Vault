package iskallia.vault.init;

import iskallia.vault.client.gui.overlay.*;
import iskallia.vault.client.gui.overlay.goal.ObeliskGoalOverlay;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ModScreens {
    public static void register(FMLClientSetupEvent event) {
        ScreenManager.register(ModContainers.SKILL_TREE_CONTAINER, iskallia.vault.client.gui.screen.SkillTreeScreen::new);
        ScreenManager.register(ModContainers.VAULT_CRATE_CONTAINER, iskallia.vault.client.gui.screen.VaultCrateScreen::new);
        ScreenManager.register(ModContainers.VENDING_MACHINE_CONTAINER, iskallia.vault.client.gui.screen.VendingMachineScreen::new);
        ScreenManager.register(ModContainers.ADVANCED_VENDING_MACHINE_CONTAINER, iskallia.vault.client.gui.screen.AdvancedVendingMachineScreen::new);
        ScreenManager.register(ModContainers.RENAMING_CONTAINER, iskallia.vault.client.gui.screen.RenameScreen::new);
        ScreenManager.register(ModContainers.KEY_PRESS_CONTAINER, iskallia.vault.client.gui.screen.KeyPressScreen::new);
        ScreenManager.register(ModContainers.OMEGA_STATUE_CONTAINER, iskallia.vault.client.gui.screen.OmegaStatueScreen::new);
        ScreenManager.register(ModContainers.TRANSMOG_TABLE_CONTAINER, iskallia.vault.client.gui.screen.TransmogTableScreen::new);
        ScreenManager.register(ModContainers.SCAVENGER_CHEST_CONTAINER, net.minecraft.client.gui.screen.inventory.ChestScreen::new);
        ScreenManager.register(ModContainers.CATALYST_DECRYPTION_CONTAINER, iskallia.vault.client.gui.screen.CatalystDecryptionScreen::new);
        ScreenManager.register(ModContainers.SHARD_POUCH_CONTAINER, iskallia.vault.client.gui.screen.ShardPouchScreen::new);
        ScreenManager.register(ModContainers.SHARD_TRADE_CONTAINER, iskallia.vault.client.gui.screen.ShardTradeScreen::new);
        ScreenManager.register(ModContainers.CRYOCHAMBER_CONTAINER, iskallia.vault.client.gui.screen.CryochamberScreen::new);
        ScreenManager.register(ModContainers.GLOBAL_DIFFICULTY_CONTAINER, iskallia.vault.client.gui.screen.GlobalDifficultyScreen::new);
        ScreenManager.register(ModContainers.ETCHING_TRADE_CONTAINER, iskallia.vault.client.gui.screen.EtchingTradeScreen::new);
        ScreenManager.register(ModContainers.VAULT_CHARM_CONTROLLER_CONTAINER, iskallia.vault.client.gui.screen.VaultCharmControllerScreen::new);
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

        MinecraftForge.EVENT_BUS.register(VaultGoalBossBarOverlay.class);
        MinecraftForge.EVENT_BUS.register(ObeliskGoalOverlay.class);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\init\ModScreens.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */