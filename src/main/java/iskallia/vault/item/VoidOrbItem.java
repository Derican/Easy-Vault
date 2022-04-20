package iskallia.vault.item;

import com.mojang.datafixers.util.Pair;
import iskallia.vault.attribute.VAttribute;
import iskallia.vault.init.ModAttributes;
import iskallia.vault.item.gear.VaultGearHelper;
import iskallia.vault.util.MiscUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.List;

public class VoidOrbItem extends BasicItem {
    public VoidOrbItem(final ResourceLocation id, final Item.Properties properties) {
        super(id, properties);
    }

    public Rarity getRarity(final ItemStack stack) {
        return Rarity.RARE;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(final ItemStack stack, @Nullable final World worldIn, final List<ITextComponent> tooltip, final ITooltipFlag flagIn) {
        tooltip.add(StringTextComponent.EMPTY);
        tooltip.add((ITextComponent) new StringTextComponent("Removes a modifier at random from").withStyle(TextFormatting.GRAY));
        tooltip.add((ITextComponent) new StringTextComponent("a vault gear item when combined").withStyle(TextFormatting.GRAY));
        tooltip.add((ITextComponent) new StringTextComponent("in an anvil.").withStyle(TextFormatting.GRAY));
        final Pair<EquipmentSlotType, VAttribute<?, ?>> gearModifier = getPredefinedRemoval(stack);
        if (gearModifier != null) {
            final String slotName = StringUtils.capitalize(((EquipmentSlotType) gearModifier.getFirst()).getName());
            final ITextComponent attributeTxt = VaultGearHelper.getDisplayName((VAttribute<?, ?>) gearModifier.getSecond());
            tooltip.add(StringTextComponent.EMPTY);
            tooltip.add((ITextComponent) new StringTextComponent("Only for: ").withStyle(TextFormatting.GRAY).append((ITextComponent) new StringTextComponent(slotName).withStyle(TextFormatting.AQUA)));
            tooltip.add((ITextComponent) new StringTextComponent("Removes: ").withStyle(TextFormatting.GRAY).append(attributeTxt));
        }
    }

    public void inventoryTick(final ItemStack stack, final World world, final Entity entity, final int itemSlot, final boolean isSelected) {
        if (world instanceof ServerWorld && entity instanceof ServerPlayerEntity) {
            final ServerPlayerEntity player = (ServerPlayerEntity) entity;
            if (stack.getCount() > 1) {
                while (stack.getCount() > 1) {
                    stack.shrink(1);
                    final ItemStack orb = stack.copy();
                    orb.setCount(1);
                    MiscUtils.giveItem(player, orb);
                }
            }
        }
    }

    @Nullable
    public static Pair<EquipmentSlotType, VAttribute<?, ?>> getPredefinedRemoval(final ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof VoidOrbItem)) {
            return null;
        }
        final CompoundNBT tag = stack.getOrCreateTag();
        if (!tag.contains("slot", 3) || !tag.contains("attribute", 8)) {
            return null;
        }
        final EquipmentSlotType slotType = MiscUtils.getEnumEntry(EquipmentSlotType.class, tag.getInt("slot"));
        final VAttribute<?, ?> attribute = ModAttributes.REGISTRY.get(new ResourceLocation(tag.getString("attribute")));
        if (attribute == null) {
            return null;
        }
        return (Pair<EquipmentSlotType, VAttribute<?, ?>>) new Pair(slotType, attribute);
    }

    public static void setPredefinedRemoval(final ItemStack stack, final EquipmentSlotType slotType, final VAttribute<?, ?> attribute) {
        if (stack.isEmpty() || !(stack.getItem() instanceof VoidOrbItem)) {
            return;
        }
        final CompoundNBT tag = stack.getOrCreateTag();
        tag.putInt("slot", slotType.ordinal());
        tag.putString("attribute", attribute.getId().toString());
    }
}
