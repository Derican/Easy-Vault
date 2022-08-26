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

public class RampageTimeAbility extends RampageAbility<RampageTimeConfig> {
    private static final Map<UUID, Integer> tickMap;

    @SubscribeEvent
    public void onLivingDamage(final LivingDamageEvent event) {
        if (event.getEntity().getCommandSenderWorld().isClientSide()) {
            return;
        }
        if (!(event.getSource().getEntity() instanceof PlayerEntity)) {
            return;
        }
        final PlayerEntity player = (PlayerEntity) event.getSource().getEntity();
        if (!(player instanceof ServerPlayerEntity)) {
            return;
        }
        final ServerPlayerEntity sPlayer = (ServerPlayerEntity) player;
        final ServerWorld world = sPlayer.getLevel();
        final int tick = RampageTimeAbility.tickMap.getOrDefault(sPlayer.getUUID(), 0);
        if (sPlayer.tickCount == tick) {
            return;
        }
        RampageTimeAbility.tickMap.put(sPlayer.getUUID(), sPlayer.tickCount);
        final EffectInstance rampage = sPlayer.getEffect(ModEffects.RAMPAGE);
        if (rampage == null) {
            return;
        }
        final AbilityTree abilities = PlayerAbilitiesData.get(world).getAbilities(sPlayer);
        final AbilityNode<?, ?> node = abilities.getNodeByName("Rampage");
        if (node.getAbility() == this && node.isLearned()) {
            final RampageTimeConfig cfg = (RampageTimeConfig) node.getAbilityConfig();
            final EffectInstance effectInstance = rampage;
//            effectInstance.duration += cfg.getTickTimeIncreasePerHit();
            sPlayer.connection.send(new SPlayEntityEffectPacket(sPlayer.getId(), rampage));
        }
    }

    static {
        tickMap = new HashMap<UUID, Integer>();
    }
}
