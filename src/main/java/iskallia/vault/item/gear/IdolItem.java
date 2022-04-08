package iskallia.vault.item.gear;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import iskallia.vault.item.BasicItem;
import iskallia.vault.world.data.PlayerFavourData;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
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
import java.util.UUID;

public class IdolItem
        extends BasicItem
        implements VaultGear<IdolItem> {
    private static final UUID ATTACK_DAMAGE_MODIFIER = UUID.fromString("CB3F55D3-645C-4F38-A497-2C13A33DB5CF");
    private static final UUID ATTACK_SPEED_MODIFIER = UUID.fromString("FA233E1C-4180-4865-B01B-4CCE9785ACA3");

    private final PlayerFavourData.VaultGodType type;

    public IdolItem(ResourceLocation id, PlayerFavourData.VaultGodType type, Item.Properties properties) {
        super(id, properties);
        this.type = type;
    }

    public PlayerFavourData.VaultGodType getType() {
        return this.type;
    }


    public int getModelsFor(VaultGear.Rarity rarity) {
        return 1;
    }


    @Nullable
    public EquipmentSlotType getIntendedSlot() {
        return EquipmentSlotType.OFFHAND;
    }


    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
        if (allowdedIn(group)) {
            fillItemGroup(items);
        }
    }


    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        if (isIntendedForSlot(slot)) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Idol modifier", 0.0D, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(ATTACK_SPEED_MODIFIER, "Idol modifier", 0.0D, AttributeModifier.Operation.ADDITION));
            return getAttributeModifiers(this, slot, stack, (Multimap<Attribute, AttributeModifier>) builder.build());
        }
        return (Multimap<Attribute, AttributeModifier>) ImmutableMultimap.of();
    }


    public boolean isDamageable(ItemStack stack) {
        return isDamageable(this, stack);
    }


    public int getMaxDamage(ItemStack stack) {
        return getMaxDamage(this, stack, super.getMaxDamage(stack));
    }


    public ITextComponent getName(ItemStack stack) {
        return getDisplayName(this, stack, super.getName(stack));
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


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\gear\IdolItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */