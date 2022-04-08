package iskallia.vault.util;

import iskallia.vault.event.ActiveFlags;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModSounds;
import iskallia.vault.skill.talent.TalentGroup;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.util.calc.LeechHelper;
import iskallia.vault.world.data.PlayerTalentsData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class PlayerLeechHelper {
    @SubscribeEvent
    public static void onLivingHurt(LivingDamageEvent event) {
        float leech;
        if (!(event.getSource().getEntity() instanceof LivingEntity)) {
            return;
        }
        LivingEntity attacker = (LivingEntity) event.getSource().getEntity();
        if (attacker.getCommandSenderWorld().isClientSide()) {
            return;
        }

        float leechMultiplier = 1.0F;


        if (attacker instanceof ServerPlayerEntity) {
            ServerPlayerEntity sPlayer = (ServerPlayerEntity) attacker;
            TalentTree talents = PlayerTalentsData.get(sPlayer.getLevel()).getTalents((PlayerEntity) sPlayer);
            if (talents.hasLearnedNode((TalentGroup) ModConfigs.TALENTS.WARD)) {
                return;
            }
        }
        if (ActiveFlags.IS_AOE_ATTACKING.isSet() || ActiveFlags.IS_DOT_ATTACKING
                .isSet() || ActiveFlags.IS_REFLECT_ATTACKING
                .isSet()) {
            return;
        }


        if (attacker instanceof ServerPlayerEntity) {
            leech = LeechHelper.getPlayerLeechPercent((ServerPlayerEntity) attacker);
        } else {
            leech = LeechHelper.getLeechPercent(attacker);
        }
        leech *= leechMultiplier;

        if (leech > 1.0E-4D) {
            leechHealth(attacker, event.getAmount() * leech);
        }
    }

    private static void leechHealth(LivingEntity attacker, float amountLeeched) {
        ActiveFlags.IS_LEECHING.runIfNotSet(() -> attacker.heal(amountLeeched));


        if (attacker.getRandom().nextFloat() <= 0.2D) {
            float pitch = MathUtilities.randomFloat(1.0F, 1.5F);
            attacker.getCommandSenderWorld().playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), ModSounds.VAMPIRE_HISSING_SFX, SoundCategory.MASTER, 0.020000001F, pitch);
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\PlayerLeechHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */