// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.item.gear;

import com.google.common.collect.Multimap;
import iskallia.vault.attribute.EnumAttribute;
import iskallia.vault.init.ModAttributes;
import iskallia.vault.init.ModModels;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
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

public class VaultArmorItem extends DyeableArmorItem implements VaultGear<VaultArmorItem>
{
    public VaultArmorItem(final ResourceLocation id, final EquipmentSlotType slot, final Item.Properties builder) {
        super((IArmorMaterial)Material.INSTANCE, slot, builder);
        this.setRegistryName(id);
    }
    
    public EquipmentSlotType getEquipmentSlot(final ItemStack stack) {
        return this.getSlot();
    }
    
    public int getModelsFor(final Rarity rarity) {
        return (rarity == Rarity.SCRAPPY) ? ModModels.GearModel.SCRAPPY_REGISTRY.size() : ModModels.GearModel.REGISTRY.size();
    }
    
    @Nullable
    public EquipmentSlotType getIntendedSlot() {
        return this.slot;
    }
    
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(final EquipmentSlotType slot, final ItemStack stack) {
        return this.getAttributeModifiers(this, slot, stack, (Multimap<Attribute, AttributeModifier>)super.getAttributeModifiers(slot, stack));
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
        final ItemStack heldStack = player.getItemInHand(hand);
        final EquipmentSlotType slot = MobEntity.getEquipmentSlotForItem(heldStack);
        return this.onItemRightClick(this, world, player, hand, (ActionResult<ItemStack>)(this.canEquip(heldStack, slot, (Entity)player) ? super.use(world, player, hand) : ActionResult.fail(heldStack)));
    }
    
    public void inventoryTick(final ItemStack stack, final World world, final Entity entity, final int itemSlot, final boolean isSelected) {
        super.inventoryTick(stack, world, entity, itemSlot, isSelected);
        this.splitStack(this, stack, world, entity);
        if (entity instanceof ServerPlayerEntity) {
            this.inventoryTick(this, stack, world, (ServerPlayerEntity)entity, itemSlot, isSelected);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(final ItemStack itemStack, final World world, final List<ITextComponent> tooltip, final ITooltipFlag flag) {
        super.appendHoverText(itemStack, world, (List)tooltip, flag);
        this.addInformation(this, itemStack, tooltip, Screen.hasShiftDown());
    }
    
    public boolean canElytraFly(final ItemStack stack, final LivingEntity entity) {
        return this.canElytraFly(this, stack, entity);
    }
    
    public boolean elytraFlightTick(final ItemStack stack, final LivingEntity entity, final int flightTicks) {
        return this.elytraFlightTick(this, stack, entity, flightTicks);
    }
    
    public int getColor(final ItemStack stack) {
        return this.getColor(this, stack);
    }
    
    public boolean canEquip(final ItemStack stack, final EquipmentSlotType armorType, final Entity entity) {
        final EnumAttribute<State> stateAttribute = ModAttributes.GEAR_STATE.get(stack).orElse(null);
        return stateAttribute != null && stateAttribute.getValue(stack) == State.IDENTIFIED && super.canEquip(stack, armorType, entity);
    }
    
    @Nullable
    @OnlyIn(Dist.CLIENT)
    public <A extends BipedModel<?>> A getArmorModel(final LivingEntity entityLiving, final ItemStack itemStack, final EquipmentSlotType armorSlot, final A _default) {
        return this.getArmorModel(this, entityLiving, itemStack, armorSlot, _default);
    }
    
    @Nullable
    @OnlyIn(Dist.CLIENT)
    public String getArmorTexture(final ItemStack itemStack, final Entity entity, final EquipmentSlotType slot, final String type) {
        return this.getArmorTexture(this, itemStack, entity, slot, type);
    }
}
