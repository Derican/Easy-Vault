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
import net.minecraft.item.*;
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

public class VaultSwordItem extends SwordItem implements VaultGear<VaultSwordItem> {
    public VaultSwordItem(final ResourceLocation id, final Item.Properties builder) {
        super(Tier.INSTANCE, 0, -2.4f, builder);
        this.setRegistryName(id);
    }

    public int getModelsFor(final Rarity rarity) {
        return 8;
    }

    @Nullable
    public EquipmentSlotType getIntendedSlot() {
        return EquipmentSlotType.MAINHAND;
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(final EquipmentSlotType slot, final ItemStack stack) {
        return this.getAttributeModifiers(this, slot, stack, super.getAttributeModifiers(slot, stack));
    }

    public void fillItemCategory(final ItemGroup group, final NonNullList<ItemStack> items) {
        if (this.allowdedIn(group)) {
            this.fillItemGroup(items);
        }
    }

    public boolean isRepairable(final ItemStack stack) {
        return false;
    }

    public boolean isDamageable(final ItemStack stack) {
        return this.isDamageable(this, stack);
    }

    public int getMaxDamage(final ItemStack stack) {
        return this.getMaxDamage(this, stack, super.getMaxDamage(stack));
    }

    public ITextComponent getName(final ItemStack itemStack) {
        return this.getDisplayName(this, itemStack, super.getName(itemStack));
    }

    public ActionResult<ItemStack> use(final World world, final PlayerEntity player, final Hand hand) {
        return this.onItemRightClick(this, world, player, hand, super.use(world, player, hand));
    }

    public void inventoryTick(final ItemStack stack, final World world, final Entity entity, final int itemSlot, final boolean isSelected) {
        super.inventoryTick(stack, world, entity, itemSlot, isSelected);
        this.splitStack(this, stack, world, entity);
        if (entity instanceof ServerPlayerEntity) {
            this.inventoryTick(this, stack, world, (ServerPlayerEntity) entity, itemSlot, isSelected);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(final ItemStack stack, final World world, final List<ITextComponent> tooltip, final ITooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        this.addInformation(this, stack, tooltip, Screen.hasShiftDown());
    }

    public boolean canElytraFly(final ItemStack stack, final LivingEntity entity) {
        return this.canElytraFly(this, stack, entity);
    }

    public boolean elytraFlightTick(final ItemStack stack, final LivingEntity entity, final int flightTicks) {
        return this.elytraFlightTick(this, stack, entity, flightTicks);
    }
}
