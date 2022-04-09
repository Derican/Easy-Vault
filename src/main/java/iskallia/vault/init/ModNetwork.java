// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.init;

import iskallia.vault.network.message.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class ModNetwork
{
    private static final String NETWORK_VERSION = "0.24.0";
    public static final SimpleChannel CHANNEL;
    private static int ID;
    
    public static void initialize() {
        ModNetwork.CHANNEL.registerMessage(nextId(), (Class)OpenSkillTreeMessage.class, OpenSkillTreeMessage::encode, OpenSkillTreeMessage::decode, OpenSkillTreeMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), (Class)VaultLevelMessage.class, VaultLevelMessage::encode, VaultLevelMessage::decode, VaultLevelMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), (Class)TalentUpgradeMessage.class, TalentUpgradeMessage::encode, TalentUpgradeMessage::decode, TalentUpgradeMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), (Class)ResearchMessage.class, ResearchMessage::encode, ResearchMessage::decode, ResearchMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), (Class)ResearchTreeMessage.class, ResearchTreeMessage::encode, ResearchTreeMessage::decode, ResearchTreeMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), (Class)AbilityKeyMessage.class, AbilityKeyMessage::encode, AbilityKeyMessage::decode, AbilityKeyMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), (Class)AbilityUpgradeMessage.class, AbilityUpgradeMessage::encode, AbilityUpgradeMessage::decode, AbilityUpgradeMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), (Class)AbilitySelectSpecializationMessage.class, AbilitySelectSpecializationMessage::encode, AbilitySelectSpecializationMessage::decode, AbilitySelectSpecializationMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), (Class)AbilityKnownOnesMessage.class, AbilityKnownOnesMessage::encode, AbilityKnownOnesMessage::decode, AbilityKnownOnesMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), (Class)AbilityFocusMessage.class, AbilityFocusMessage::encode, AbilityFocusMessage::decode, AbilityFocusMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), (Class)AbilityActivityMessage.class, AbilityActivityMessage::encode, AbilityActivityMessage::decode, AbilityActivityMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), (Class)VaultOverlayMessage.class, VaultOverlayMessage::encode, VaultOverlayMessage::decode, VaultOverlayMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), (Class)FighterSizeMessage.class, FighterSizeMessage::encode, FighterSizeMessage::decode, FighterSizeMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), (Class)VendingUIMessage.class, VendingUIMessage::encode, VendingUIMessage::decode, VendingUIMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), (Class)AdvancedVendingUIMessage.class, AdvancedVendingUIMessage::encode, AdvancedVendingUIMessage::decode, AdvancedVendingUIMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), (Class)RenameUIMessage.class, RenameUIMessage::encode, RenameUIMessage::decode, RenameUIMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), (Class)StepHeightMessage.class, StepHeightMessage::encode, StepHeightMessage::decode, StepHeightMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), (Class)BossMusicMessage.class, BossMusicMessage::encode, BossMusicMessage::decode, BossMusicMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), (Class)OmegaStatueUIMessage.class, OmegaStatueUIMessage::encode, OmegaStatueUIMessage::decode, OmegaStatueUIMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), (Class)VaultModifierMessage.class, VaultModifierMessage::encode, VaultModifierMessage::decode, VaultModifierMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), (Class)VaultGoalMessage.class, VaultGoalMessage::encode, VaultGoalMessage::decode, VaultGoalMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), (Class)AbilityQuickselectMessage.class, AbilityQuickselectMessage::encode, AbilityQuickselectMessage::decode, AbilityQuickselectMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), (Class)PlayerStatisticsMessage.class, PlayerStatisticsMessage::encode, PlayerStatisticsMessage::decode, PlayerStatisticsMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), (Class)PartyStatusMessage.class, PartyStatusMessage::encode, PartyStatusMessage::decode, PartyStatusMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), (Class)PartyMembersMessage.class, PartyMembersMessage::encode, PartyMembersMessage::decode, PartyMembersMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), (Class)EffectMessage.class, EffectMessage::encode, EffectMessage::decode, EffectMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), (Class)KnownTalentsMessage.class, KnownTalentsMessage::encode, KnownTalentsMessage::decode, KnownTalentsMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), (Class)ShardTradeMessage.class, ShardTradeMessage::encode, ShardTradeMessage::decode, ShardTradeMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), (Class)ShardTraderScreenMessage.class, ShardTraderScreenMessage::encode, ShardTraderScreenMessage::decode, ShardTraderScreenMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), (Class)SyncOversizedStackMessage.class, SyncOversizedStackMessage::encode, SyncOversizedStackMessage::decode, SyncOversizedStackMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), (Class)EternalInteractionMessage.class, EternalInteractionMessage::encode, EternalInteractionMessage::decode, EternalInteractionMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), (Class)EternalSyncMessage.class, EternalSyncMessage::encode, EternalSyncMessage::decode, EternalSyncMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), (Class)ShardGlobalTradeMessage.class, ShardGlobalTradeMessage::encode, ShardGlobalTradeMessage::decode, ShardGlobalTradeMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), (Class)RageSyncMessage.class, RageSyncMessage::encode, RageSyncMessage::decode, RageSyncMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), (Class)ActiveEternalMessage.class, ActiveEternalMessage::encode, ActiveEternalMessage::decode, ActiveEternalMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), (Class)GlobalDifficultyMessage.class, GlobalDifficultyMessage::encode, GlobalDifficultyMessage::decode, GlobalDifficultyMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), (Class)PlayerDamageMultiplierMessage.class, PlayerDamageMultiplierMessage::encode, PlayerDamageMultiplierMessage::decode, PlayerDamageMultiplierMessage::handle);
        ModNetwork.CHANNEL.registerMessage(nextId(), (Class)VaultCharmControllerScrollMessage.class, VaultCharmControllerScrollMessage::encode, VaultCharmControllerScrollMessage::decode, VaultCharmControllerScrollMessage::handle);
    }
    
    public static int nextId() {
        return ModNetwork.ID++;
    }
    
    static {
        CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation("the_vault", "network"), () -> "0.24.0", version -> version.equals("0.24.0"), version -> version.equals("0.24.0"));
        ModNetwork.ID = 0;
    }
}
