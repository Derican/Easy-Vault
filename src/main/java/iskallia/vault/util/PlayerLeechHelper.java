package iskallia.vault.util;

import iskallia.vault.event.ActiveFlags;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModSounds;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.util.calc.LeechHelper;
import iskallia.vault.world.data.PlayerTalentsData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class PlayerLeechHelper {
    @SubscribeEvent
    public static void onLivingHurt(final LivingDamageEvent event) {
        if (!(event.getSource().getEntity() instanceof LivingEntity)) {
            return;
        }
        final LivingEntity attacker = (LivingEntity) event.getSource().getEntity();
        if (attacker.getCommandSenderWorld().isClientSide()) {
            return;
        }
        final float leechMultiplier = 1.0f;
        if (attacker instanceof ServerPlayerEntity) {
            final ServerPlayerEntity sPlayer = (ServerPlayerEntity) attacker;
            final TalentTree talents = PlayerTalentsData.get(sPlayer.getLevel()).getTalents(sPlayer);
            if (talents.hasLearnedNode(ModConfigs.TALENTS.WARD)) {
                return;
            }
        }
        if (ActiveFlags.IS_AOE_ATTACKING.isSet() || ActiveFlags.IS_DOT_ATTACKING.isSet() || ActiveFlags.IS_REFLECT_ATTACKING.isSet()) {
            return;
        }
        float leech;
        if (attacker instanceof ServerPlayerEntity) {
            leech = LeechHelper.getPlayerLeechPercent((ServerPlayerEntity) attacker);
        } else {
            leech = LeechHelper.getLeechPercent(attacker);
        }
        leech *= leechMultiplier;
        if (leech > 1.0E-4) {
            leechHealth(attacker, event.getAmount() * leech);
        }
    }

    private static void leechHealth(final LivingEntity attacker, final float amountLeeched) {
        ActiveFlags.IS_LEECHING.runIfNotSet(() -> attacker.heal(amountLeeched));
        if (attacker.getRandom().nextFloat() <= 0.2) {
            final float pitch = MathUtilities.randomFloat(1.0f, 1.5f);
            attacker.getCommandSenderWorld().playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), ModSounds.VAMPIRE_HISSING_SFX, SoundCategory.MASTER, 0.020000001f, pitch);
        }
    }
}
