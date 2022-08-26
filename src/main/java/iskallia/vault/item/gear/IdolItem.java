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

public class IdolItem extends BasicItem implements VaultGear<IdolItem> {
    private static final UUID ATTACK_DAMAGE_MODIFIER;
    private static final UUID ATTACK_SPEED_MODIFIER;
    private final PlayerFavourData.VaultGodType type;

    public IdolItem(final ResourceLocation id, final PlayerFavourData.VaultGodType type, final Item.Properties properties) {
        super(id, properties);
        this.type = type;
    }

    public PlayerFavourData.VaultGodType getType() {
        return this.type;
    }

    @Override
    public int getModelsFor(final Rarity rarity) {
        return 1;
    }

    @Nullable
    @Override
    public EquipmentSlotType getIntendedSlot() {
        return EquipmentSlotType.OFFHAND;
    }

    public void fillItemCategory(final ItemGroup group, final NonNullList<ItemStack> items) {
        if (this.allowdedIn(group)) {
            this.fillItemGroup(items);
        }
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(final EquipmentSlotType slot, final ItemStack stack) {
        if (this.isIntendedForSlot(slot)) {
            final ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(IdolItem.ATTACK_DAMAGE_MODIFIER, "Idol modifier", 0.0, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(IdolItem.ATTACK_SPEED_MODIFIER, "Idol modifier", 0.0, AttributeModifier.Operation.ADDITION));
            return this.getAttributeModifiers(this, slot, stack, builder.build());
        }
        return ImmutableMultimap.of();
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

    public ITextComponent getName(final ItemStack stack) {
        return this.getDisplayName(this, stack, super.getName(stack));
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
    @Override
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

    static {
        ATTACK_DAMAGE_MODIFIER = UUID.fromString("CB3F55D3-645C-4F38-A497-2C13A33D1234");
        ATTACK_SPEED_MODIFIER = UUID.fromString("FA233E1C-4180-4865-B01B-4CCE97851234");
    }
}
