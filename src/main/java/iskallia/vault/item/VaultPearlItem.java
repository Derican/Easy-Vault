package iskallia.vault.item;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.entity.VaultPearlEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class VaultPearlItem extends EnderPearlItem {
    public VaultPearlItem(ResourceLocation id) {
        super((new Item.Properties()).stacksTo(1).tab(ModItems.VAULT_MOD_GROUP).durability(0));

        setRegistryName(id);
    }


    public boolean showDurabilityBar(ItemStack stack) {
        return (stack.getDamageValue() > 0);
    }


    public void setDamage(ItemStack stack, int damage) {
        int currentDamage = getDamage(stack);
        if (damage <= currentDamage)
            return;
        super.setDamage(stack, damage);
    }


    public double getDurabilityForDisplay(ItemStack stack) {
        return stack.getDamageValue() / getMaxDamage(stack);
    }


    public int getMaxDamage(ItemStack stack) {
        if (ModConfigs.VAULT_UTILITIES != null) {
            return ModConfigs.VAULT_UTILITIES.getVaultPearlMaxUses();
        }
        return 0;
    }


    public boolean canBeDepleted() {
        return true;
    }


    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (hand != Hand.MAIN_HAND) return ActionResult.pass(player.getItemInHand(hand));

        ItemStack stack = player.getItemInHand(hand);

        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_PEARL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
        player.getCooldowns().addCooldown((Item) this, 20);
        if (!world.isClientSide) {
            VaultPearlEntity pearl = new VaultPearlEntity(world, (LivingEntity) player);
            pearl.setItem(stack);
            pearl.shootFromRotation((Entity) player, player.xRot, player.yRot, 0.0F, 1.5F, 1.0F);
            world.addFreshEntity((Entity) pearl);

            stack.hurtAndBreak(1, (LivingEntity) player, e -> e.broadcastBreakEvent(hand));
        }

        player.awardStat(Stats.ITEM_USED.get(this));

        return ActionResult.sidedSuccess(stack, world.isClientSide());
    }


    public boolean isEnchantable(ItemStack stack) {
        return false;
    }


    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }


    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\VaultPearlItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */