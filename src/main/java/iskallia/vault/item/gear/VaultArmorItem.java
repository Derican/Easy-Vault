package iskallia.vault.item.gear;

import com.google.common.collect.Multimap;
import iskallia.vault.attribute.EnumAttribute;
import iskallia.vault.init.ModAttributes;
import iskallia.vault.init.ModModels;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
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

public class VaultArmorItem
        extends DyeableArmorItem
        implements VaultGear<VaultArmorItem> {
    public VaultArmorItem(ResourceLocation id, EquipmentSlotType slot, Item.Properties builder) {
        super(VaultGear.Material.INSTANCE, slot, builder);
        setRegistryName(id);
    }


    public EquipmentSlotType getEquipmentSlot(ItemStack stack) {
        return getSlot();
    }


    public int getModelsFor(VaultGear.Rarity rarity) {
        return (rarity == VaultGear.Rarity.SCRAPPY) ? ModModels.GearModel.SCRAPPY_REGISTRY
                .size() : ModModels.GearModel.REGISTRY
                .size();
    }


    @Nullable
    public EquipmentSlotType getIntendedSlot() {
        return this.slot;
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
        ItemStack heldStack = player.getItemInHand(hand);
        EquipmentSlotType slot = MobEntity.getEquipmentSlotForItem(heldStack);
        return onItemRightClick(this, world, player, hand,
                canEquip(heldStack, slot, (Entity) player) ? super.use(world, player, hand) : ActionResult.fail(heldStack));
    }


    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, world, entity, itemSlot, isSelected);
        splitStack(this, stack, world, entity);
        if (entity instanceof ServerPlayerEntity) {
            inventoryTick(this, stack, world, (ServerPlayerEntity) entity, itemSlot, isSelected);
        }
    }


    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack itemStack, World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        super.appendHoverText(itemStack, world, tooltip, flag);
        addInformation(this, itemStack, tooltip, Screen.hasShiftDown());
    }


    public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
        return canElytraFly(this, stack, entity);
    }


    public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
        return elytraFlightTick(this, stack, entity, flightTicks);
    }


    public int getColor(ItemStack stack) {
        return getColor(this, stack);
    }


    public boolean canEquip(ItemStack stack, EquipmentSlotType armorType, Entity entity) {
        EnumAttribute<VaultGear.State> stateAttribute = ModAttributes.GEAR_STATE.get(stack).orElse(null);
        return (stateAttribute != null && stateAttribute.getValue(stack) == VaultGear.State.IDENTIFIED && super
                .canEquip(stack, armorType, entity));
    }


    @Nullable
    @OnlyIn(Dist.CLIENT)
    public <A extends net.minecraft.client.renderer.entity.model.BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
        return (A) getArmorModel(this, entityLiving, itemStack, armorSlot, _default);
    }


    @Nullable
    @OnlyIn(Dist.CLIENT)
    public String getArmorTexture(ItemStack itemStack, Entity entity, EquipmentSlotType slot, String type) {
        return getArmorTexture(this, itemStack, entity, slot, type);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\gear\VaultArmorItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */