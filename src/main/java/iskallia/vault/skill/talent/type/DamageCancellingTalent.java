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
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public abstract class DamageCancellingTalent extends PlayerTalent {
    public DamageCancellingTalent(int cost) {
        super(cost);
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingAttackEvent event) {
        if (event.getEntity().getCommandSenderWorld().isClientSide())
            return;
        if (!(event.getEntityLiving() instanceof ServerPlayerEntity))
            return;
        ServerPlayerEntity player = (ServerPlayerEntity) event.getEntityLiving();
        TalentTree abilities = PlayerTalentsData.get(player.getLevel()).getTalents((PlayerEntity) player);

        for (TalentNode<?> node : (Iterable<TalentNode<?>>) abilities.getNodes()) {
            if (!(node.getTalent() instanceof DamageCancellingTalent))
                continue;
            if (((DamageCancellingTalent) node.getTalent()).shouldCancel(event.getSource())) {
                event.setCanceled(true);
                return;
            }
        }
    }

    protected abstract boolean shouldCancel(DamageSource paramDamageSource);
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\talent\type\DamageCancellingTalent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */