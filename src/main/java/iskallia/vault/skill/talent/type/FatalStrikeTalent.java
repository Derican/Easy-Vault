// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.talent.type;

import com.google.gson.annotations.Expose;
import iskallia.vault.util.calc.FatalStrikeHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class FatalStrikeTalent extends PlayerTalent
{
    @Expose
    private final float fatalStrikeChance;
    @Expose
    private final float fatalStrikeDamage;
    
    public FatalStrikeTalent(final int cost, final float fatalStrikeChance, final float fatalStrikeDamage) {
        super(cost);
        this.fatalStrikeChance = fatalStrikeChance;
        this.fatalStrikeDamage = fatalStrikeDamage;
    }
    
    public float getFatalStrikeChance() {
        return this.fatalStrikeChance;
    }
    
    public float getFatalStrikeDamage() {
        return this.fatalStrikeDamage;
    }
    
    @SubscribeEvent
    public static void onPlayerAttack(final LivingHurtEvent event) {
        final LivingEntity attacked = event.getEntityLiving();
        if (attacked.getCommandSenderWorld().isClientSide()) {
            return;
        }
        final Entity source = event.getSource().getEntity();
        float fatalChance;
        if (source instanceof ServerPlayerEntity) {
            fatalChance = FatalStrikeHelper.getPlayerFatalStrikeChance((ServerPlayerEntity)source);
        }
        else {
            if (!(source instanceof LivingEntity)) {
                return;
            }
            fatalChance = FatalStrikeHelper.getFatalStrikeChance((LivingEntity)source);
        }
        if (FatalStrikeTalent.rand.nextFloat() >= fatalChance) {
            return;
        }
        float fatalPercentDamage;
        if (source instanceof ServerPlayerEntity) {
            fatalPercentDamage = FatalStrikeHelper.getPlayerFatalStrikeDamage((ServerPlayerEntity)source);
        }
        else {
            fatalPercentDamage = FatalStrikeHelper.getFatalStrikeDamage((LivingEntity)source);
        }
        final float damage = event.getAmount() * (1.0f + fatalPercentDamage);
        event.setAmount(damage);
    }
}
