package iskallia.vault.util;

import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.influence.VaultAttributeInfluence;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class PlayerRecoveryHelper {
    @SubscribeEvent
    public static void onHeal(LivingHealEvent event) {
        LivingEntity healed = event.getEntityLiving();
        if (healed.getCommandSenderWorld().isClientSide()) {
            return;
        }
        if (!(healed instanceof ServerPlayerEntity)) {
            return;
        }
        ServerPlayerEntity sPlayer = (ServerPlayerEntity) healed;

        int rage = PlayerRageHelper.getCurrentRage((PlayerEntity) sPlayer, LogicalSide.SERVER);
        float multiplier = 1.0F;

        multiplier *= 1.0F - rage / 100.0F / 2.0F;

        VaultRaid vault = VaultRaidData.get(sPlayer.getLevel()).getActiveFor(sPlayer);
        if (vault != null) {
            for (VaultAttributeInfluence influence : vault.getInfluences().getInfluences(VaultAttributeInfluence.class)) {
                if (influence.getType() == VaultAttributeInfluence.Type.HEALING_EFFECTIVENESS && !influence.isMultiplicative()) {
                    multiplier += influence.getValue();
                }
            }
            for (VaultAttributeInfluence influence : vault.getInfluences().getInfluences(VaultAttributeInfluence.class)) {
                if (influence.getType() == VaultAttributeInfluence.Type.HEALING_EFFECTIVENESS && influence.isMultiplicative()) {
                    multiplier *= influence.getValue();
                }
            }
        }
        event.setAmount(event.getAmount() * multiplier);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\PlayerRecoveryHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */