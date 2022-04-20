package iskallia.vault.skill.talent.type;

import com.google.gson.annotations.Expose;
import iskallia.vault.util.calc.ParryHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ParryTalent extends PlayerTalent {
    @Expose
    protected float additionalParryLimit;

    public ParryTalent(final int cost, final float additionalParryLimit) {
        super(cost);
        this.additionalParryLimit = additionalParryLimit;
    }

    public float getAdditionalParryLimit() {
        return this.additionalParryLimit;
    }

    @SubscribeEvent
    public static void onPlayerDamage(final LivingAttackEvent event) {
        final LivingEntity entity = event.getEntityLiving();
        final World world = entity.getCommandSenderWorld();
        if (world.isClientSide() || event.getSource().isBypassInvul()) {
            return;
        }
        if (entity.invulnerableTime > 10 /* && event.getAmount() < entity.lastHurt */) {
            return;
        }
        float parryChance;
        if (entity instanceof ServerPlayerEntity) {
            parryChance = ParryHelper.getPlayerParryChance((ServerPlayerEntity) entity);
        } else {
            parryChance = ParryHelper.getParryChance(entity);
        }
        if (ParryTalent.rand.nextFloat() <= parryChance) {
            world.playSound((PlayerEntity) null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.SHIELD_BLOCK, SoundCategory.MASTER, 0.5f, 1.0f);
            event.setCanceled(true);
        }
    }
}
