package iskallia.vault.mixin;

import iskallia.vault.attribute.EnumAttribute;
import iskallia.vault.config.DurabilityConfig;
import iskallia.vault.init.ModAttributes;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.item.gear.VaultGear;
import iskallia.vault.skill.set.PlayerSet;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.skill.talent.type.UnbreakableTalent;
import iskallia.vault.world.data.PlayerTalentsData;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.Random;


@Mixin(value = {ItemStack.class}, priority = 1001)
public abstract class MixinItemStack {
    @Shadow
    public abstract boolean isDamageableItem();

    @Shadow
    public abstract int getDamageValue();

    @Shadow
    public abstract void setDamageValue(int paramInt);

    @Overwrite
    public boolean hurt(int damage, Random rand, @Nullable ServerPlayerEntity damager) {
        if (!isDamageableItem()) return false;

        if (damager != null && getItem() instanceof iskallia.vault.item.gear.VaultArmorItem && PlayerSet.isActive(VaultGear.Set.ZOD, (LivingEntity) damager)) {
            return false;
        }

        if (damage > 0) {
            int unbreakingLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, (ItemStack) this);

            if (damager != null) {
                TalentTree abilities = PlayerTalentsData.get(damager.getLevel()).getTalents((PlayerEntity) damager);

                for (UnbreakableTalent talent : abilities.getTalents(UnbreakableTalent.class)) {
                    unbreakingLevel = (int) (unbreakingLevel + talent.getExtraUnbreaking());
                }
            }

            int damageNegation = 0;

            boolean isArmor = ((ItemStack) this).getItem() instanceof net.minecraft.item.ArmorItem;
            DurabilityConfig cfg = ModConfigs.DURBILITY;
            float chance = isArmor ? cfg.getArmorDurabilityIgnoreChance(unbreakingLevel) : cfg.getDurabilityIgnoreChance(unbreakingLevel);

            for (int k = 0; unbreakingLevel > 0 && k < damage; k++) {
                if (rand.nextFloat() < chance) {
                    damageNegation++;
                }
            }

            damage -= damageNegation;

            if (damage <= 0) {
                return false;
            }
        }

        if (damager != null && damage != 0) {
            CriteriaTriggers.ITEM_DURABILITY_CHANGED.trigger(damager, (ItemStack) this, getDamageValue() + damage);
        }

        int absDamage = getDamageValue() + damage;
        setDamageValue(absDamage);

        return (absDamage >= getMaxDamage());
    }

    @Shadow
    public abstract int getMaxDamage();

    @Shadow
    public abstract ItemStack copy();

    @Shadow
    public abstract Item getItem();

    @Inject(method = {"getDisplayName"}, at = {@At("RETURN")}, cancellable = true)
    public void useGearRarity(CallbackInfoReturnable<ITextComponent> ci) {
        if (!(getItem() instanceof VaultGear)) {
            return;
        }

        ItemStack itemStack = copy();
        VaultGear.State state = (VaultGear.State) ((EnumAttribute) ModAttributes.GEAR_STATE.getOrDefault(itemStack, VaultGear.State.UNIDENTIFIED)).getValue(itemStack);
        VaultGear.Rarity rarity = (VaultGear.Rarity) ((EnumAttribute) ModAttributes.GEAR_RARITY.getOrDefault(itemStack, VaultGear.Rarity.COMMON)).getValue(itemStack);

        if (state == VaultGear.State.UNIDENTIFIED) {
            return;
        }

        IFormattableTextComponent returnValue = (IFormattableTextComponent) ci.getReturnValue();
        Style style = returnValue.getStyle().withColor(rarity.getColor());
        ci.setReturnValue(returnValue.setStyle(style));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\mixin\MixinItemStack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */