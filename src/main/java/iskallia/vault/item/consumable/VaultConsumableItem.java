package iskallia.vault.item.consumable;

import iskallia.vault.config.entry.ConsumableEffect;
import iskallia.vault.config.entry.ConsumableEntry;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModEffects;
import iskallia.vault.init.ModItems;
import iskallia.vault.skill.talent.TalentGroup;
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

public class VaultConsumableItem
        extends Item {
    public static Food VAULT_FOOD = (new Food.Builder())
            .saturationMod(0.0F)
            .nutrition(0)
            .fast()
            .alwaysEat()
            .build();

    public VaultConsumableItem(ResourceLocation id) {
        super((new Item.Properties())
                .tab(ModItems.VAULT_MOD_GROUP)
                .food(VAULT_FOOD)
                .stacksTo(64));

        setRegistryName(id);
    }


    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        ResourceLocation itemId = stack.getItem().getRegistryName();
        if (ModConfigs.CONSUMABLES == null || itemId == null)
            return;
        List<String> text = ModConfigs.CONSUMABLES.getDescriptionFor(itemId.toString());
        if (text == null)
            return;
        for (String s : text) {
            tooltip.add(new StringTextComponent(s));
        }
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }


    public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity entityLiving) {
        if (entityLiving instanceof ServerPlayerEntity) {
            ServerPlayerEntity sPlayer = (ServerPlayerEntity) entityLiving;

            ResourceLocation itemId = stack.getItem().getRegistryName();

            ConsumableEntry entry = ModConfigs.CONSUMABLES.get(itemId.toString());
            if (entry.isPowerup() &&
                    sPlayer.getEffect(ModEffects.VAULT_POWERUP) != null) {
                return stack;
            }


            if (entry.shouldAddAbsorption()) {
                TalentTree talents = PlayerTalentsData.get(sPlayer.getLevel()).getTalents((PlayerEntity) sPlayer);
                if (talents.hasLearnedNode((TalentGroup) ModConfigs.TALENTS.WARD) || talents.hasLearnedNode((TalentGroup) ModConfigs.TALENTS.BARBARIC)) {
                    return stack;
                }

                float targetAbsorption = sPlayer.getAbsorptionAmount() + entry.getAbsorptionAmount();
                if (targetAbsorption > AbsorptionHelper.getMaxAbsorption((PlayerEntity) sPlayer)) {
                    return stack;
                }
                sPlayer.setAbsorptionAmount(targetAbsorption);
            }

            for (ConsumableEffect setting : entry.getEffects()) {

                ResourceLocation id = ResourceLocation.tryParse(setting.getEffectId());
                Effect effect = (Effect) ForgeRegistries.POTIONS.getValue(id);

                if (id != null && effect != null) {
                    EffectInstance effectInstance = sPlayer.getEffect(effect);

                    if (effectInstance == null) {
                        if (entry.isPowerup()) {
                            applyEffect((PlayerEntity) sPlayer, effect, setting);
                            applyPowerup((PlayerEntity) sPlayer, setting);
                            continue;
                        }
                        applyEffect((PlayerEntity) sPlayer, effect, setting);
                        continue;
                    }
                    if (entry.isPowerup()) {
                        addEffect((PlayerEntity) sPlayer, effectInstance, setting);
                        applyPowerup((PlayerEntity) sPlayer, setting);
                        continue;
                    }
                    if (effectInstance.getAmplifier() < setting.getAmplifier()) {
                        applyEffect((PlayerEntity) sPlayer, effect, setting);
                        continue;
                    }
                    if (effectInstance.getDuration() < setting.getDuration()) {
                        addEffectDuration((PlayerEntity) sPlayer, effectInstance, setting.getDuration());
                    }
                }
            }
        }

        return super.finishUsingItem(stack, world, entityLiving);
    }

    private void addEffect(PlayerEntity player, EffectInstance effectInstance, ConsumableEffect effect) {
        player.removeEffect(effectInstance.getEffect());


        EffectInstance newEffect = new EffectInstance(effectInstance.getEffect(), effect.getDuration(), effectInstance.getAmplifier() + effect.getAmplifier(), effectInstance.isAmbient(), effectInstance.isVisible(), effectInstance.showIcon());
        player.addEffect(newEffect);
    }


    private void applyPowerup(PlayerEntity player, ConsumableEffect effect) {
        EffectInstance powerup = new EffectInstance(ModEffects.VAULT_POWERUP, effect.getDuration(), 0, effect.isAmbient(), effect.shouldShowParticles(), effect.shouldShowIcon());
        player.addEffect(powerup);
    }


    private void applyEffect(PlayerEntity player, Effect effect, ConsumableEffect setting) {
        EffectInstance newEffect = new EffectInstance(effect, setting.getDuration(), setting.getAmplifier() - 1, setting.isAmbient(), setting.shouldShowParticles(), setting.shouldShowIcon());
        player.addEffect(newEffect);
    }

    private void addEffectDuration(PlayerEntity player, EffectInstance effectInstance, int newDuration) {
        player.removeEffect(effectInstance.getEffect());


        EffectInstance newEffect = new EffectInstance(effectInstance.getEffect(), newDuration, effectInstance.getAmplifier(), effectInstance.isAmbient(), effectInstance.isVisible(), effectInstance.showIcon());
        player.addEffect(newEffect);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\consumable\VaultConsumableItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */