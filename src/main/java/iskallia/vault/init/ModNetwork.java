package iskallia.vault.init;

import iskallia.vault.network.message.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class ModNetwork {
    static {
        CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation("the_vault", "network"), () -> "0.24.0", version -> version.equals("0.24.0"), version -> version.equals("0.24.0"));
    }


    private static final String NETWORK_VERSION = "0.24.0";

    public static final SimpleChannel CHANNEL;
    private static int ID = 0;

    public static void initialize() {
        CHANNEL.registerMessage(nextId(), OpenSkillTreeMessage.class, OpenSkillTreeMessage::encode, OpenSkillTreeMessage::decode, OpenSkillTreeMessage::handle);


        CHANNEL.registerMessage(nextId(), VaultLevelMessage.class, VaultLevelMessage::encode, VaultLevelMessage::decode, VaultLevelMessage::handle);


        CHANNEL.registerMessage(nextId(), TalentUpgradeMessage.class, TalentUpgradeMessage::encode, TalentUpgradeMessage::decode, TalentUpgradeMessage::handle);


        CHANNEL.registerMessage(nextId(), ResearchMessage.class, ResearchMessage::encode, ResearchMessage::decode, ResearchMessage::handle);


        CHANNEL.registerMessage(nextId(), ResearchTreeMessage.class, ResearchTreeMessage::encode, ResearchTreeMessage::decode, ResearchTreeMessage::handle);


        CHANNEL.registerMessage(nextId(), AbilityKeyMessage.class, AbilityKeyMessage::encode, AbilityKeyMessage::decode, AbilityKeyMessage::handle);


        CHANNEL.registerMessage(nextId(), AbilityUpgradeMessage.class, AbilityUpgradeMessage::encode, AbilityUpgradeMessage::decode, AbilityUpgradeMessage::handle);


        CHANNEL.registerMessage(nextId(), AbilitySelectSpecializationMessage.class, AbilitySelectSpecializationMessage::encode, AbilitySelectSpecializationMessage::decode, AbilitySelectSpecializationMessage::handle);


        CHANNEL.registerMessage(nextId(), AbilityKnownOnesMessage.class, AbilityKnownOnesMessage::encode, AbilityKnownOnesMessage::decode, AbilityKnownOnesMessage::handle);


        CHANNEL.registerMessage(nextId(), AbilityFocusMessage.class, AbilityFocusMessage::encode, AbilityFocusMessage::decode, AbilityFocusMessage::handle);


        CHANNEL.registerMessage(nextId(), AbilityActivityMessage.class, AbilityActivityMessage::encode, AbilityActivityMessage::decode, AbilityActivityMessage::handle);


        CHANNEL.registerMessage(nextId(), VaultOverlayMessage.class, VaultOverlayMessage::encode, VaultOverlayMessage::decode, VaultOverlayMessage::handle);


        CHANNEL.registerMessage(nextId(), FighterSizeMessage.class, FighterSizeMessage::encode, FighterSizeMessage::decode, FighterSizeMessage::handle);


        CHANNEL.registerMessage(nextId(), VendingUIMessage.class, VendingUIMessage::encode, VendingUIMessage::decode, VendingUIMessage::handle);


        CHANNEL.registerMessage(nextId(), AdvancedVendingUIMessage.class, AdvancedVendingUIMessage::encode, AdvancedVendingUIMessage::decode, AdvancedVendingUIMessage::handle);


        CHANNEL.registerMessage(nextId(), RenameUIMessage.class, RenameUIMessage::encode, RenameUIMessage::decode, RenameUIMessage::handle);


        CHANNEL.registerMessage(nextId(), StepHeightMessage.class, StepHeightMessage::encode, StepHeightMessage::decode, StepHeightMessage::handle);


        CHANNEL.registerMessage(nextId(), BossMusicMessage.class, BossMusicMessage::encode, BossMusicMessage::decode, BossMusicMessage::handle);


        CHANNEL.registerMessage(nextId(), OmegaStatueUIMessage.class, OmegaStatueUIMessage::encode, OmegaStatueUIMessage::decode, OmegaStatueUIMessage::handle);


        CHANNEL.registerMessage(nextId(), VaultModifierMessage.class, VaultModifierMessage::encode, VaultModifierMessage::decode, VaultModifierMessage::handle);


        CHANNEL.registerMessage(nextId(), VaultGoalMessage.class, VaultGoalMessage::encode, VaultGoalMessage::decode, VaultGoalMessage::handle);


        CHANNEL.registerMessage(nextId(), AbilityQuickselectMessage.class, AbilityQuickselectMessage::encode, AbilityQuickselectMessage::decode, AbilityQuickselectMessage::handle);


        CHANNEL.registerMessage(nextId(), PlayerStatisticsMessage.class, PlayerStatisticsMessage::encode, PlayerStatisticsMessage::decode, PlayerStatisticsMessage::handle);


        CHANNEL.registerMessage(nextId(), PartyStatusMessage.class, PartyStatusMessage::encode, PartyStatusMessage::decode, PartyStatusMessage::handle);


        CHANNEL.registerMessage(nextId(), PartyMembersMessage.class, PartyMembersMessage::encode, PartyMembersMessage::decode, PartyMembersMessage::handle);


        CHANNEL.registerMessage(nextId(), EffectMessage.class, EffectMessage::encode, EffectMessage::decode, EffectMessage::handle);


        CHANNEL.registerMessage(nextId(), KnownTalentsMessage.class, KnownTalentsMessage::encode, KnownTalentsMessage::decode, KnownTalentsMessage::handle);


        CHANNEL.registerMessage(nextId(), ShardTradeMessage.class, ShardTradeMessage::encode, ShardTradeMessage::decode, ShardTradeMessage::handle);


        CHANNEL.registerMessage(nextId(), ShardTraderScreenMessage.class, ShardTraderScreenMessage::encode, ShardTraderScreenMessage::decode, ShardTraderScreenMessage::handle);


        CHANNEL.registerMessage(nextId(), SyncOversizedStackMessage.class, SyncOversizedStackMessage::encode, SyncOversizedStackMessage::decode, SyncOversizedStackMessage::handle);


        CHANNEL.registerMessage(nextId(), EternalInteractionMessage.class, EternalInteractionMessage::encode, EternalInteractionMessage::decode, EternalInteractionMessage::handle);


        CHANNEL.registerMessage(nextId(), EternalSyncMessage.class, EternalSyncMessage::encode, EternalSyncMessage::decode, EternalSyncMessage::handle);


        CHANNEL.registerMessage(nextId(), ShardGlobalTradeMessage.class, ShardGlobalTradeMessage::encode, ShardGlobalTradeMessage::decode, ShardGlobalTradeMessage::handle);


        CHANNEL.registerMessage(nextId(), RageSyncMessage.class, RageSyncMessage::encode, RageSyncMessage::decode, RageSyncMessage::handle);


        CHANNEL.registerMessage(nextId(), ActiveEternalMessage.class, ActiveEternalMessage::encode, ActiveEternalMessage::decode, ActiveEternalMessage::handle);


        CHANNEL.registerMessage(nextId(), GlobalDifficultyMessage.class, GlobalDifficultyMessage::encode, GlobalDifficultyMessage::decode, GlobalDifficultyMessage::handle);


        CHANNEL.registerMessage(nextId(), PlayerDamageMultiplierMessage.class, PlayerDamageMultiplierMessage::encode, PlayerDamageMultiplierMessage::decode, PlayerDamageMultiplierMessage::handle);


        CHANNEL.registerMessage(nextId(), VaultCharmControllerScrollMessage.class, VaultCharmControllerScrollMessage::encode, VaultCharmControllerScrollMessage::decode, VaultCharmControllerScrollMessage::handle);
    }


    public static int nextId() {
        return ID++;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\init\ModNetwork.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */