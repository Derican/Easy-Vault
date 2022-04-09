// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.talent.type;

import com.google.gson.annotations.Expose;
import iskallia.vault.event.ActiveFlags;
import iskallia.vault.util.ServerScheduler;
import iskallia.vault.util.calc.ThornsHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ThornsTalent extends PlayerTalent
{
    @Expose
    private final float thornsChance;
    @Expose
    private final float thornsDamage;
    
    public ThornsTalent(final int cost, final float thornsChance, final float thornsDamage) {
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
    public static void onLivingAttack(final LivingAttackEvent event) {
        final LivingEntity hurt = event.getEntityLiving();
        if (hurt.getCommandSenderWorld().isClientSide()) {
            return;
        }
        final Entity source = event.getSource().getEntity();
        if (!(source instanceof LivingEntity)) {
            return;
        }
        float thornsChance;
        if (hurt instanceof ServerPlayerEntity) {
            thornsChance = ThornsHelper.getPlayerThornsChance((ServerPlayerEntity)hurt);
        }
        else {
            thornsChance = ThornsHelper.getThornsChance(hurt);
        }
        if (ThornsTalent.rand.nextFloat() >= thornsChance) {
            return;
        }
        float thornsDamage;
        if (hurt instanceof ServerPlayerEntity) {
            thornsDamage = ThornsHelper.getPlayerThornsDamage((ServerPlayerEntity)hurt);
        }
        else {
            thornsDamage = ThornsHelper.getThornsDamage(hurt);
        }
        if (thornsDamage <= 0.001f) {
            return;
        }
        final float dmg = (float)hurt.getAttributeValue(Attributes.ATTACK_DAMAGE);
        ServerScheduler.INSTANCE.schedule(0, () -> ActiveFlags.IS_REFLECT_ATTACKING.runIfNotSet(() -> source.hurt(DamageSource.thorns((Entity)hurt), dmg * thornsDamage)));
    }
}
