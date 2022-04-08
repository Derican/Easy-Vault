package iskallia.vault.skill.ability.effect.sub;

import iskallia.vault.init.ModEffects;
import iskallia.vault.skill.ability.AbilityNode;
import iskallia.vault.skill.ability.AbilityTree;
import iskallia.vault.skill.ability.config.sub.RampageTimeConfig;
import iskallia.vault.skill.ability.effect.RampageAbility;
import iskallia.vault.world.data.PlayerAbilitiesData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SPlayEntityEffectPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RampageTimeAbility
        extends RampageAbility<RampageTimeConfig> {
    private static final Map<UUID, Integer> tickMap = new HashMap<>();

    @SubscribeEvent
    public void onLivingDamage(LivingDamageEvent event) {
        if (event.getEntity().getCommandSenderWorld().isClientSide()) {
            return;
        }
        if (!(event.getSource().getEntity() instanceof PlayerEntity)) {
            return;
        }

        PlayerEntity player = (PlayerEntity) event.getSource().getEntity();
        if (!(player instanceof ServerPlayerEntity)) {
            return;
        }
        ServerPlayerEntity sPlayer = (ServerPlayerEntity) player;
        ServerWorld world = sPlayer.getLevel();

        int tick = ((Integer) tickMap.getOrDefault(sPlayer.getUUID(), Integer.valueOf(0))).intValue();
        if (sPlayer.tickCount == tick) {
            return;
        }
        tickMap.put(sPlayer.getUUID(), Integer.valueOf(sPlayer.tickCount));

        EffectInstance rampage = sPlayer.getEffect(ModEffects.RAMPAGE);
        if (rampage == null) {
            return;
        }
        AbilityTree abilities = PlayerAbilitiesData.get(world).getAbilities((PlayerEntity) sPlayer);
        AbilityNode<?, ?> node = abilities.getNodeByName("Rampage");
        if (node.getAbility() == this && node.isLearned()) {
            RampageTimeConfig cfg = (RampageTimeConfig) node.getAbilityConfig();

            rampage.duration += cfg.getTickTimeIncreasePerHit();
            sPlayer.connection.send((IPacket) new SPlayEntityEffectPacket(sPlayer.getId(), rampage));
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\effect\sub\RampageTimeAbility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */