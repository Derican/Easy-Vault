package iskallia.vault.skill.ability.effect;

import iskallia.vault.Vault;
import iskallia.vault.entity.EternalEntity;
import iskallia.vault.entity.eternal.ActiveEternalData;
import iskallia.vault.entity.eternal.EternalData;
import iskallia.vault.entity.eternal.EternalDataAccess;
import iskallia.vault.entity.eternal.EternalHelper;
import iskallia.vault.skill.ability.config.SummonEternalConfig;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.skill.talent.type.archetype.CommanderTalent;
import iskallia.vault.world.data.EternalsData;
import iskallia.vault.world.data.PlayerTalentsData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class SummonEternalAbility<C extends SummonEternalConfig> extends AbilityEffect<C> {
    public String getAbilityGroupName() {
        return "Summon Eternal";
    }


    public boolean onAction(C config, ServerPlayerEntity player, boolean active) {
        if (player.getCommandSenderWorld().isClientSide() || !(player.getCommandSenderWorld() instanceof ServerWorld)) {
            return false;
        }
        ServerWorld sWorld = (ServerWorld) player.getCommandSenderWorld();
        EternalsData.EternalGroup playerEternals = EternalsData.get(sWorld).getEternals((PlayerEntity) player);

        if (playerEternals.getEternals().isEmpty()) {
            player.sendMessage((ITextComponent) (new StringTextComponent("You have no eternals to summon.")).withStyle(TextFormatting.RED), Util.NIL_UUID);
            return false;
        }
        if (player.getCommandSenderWorld().dimension() != Vault.VAULT_KEY && config.isVaultOnly()) {
            player.sendMessage((ITextComponent) (new StringTextComponent("You can only summon eternals in the Vault!")).withStyle(TextFormatting.RED), Util.NIL_UUID);
            return false;
        }

        List<EternalData> eternals = new ArrayList<>();
        int count = getEternalCount(playerEternals, config);
        for (int i = 0; i < count; i++) {
            EternalData eternal = null;
            if (rand.nextFloat() < config.getAncientChance()) {
                eternal = playerEternals.getRandomAliveAncient(rand, eternalData ->
                        (!eternals.contains(eternalData) && !ActiveEternalData.getInstance().isEternalActive(eternalData.getId())));
            }

            if (eternal == null) {
                eternal = playerEternals.getRandomAlive(rand, eternalData ->
                        (!eternals.contains(eternalData) && !ActiveEternalData.getInstance().isEternalActive(eternalData.getId())));
            }

            if (eternal != null) {
                eternals.add(eternal);
            }
        }
        if (eternals.isEmpty()) {
            player.sendMessage((ITextComponent) (new StringTextComponent("You have no (alive) eternals to summon.")).withStyle(TextFormatting.RED), Util.NIL_UUID);
            return false;
        }

        TalentTree talents = PlayerTalentsData.get(sWorld).getTalents((PlayerEntity) player);


        double damageMultiplier = talents.getLearnedNodes(CommanderTalent.class).stream().mapToDouble(node -> ((CommanderTalent) node.getTalent()).getSummonEternalDamageDealtMultiplier()).max().orElse(1.0D);
        AttributeModifier modifier = new AttributeModifier(CommanderTalent.ETERNAL_DAMAGE_INCREASE_MODIFIER, "CommanderTalent", damageMultiplier, AttributeModifier.Operation.MULTIPLY_TOTAL);

        for (EternalData eternalData : eternals) {
            EternalEntity eternal = EternalHelper.spawnEternal((World) sWorld, (EternalDataAccess) eternalData);
            eternal.moveTo(player.getX(), player.getY(), player.getZ(), player.yRot, player.xRot);
            eternal.setDespawnTime((sWorld.getServer().getTickCount() + config.getDespawnTime()));
            eternal.setOwner(player.getUUID());
            eternal.setEternalId(eternalData.getId());
            eternal.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(modifier);
            eternal.addEffect(new EffectInstance(Effects.GLOWING, 2147483647, 0, true, false));
            postProcessEternal(eternal, config);
            if (eternalData.getAura() != null) {
                eternal.setProvidedAura(eternalData.getAura().getAuraName());
            }
            sWorld.addFreshEntity((Entity) eternal);
        }
        return true;
    }

    protected int getEternalCount(EternalsData.EternalGroup eternals, C config) {
        return config.getNumberOfEternals();
    }

    protected void postProcessEternal(EternalEntity eternalEntity, C config) {
    }

    @SubscribeEvent
    public void onDamage(LivingAttackEvent event) {
        LivingEntity damagedEntity = event.getEntityLiving();
        Entity dealerEntity = event.getSource().getEntity();

        if (damagedEntity instanceof EternalEntity && dealerEntity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) dealerEntity;
            if (!player.isCreative())
                event.setCanceled(true);
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\effect\SummonEternalAbility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */