package iskallia.vault.skill.talent.type;

import com.google.gson.annotations.Expose;
import iskallia.vault.event.ActiveFlags;
import iskallia.vault.util.ServerScheduler;
import iskallia.vault.util.calc.ThornsHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class ThornsTalent
        extends PlayerTalent {
    @Expose
    private final float thornsChance;
    @Expose
    private final float thornsDamage;

    public ThornsTalent(int cost, float thornsChance, float thornsDamage) {
        super(cost);
        this.thornsChance = thornsChance;
        this.thornsDamage = thornsDamage;
    }

    public float getThornsChance() {
        return this.thornsChance;
    }

    public float getThornsDamage() {
        return this.thornsDamage;
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onLivingAttack(LivingAttackEvent event) {
        float thornsChance, thornsDamage;
        LivingEntity hurt = event.getEntityLiving();
        if (hurt.getCommandSenderWorld().isClientSide()) {
            return;
        }
        Entity source = event.getSource().getEntity();
        if (!(source instanceof LivingEntity)) {
            return;
        }


        if (hurt instanceof ServerPlayerEntity) {
            thornsChance = ThornsHelper.getPlayerThornsChance((ServerPlayerEntity) hurt);
        } else {
            thornsChance = ThornsHelper.getThornsChance(hurt);
        }
        if (rand.nextFloat() >= thornsChance) {
            return;
        }


        if (hurt instanceof ServerPlayerEntity) {
            thornsDamage = ThornsHelper.getPlayerThornsDamage((ServerPlayerEntity) hurt);
        } else {
            thornsDamage = ThornsHelper.getThornsDamage(hurt);
        }
        if (thornsDamage <= 0.001F) {
            return;
        }

        float dmg = (float) hurt.getAttributeValue(Attributes.ATTACK_DAMAGE);
        ServerScheduler.INSTANCE.schedule(0, () -> ActiveFlags.IS_REFLECT_ATTACKING.runIfNotSet(()));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\talent\type\ThornsTalent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */