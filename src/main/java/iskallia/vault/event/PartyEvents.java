package iskallia.vault.event;

import iskallia.vault.entity.EternalEntity;
import iskallia.vault.world.data.VaultPartyData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.UUID;
import java.util.function.Function;


@EventBusSubscriber
public class PartyEvents {
    @SubscribeEvent
    public static void onAttack(LivingAttackEvent event) {
        Entity source = event.getSource().getEntity();
        if (!(source instanceof LivingEntity)) {
            return;
        }
        LivingEntity attacker = (LivingEntity) source;
        LivingEntity attacked = event.getEntityLiving();
        World world = attacked.getCommandSenderWorld();
        if (!(world instanceof ServerWorld)) {
            return;
        }
        ServerWorld sWorld = (ServerWorld) world;

        UUID attackerUUID = attacker.getUUID();
        if (attacker instanceof EternalEntity) {
            attackerUUID = (UUID) ((EternalEntity) attacker).getOwner().map(Function.identity(), Entity::getUUID);
        }
        UUID attackedUUID = attacked.getUUID();
        if (attacked instanceof EternalEntity) {
            attackerUUID = (UUID) ((EternalEntity) attacked).getOwner().map(Function.identity(), Entity::getUUID);
        }


        VaultPartyData.Party party = VaultPartyData.get(sWorld).getParty(attackerUUID).orElse(null);
        if (party != null && party.hasMember(attackedUUID))
            event.setCanceled(true);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\event\PartyEvents.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */