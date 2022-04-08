package iskallia.vault.skill.talent.type;

import com.google.gson.annotations.Expose;
import iskallia.vault.util.calc.ParryHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class ParryTalent extends PlayerTalent {
    @Expose
    protected float additionalParryLimit;

    public ParryTalent(int cost, float additionalParryLimit) {
        super(cost);
        this.additionalParryLimit = additionalParryLimit;
    }

    public float getAdditionalParryLimit() {
        return this.additionalParryLimit;
    }

    @SubscribeEvent
    public static void onPlayerDamage(LivingAttackEvent event) {
        float parryChance;
        LivingEntity entity = event.getEntityLiving();
        World world = entity.getCommandSenderWorld();
        if (world.isClientSide() || event.getSource().isBypassInvul()) {
            return;
        }
        if (entity.invulnerableTime > 10 && event.getAmount() < entity.lastHurt) {
            return;
        }


        if (entity instanceof ServerPlayerEntity) {
            parryChance = ParryHelper.getPlayerParryChance((ServerPlayerEntity) entity);
        } else {
            parryChance = ParryHelper.getParryChance(entity);
        }

        if (rand.nextFloat() <= parryChance) {
            world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.SHIELD_BLOCK, SoundCategory.MASTER, 0.5F, 1.0F);
            event.setCanceled(true);
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\talent\type\ParryTalent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */