package iskallia.vault.skill.ability.effect.sub;

import iskallia.vault.init.ModEffects;
import iskallia.vault.skill.ability.AbilityNode;
import iskallia.vault.skill.ability.AbilityTree;
import iskallia.vault.skill.ability.config.sub.CleanseImmuneConfig;
import iskallia.vault.skill.ability.effect.CleanseAbility;
import iskallia.vault.world.data.PlayerAbilitiesData;
import iskallia.vault.world.data.PlayerImmunityData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Collections;
import java.util.List;

public class CleanseImmuneAbility
        extends CleanseAbility<CleanseImmuneConfig> {
    protected void removeEffects(CleanseImmuneConfig config, ServerPlayerEntity player, List<EffectInstance> effects) {
        if (effects.isEmpty())
            return;
        if (!(player.getCommandSenderWorld() instanceof ServerWorld))
            return;
        ServerWorld world = (ServerWorld) player.getCommandSenderWorld();

        PlayerImmunityData data = PlayerImmunityData.get(world);
        Collections.shuffle(effects);
        data.addEffect((PlayerEntity) player, effects.get(0));

        effects.forEach(effect -> player.removeEffect(effect.getEffect()));

        EffectInstance activeEffect = player.getEffect(ModEffects.IMMUNITY);

        EffectInstance newEffect = new EffectInstance(ModEffects.IMMUNITY, config.getImmunityDuration(), 0, false, false, true);


        if (activeEffect == null) {
            player.addEffect(newEffect);
        }
    }


    @SubscribeEvent
    public void onEffect(PotionEvent.PotionApplicableEvent event) {
        if (!(event.getEntityLiving() instanceof PlayerEntity))
            return;
        PlayerEntity player = (PlayerEntity) event.getEntityLiving();

        EffectInstance immunity = player.getEffect(ModEffects.IMMUNITY);
        if (immunity == null)
            return;
        EffectInstance effectInstance = event.getPotionEffect();
        if (effectInstance.getEffect().isBeneficial())
            return;
        if (!(player.getCommandSenderWorld() instanceof ServerWorld))
            return;
        ServerWorld world = (ServerWorld) player.getCommandSenderWorld();

        PlayerAbilitiesData data = PlayerAbilitiesData.get(world);
        AbilityTree abilities = data.getAbilities(player);
        AbilityNode<?, ?> node = abilities.getNodeByName("Cleanse");

        if (node.getAbility() == this && node.isLearned()) {
            PlayerImmunityData immunityData = PlayerImmunityData.get(world);
            if (immunityData.getEffects(player.getUUID()).stream().anyMatch(effect -> effect.equals(effectInstance.getEffect()))) {
                event.setResult(Event.Result.DENY);
            }
        }
    }

    @SubscribeEvent
    public void onImmunityRemoved(PotionEvent.PotionExpiryEvent event) {
        if (!(event.getEntityLiving() instanceof PlayerEntity))
            return;
        PlayerEntity player = (PlayerEntity) event.getEntityLiving();

        if (!(player.getCommandSenderWorld() instanceof ServerWorld))
            return;
        ServerWorld world = (ServerWorld) player.getCommandSenderWorld();

        if (event.getPotionEffect() == null || event.getPotionEffect().getEffect() != ModEffects.IMMUNITY)
            return;
        PlayerImmunityData.get(world).removeEffects(player);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\effect\sub\CleanseImmuneAbility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */