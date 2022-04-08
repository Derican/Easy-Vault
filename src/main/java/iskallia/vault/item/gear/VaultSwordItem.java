package iskallia.vault.item.gear;

import com.google.common.collect.Multimap;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class VaultSwordItem
        extends SwordItem implements VaultGear<VaultSwordItem> {
    public VaultSwordItem(ResourceLocation id, Item.Properties builder) {
        super(VaultGear.Tier.INSTANCE, 0, -2.4F, builder);
        setRegistryName(id);
    }


    public int getModelsFor(VaultGear.Rarity rarity) {
        return 8;
    }


    @Nullable
    public EquipmentSlotType getIntendedSlot() {
        return EquipmentSlotType.MAINHAND;
    }


    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        return getAttributeModifiers(this, slot, stack, super.getAttributeModifiers(slot, stack));
    }


    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
        if (allowdedIn(group)) {
            fillItemGroup(items);
        }
    }


    public boolean isRepairable(ItemStack stack) {
        return false;
    }


    public boolean isDamageable(ItemStack stack) {
        return isDamageable(this, stack);
    }


    public int getMaxDamage(ItemStack stack) {
        return getMaxDamage(this, stack, super.getMaxDamage(stack));
    }


    public ITextComponent getName(ItemStack itemStack) {
        return getDisplayName(this, itemStack, super.getName(itemStack));
    }


    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        return onItemRightClick(this, world, player, hand, super.use(world, player, hand));
    }


    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, world, entity, itemSlot, isSelected);
        splitStack(this, stack, world, entity);
        if (entity instanceof ServerPlayerEntity) {
            inventoryTick(this, stack, world, (ServerPlayerEntity) entity, itemSlot, isSelected);
        }
    }


    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        addInformation(this, stack, tooltip, Screen.hasShiftDown());
    }


    public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
        return canElytraFly(this, stack, entity);
    }


    public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
        return elytraFlightTick(this, stack, entity, flightTicks);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\gear\VaultSwordItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */