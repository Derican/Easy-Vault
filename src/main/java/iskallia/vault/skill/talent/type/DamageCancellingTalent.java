// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.talent.type;

import iskallia.vault.skill.talent.TalentNode;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.world.data.PlayerTalentsData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public abstract class DamageCancellingTalent extends PlayerTalent
{
    public DamageCancellingTalent(final int cost) {
        super(cost);
    }
    
    @SubscribeEvent
    public static void onLivingHurt(final LivingAttackEvent event) {
        if (event.getEntity().getCommandSenderWorld().isClientSide()) {
            return;
        }
        if (!(event.getEntityLiving() instanceof ServerPlayerEntity)) {
            return;
        }
        final ServerPlayerEntity player = (ServerPlayerEntity)event.getEntityLiving();
        final TalentTree abilities = PlayerTalentsData.get(player.getLevel()).getTalents((PlayerEntity)player);
        for (final TalentNode<?> node : abilities.getNodes()) {
            if (!(node.getTalent() instanceof DamageCancellingTalent)) {
                continue;
            }
            if (((DamageCancellingTalent)node.getTalent()).shouldCancel(event.getSource())) {
                event.setCanceled(true);
            }
        }
    }
    
    protected abstract boolean shouldCancel(final DamageSource p0);
}
