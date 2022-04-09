// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.item.consumable;

import iskallia.vault.config.entry.ConsumableEffect;
import iskallia.vault.config.entry.ConsumableEntry;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModEffects;
import iskallia.vault.init.ModItems;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.util.calc.AbsorptionHelper;
import iskallia.vault.world.data.PlayerTalentsData;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;

public class VaultConsumableItem extends Item
{
    public static Food VAULT_FOOD;
    
    public VaultConsumableItem(final ResourceLocation id) {
        super(new Item.Properties().tab(ModItems.VAULT_MOD_GROUP).food(VaultConsumableItem.VAULT_FOOD).stacksTo(64));
        this.setRegistryName(id);
    }
    
    public void appendHoverText(final ItemStack stack, @Nullable final World worldIn, final List<ITextComponent> tooltip, final ITooltipFlag flagIn) {
        final ResourceLocation itemId = stack.getItem().getRegistryName();
        if (ModConfigs.CONSUMABLES == null || itemId == null) {
            return;
        }
        final List<String> text = ModConfigs.CONSUMABLES.getDescriptionFor(itemId.toString());
        if (text == null) {
            return;
        }
        for (final String s : text) {
            tooltip.add((ITextComponent)new StringTextComponent(s));
        }
        super.appendHoverText(stack, worldIn, (List)tooltip, flagIn);
    }
    
    public ItemStack finishUsingItem(final ItemStack stack, final World world, final LivingEntity entityLiving) {
        if (entityLiving instanceof ServerPlayerEntity) {
            final ServerPlayerEntity sPlayer = (ServerPlayerEntity)entityLiving;
            final ResourceLocation itemId = stack.getItem().getRegistryName();
            final ConsumableEntry entry = ModConfigs.CONSUMABLES.get(itemId.toString());
            if (entry.isPowerup() && sPlayer.getEffect(ModEffects.VAULT_POWERUP) != null) {
                return stack;
            }
            if (entry.shouldAddAbsorption()) {
                final TalentTree talents = PlayerTalentsData.get(sPlayer.getLevel()).getTalents((PlayerEntity)sPlayer);
                if (talents.hasLearnedNode(ModConfigs.TALENTS.WARD) || talents.hasLearnedNode(ModConfigs.TALENTS.BARBARIC)) {
                    return stack;
                }
                final float targetAbsorption = sPlayer.getAbsorptionAmount() + entry.getAbsorptionAmount();
                if (targetAbsorption > AbsorptionHelper.getMaxAbsorption((PlayerEntity)sPlayer)) {
                    return stack;
                }
                sPlayer.setAbsorptionAmount(targetAbsorption);
            }
            for (final ConsumableEffect setting : entry.getEffects()) {
                final ResourceLocation id = ResourceLocation.tryParse(setting.getEffectId());
                final Effect effect = (Effect)ForgeRegistries.POTIONS.getValue(id);
                if (id != null && effect != null) {
                    final EffectInstance effectInstance = sPlayer.getEffect(effect);
                    if (effectInstance == null) {
                        if (entry.isPowerup()) {
                            this.applyEffect((PlayerEntity)sPlayer, effect, setting);
                            this.applyPowerup((PlayerEntity)sPlayer, setting);
                        }
                        else {
                            this.applyEffect((PlayerEntity)sPlayer, effect, setting);
                        }
                    }
                    else if (entry.isPowerup()) {
                        this.addEffect((PlayerEntity)sPlayer, effectInstance, setting);
                        this.applyPowerup((PlayerEntity)sPlayer, setting);
                    }
                    else if (effectInstance.getAmplifier() < setting.getAmplifier()) {
                        this.applyEffect((PlayerEntity)sPlayer, effect, setting);
                    }
                    else {
                        if (effectInstance.getDuration() >= setting.getDuration()) {
                            continue;
                        }
                        this.addEffectDuration((PlayerEntity)sPlayer, effectInstance, setting.getDuration());
                    }
                }
            }
        }
        return super.finishUsingItem(stack, world, entityLiving);
    }
    
    private void addEffect(final PlayerEntity player, final EffectInstance effectInstance, final ConsumableEffect effect) {
        player.removeEffect(effectInstance.getEffect());
        final EffectInstance newEffect = new EffectInstance(effectInstance.getEffect(), effect.getDuration(), effectInstance.getAmplifier() + effect.getAmplifier(), effectInstance.isAmbient(), effectInstance.isVisible(), effectInstance.showIcon());
        player.addEffect(newEffect);
    }
    
    private void applyPowerup(final PlayerEntity player, final ConsumableEffect effect) {
        final EffectInstance powerup = new EffectInstance(ModEffects.VAULT_POWERUP, effect.getDuration(), 0, effect.isAmbient(), effect.shouldShowParticles(), effect.shouldShowIcon());
        player.addEffect(powerup);
    }
    
    private void applyEffect(final PlayerEntity player, final Effect effect, final ConsumableEffect setting) {
        final EffectInstance newEffect = new EffectInstance(effect, setting.getDuration(), setting.getAmplifier() - 1, setting.isAmbient(), setting.shouldShowParticles(), setting.shouldShowIcon());
        player.addEffect(newEffect);
    }
    
    private void addEffectDuration(final PlayerEntity player, final EffectInstance effectInstance, final int newDuration) {
        player.removeEffect(effectInstance.getEffect());
        final EffectInstance newEffect = new EffectInstance(effectInstance.getEffect(), newDuration, effectInstance.getAmplifier(), effectInstance.isAmbient(), effectInstance.isVisible(), effectInstance.showIcon());
        player.addEffect(newEffect);
    }
    
    static {
        VaultConsumableItem.VAULT_FOOD = new Food.Builder().saturationMod(0.0f).nutrition(0).fast().alwaysEat().build();
    }
}
