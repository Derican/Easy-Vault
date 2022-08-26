package iskallia.vault.skill.ability.effect;

import iskallia.vault.Vault;
import iskallia.vault.entity.EternalEntity;
import iskallia.vault.entity.eternal.ActiveEternalData;
import iskallia.vault.entity.eternal.EternalData;
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
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class SummonEternalAbility<C extends SummonEternalConfig> extends AbilityEffect<C> {
    @Override
    public String getAbilityGroupName() {
        return "Summon Eternal";
    }

    @Override
    public boolean onAction(final C config, final ServerPlayerEntity player, final boolean active) {
        if (player.getCommandSenderWorld().isClientSide() || !(player.getCommandSenderWorld() instanceof ServerWorld)) {
            return false;
        }
        final ServerWorld sWorld = (ServerWorld) player.getCommandSenderWorld();
        final EternalsData.EternalGroup playerEternals = EternalsData.get(sWorld).getEternals(player);
        if (playerEternals.getEternals().isEmpty()) {
            player.sendMessage(new StringTextComponent("You have no eternals to summon.").withStyle(TextFormatting.RED), Util.NIL_UUID);
            return false;
        }
        if (player.getCommandSenderWorld().dimension() != Vault.VAULT_KEY && config.isVaultOnly()) {
            player.sendMessage(new StringTextComponent("You can only summon eternals in the Vault!").withStyle(TextFormatting.RED), Util.NIL_UUID);
            return false;
        }
        final List<EternalData> eternals = new ArrayList<EternalData>();
        int count = this.getEternalCount(playerEternals, config);
        EternalData eternal = null;
        final List<EternalEntity> summonedEternals = player.getLevel().getEntities().filter(entity -> entity instanceof EternalEntity).map(entity -> (EternalEntity) entity).filter(eternal1 -> eternal1.getOwnerUUID().equals(player.getUUID())).collect(Collectors.toList());
        final int maxToSummon = config.getSummonedEternalsCap() - summonedEternals.size();
        count = Math.min(count, maxToSummon);
        for (int i = 0; i < count; ++i) {
            eternal = null;
            if (SummonEternalAbility.rand.nextFloat() < config.getAncientChance()) {
                eternal = playerEternals.getRandomAliveAncient(SummonEternalAbility.rand, eternalData -> !eternals.contains(eternalData) && !ActiveEternalData.getInstance().isEternalActive(eternalData.getId()));
            }
            if (eternal == null) {
                eternal = playerEternals.getRandomAlive(SummonEternalAbility.rand, eternalData -> !eternals.contains(eternalData) && !ActiveEternalData.getInstance().isEternalActive(eternalData.getId()));
            }
            final EternalData eternalData;
            if (eternal != null) {
                eternals.add(eternal);
            }
        }
        if (eternals.isEmpty()) {
            if (count > 0) {
                player.sendMessage(new StringTextComponent("You have no (alive) eternals to summon.").withStyle(TextFormatting.RED), Util.NIL_UUID);
            } else {
                player.sendMessage(new StringTextComponent("You have reached the eternal cap.").withStyle(TextFormatting.RED), Util.NIL_UUID);
            }
            return false;
        }
        final TalentTree talents = PlayerTalentsData.get(sWorld).getTalents(player);
        final double damageMultiplier = talents.getLearnedNodes(CommanderTalent.class).stream().mapToDouble(node -> ((CommanderTalent) node.getTalent()).getSummonEternalDamageDealtMultiplier()).max().orElse(1.0);
        final AttributeModifier modifier = new AttributeModifier(CommanderTalent.ETERNAL_DAMAGE_INCREASE_MODIFIER, "CommanderTalent", damageMultiplier, AttributeModifier.Operation.MULTIPLY_TOTAL);
        for (EternalData eternalData : eternals) {
            final EternalEntity eternal2 = EternalHelper.spawnEternal(sWorld, eternalData);
            eternal2.moveTo(player.getX(), player.getY(), player.getZ(), player.yRot, player.xRot);
            eternal2.setDespawnTime(sWorld.getServer().getTickCount() + config.getDespawnTime());
            eternal2.setOwner(player.getUUID());
            eternal2.setEternalId(eternalData.getId());
            eternal2.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(modifier);
            eternal2.addEffect(new EffectInstance(Effects.GLOWING, Integer.MAX_VALUE, 0, true, false));
            this.postProcessEternal(eternal2, config);
            if (eternalData.getAura() != null) {
                eternal2.setProvidedAura(eternalData.getAura().getAuraName());
            }
            sWorld.addFreshEntity(eternal2);
        }
        return true;
    }

    protected int getEternalCount(final EternalsData.EternalGroup eternals, final C config) {
        return config.getNumberOfEternals();
    }

    protected void postProcessEternal(final EternalEntity eternalEntity, final C config) {
    }

    @SubscribeEvent
    public void onDamage(final LivingAttackEvent event) {
        final LivingEntity damagedEntity = event.getEntityLiving();
        final Entity dealerEntity = event.getSource().getEntity();
        if (damagedEntity instanceof EternalEntity && dealerEntity instanceof PlayerEntity) {
            final PlayerEntity player = (PlayerEntity) dealerEntity;
            if (!player.isCreative()) {
                event.setCanceled(true);
            }
        }
    }
}
