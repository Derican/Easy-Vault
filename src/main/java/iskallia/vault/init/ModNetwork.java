package iskallia.vault.init;

import iskallia.vault.network.message.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class ModNetwork {
    private static final String NETWORK_VERSION = "0.25.0";
    public static final SimpleChannel CHANNEL;
    private static int ID;

    public static void initialize() {
        ModNetwork.CHANNEL.registerMessage(nextId(), OpenSkillTreeMessage.class, OpenSkillTreeMessage::encode, OpenSkillTreeMessage::decode, OpenSkillTreeMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), VaultLevelMessage.class, VaultLevelMessage::encode, VaultLevelMessage::decode, VaultLevelMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), TalentUpgradeMessage.class, TalentUpgradeMessage::encode, TalentUpgradeMessage::decode, TalentUpgradeMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), AbilityKeyMessage.class, AbilityKeyMessage::encode, AbilityKeyMessage::decode, AbilityKeyMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), AbilityUpgradeMessage.class, AbilityUpgradeMessage::encode, AbilityUpgradeMessage::decode, AbilityUpgradeMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), AbilitySelectSpecializationMessage.class, AbilitySelectSpecializationMessage::encode, AbilitySelectSpecializationMessage::decode, AbilitySelectSpecializationMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), AbilityKnownOnesMessage.class, AbilityKnownOnesMessage::encode, AbilityKnownOnesMessage::decode, AbilityKnownOnesMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), AbilityFocusMessage.class, AbilityFocusMessage::encode, AbilityFocusMessage::decode, AbilityFocusMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), AbilityActivityMessage.class, AbilityActivityMessage::encode, AbilityActivityMessage::decode, AbilityActivityMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), VaultOverlayMessage.class, VaultOverlayMessage::encode, VaultOverlayMessage::decode, VaultOverlayMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), FighterSizeMessage.class, FighterSizeMessage::encode, FighterSizeMessage::decode, FighterSizeMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), VendingUIMessage.class, VendingUIMessage::encode, VendingUIMessage::decode, VendingUIMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), AdvancedVendingUIMessage.class, AdvancedVendingUIMessage::encode, AdvancedVendingUIMessage::decode, AdvancedVendingUIMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), RenameUIMessage.class, RenameUIMessage::encode, RenameUIMessage::decode, RenameUIMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), StepHeightMessage.class, StepHeightMessage::encode, StepHeightMessage::decode, StepHeightMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), BossMusicMessage.class, BossMusicMessage::encode, BossMusicMessage::decode, BossMusicMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), OmegaStatueUIMessage.class, OmegaStatueUIMessage::encode, OmegaStatueUIMessage::decode, OmegaStatueUIMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), VaultModifierMessage.class, VaultModifierMessage::encode, VaultModifierMessage::decode, VaultModifierMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), VaultGoalMessage.class, VaultGoalMessage::encode, VaultGoalMessage::decode, VaultGoalMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), AbilityQuickselectMessage.class, AbilityQuickselectMessage::encode, AbilityQuickselectMessage::decode, AbilityQuickselectMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), PlayerStatisticsMessage.class, PlayerStatisticsMessage::encode, PlayerStatisticsMessage::decode, PlayerStatisticsMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), PartyStatusMessage.class, PartyStatusMessage::encode, PartyStatusMessage::decode, PartyStatusMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), PartyMembersMessage.class, PartyMembersMessage::encode, PartyMembersMessage::decode, PartyMembersMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), SandEventUpdateMessage.class, SandEventUpdateMessage::encode, SandEventUpdateMessage::decode, SandEventUpdateMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), SandEventContributorMessage.class, SandEventContributorMessage::encode, SandEventContributorMessage::decode, SandEventContributorMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), EffectMessage.class, EffectMessage::encode, EffectMessage::decode, EffectMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), KnownTalentsMessage.class, KnownTalentsMessage::encode, KnownTalentsMessage::decode, KnownTalentsMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), ShardTradeMessage.class, ShardTradeMessage::encode, ShardTradeMessage::decode, ShardTradeMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), ShardTraderScreenMessage.class, ShardTraderScreenMessage::encode, ShardTraderScreenMessage::decode, ShardTraderScreenMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), SyncOversizedStackMessage.class, SyncOversizedStackMessage::encode, SyncOversizedStackMessage::decode, SyncOversizedStackMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), EternalInteractionMessage.class, EternalInteractionMessage::encode, EternalInteractionMessage::decode, EternalInteractionMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), EternalSyncMessage.class, EternalSyncMessage::encode, EternalSyncMessage::decode, EternalSyncMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), ShardGlobalTradeMessage.class, ShardGlobalTradeMessage::encode, ShardGlobalTradeMessage::decode, ShardGlobalTradeMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), RageSyncMessage.class, RageSyncMessage::encode, RageSyncMessage::decode, RageSyncMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), ActiveEternalMessage.class, ActiveEternalMessage::encode, ActiveEternalMessage::decode, ActiveEternalMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), GlobalDifficultyMessage.class, GlobalDifficultyMessage::encode, GlobalDifficultyMessage::decode, GlobalDifficultyMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), PlayerDamageMultiplierMessage.class, PlayerDamageMultiplierMessage::encode, PlayerDamageMultiplierMessage::decode, PlayerDamageMultiplierMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), VaultCharmControllerScrollMessage.class, VaultCharmControllerScrollMessage::encode, VaultCharmControllerScrollMessage::decode, VaultCharmControllerScrollMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), EnteredEyesoreDomainMessage.class, EnteredEyesoreDomainMessage::encode, EnteredEyesoreDomainMessage::decode, EnteredEyesoreDomainMessage::handle);
    }

    public static int nextId() {
        return ModNetwork.ID++;
    }

    static {
        CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation("the_vault", "network"), () -> NETWORK_VERSION, version -> version.equals(NETWORK_VERSION), version -> version.equals(NETWORK_VERSION));
        ModNetwork.ID = 0;
    }
}
