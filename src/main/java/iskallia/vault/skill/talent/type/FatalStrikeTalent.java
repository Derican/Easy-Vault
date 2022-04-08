package iskallia.vault.skill.talent.type;

import com.google.gson.annotations.Expose;
import iskallia.vault.util.calc.FatalStrikeHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class FatalStrikeTalent
        extends PlayerTalent {
    @Expose
    private final float fatalStrikeChance;

    public FatalStrikeTalent(int cost, float fatalStrikeChance, float fatalStrikeDamage) {
        super(cost);
        this.fatalStrikeChance = fatalStrikeChance;
        this.fatalStrikeDamage = fatalStrikeDamage;
    }

    @Expose
    private final float fatalStrikeDamage;

    public float getFatalStrikeChance() {
        return this.fatalStrikeChance;
    }

    public float getFatalStrikeDamage() {
        return this.fatalStrikeDamage;
    }

    @SubscribeEvent
    public static void onPlayerAttack(LivingHurtEvent event) {
        float fatalChance, fatalPercentDamage;
        LivingEntity attacked = event.getEntityLiving();
        if (attacked.getCommandSenderWorld().isClientSide()) {
            return;
        }
        Entity source = event.getSource().getEntity();

        if (source instanceof ServerPlayerEntity) {
            fatalChance = FatalStrikeHelper.getPlayerFatalStrikeChance((ServerPlayerEntity) source);
        } else if (source instanceof LivingEntity) {
            fatalChance = FatalStrikeHelper.getFatalStrikeChance((LivingEntity) source);
        } else {
            return;
        }
        if (rand.nextFloat() >= fatalChance) {
            return;
        }


        if (source instanceof ServerPlayerEntity) {
            fatalPercentDamage = FatalStrikeHelper.getPlayerFatalStrikeDamage((ServerPlayerEntity) source);
        } else {
            fatalPercentDamage = FatalStrikeHelper.getFatalStrikeDamage((LivingEntity) source);
        }

        float damage = event.getAmount() * (1.0F + fatalPercentDamage);
        event.setAmount(damage);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\talent\type\FatalStrikeTalent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */