package iskallia.vault.util;

import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.influence.VaultAttributeInfluence;
import iskallia.vault.world.vault.influence.VaultInfluence;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class PlayerRecoveryHelper {
    @SubscribeEvent
    public static void onHeal(final LivingHealEvent event) {
        final LivingEntity healed = event.getEntityLiving();
        if (healed.getCommandSenderWorld().isClientSide()) {
            return;
        }
        if (!(healed instanceof ServerPlayerEntity)) {
            return;
        }
        final ServerPlayerEntity sPlayer = (ServerPlayerEntity) healed;
        final int rage = PlayerRageHelper.getCurrentRage(sPlayer, LogicalSide.SERVER);
        float multiplier = 1.0f;
        multiplier *= 1.0f - rage / 100.0f / 2.0f;
        final VaultRaid vault = VaultRaidData.get(sPlayer.getLevel()).getActiveFor(sPlayer);
        if (vault != null) {
            for (final VaultInfluence influence : vault.getInfluences().getInfluences(VaultAttributeInfluence.class)) {
                if (((VaultAttributeInfluence) influence).getType() == VaultAttributeInfluence.Type.HEALING_EFFECTIVENESS && !((VaultAttributeInfluence) influence).isMultiplicative()) {
                    multiplier += ((VaultAttributeInfluence) influence).getValue();
                }
            }
            for (final VaultInfluence influence : vault.getInfluences().getInfluences(VaultAttributeInfluence.class)) {
                if (((VaultAttributeInfluence) influence).getType() == VaultAttributeInfluence.Type.HEALING_EFFECTIVENESS && ((VaultAttributeInfluence) influence).isMultiplicative()) {
                    multiplier *= ((VaultAttributeInfluence) influence).getValue();
                }
            }
        }
        event.setAmount(event.getAmount() * multiplier);
    }
}
