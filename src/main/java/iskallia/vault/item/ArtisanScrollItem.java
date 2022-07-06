package iskallia.vault.item;

import com.mojang.datafixers.util.Pair;
import iskallia.vault.attribute.VAttribute;
import iskallia.vault.config.VaultGearConfig;
import iskallia.vault.init.ModAttributes;
import iskallia.vault.item.gear.VaultGear;
import iskallia.vault.item.gear.VaultGearHelper;
import iskallia.vault.util.MiscUtils;
import iskallia.vault.util.data.WeightedList;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class ArtisanScrollItem extends BasicItem {
    public static final WeightedList<EquipmentSlotType> SLOTS;
    public ArtisanScrollItem(final ResourceLocation id, final Item.Properties properties) {
        super(id, properties);
    }

    public ITextComponent getName(final ItemStack stack) {
        final IFormattableTextComponent displayName = (IFormattableTextComponent) super.getName(stack);
        return (ITextComponent) displayName.setStyle(Style.EMPTY.withColor(Color.fromRgb(-1213660)));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(final ItemStack stack, @Nullable final World worldIn, final List<ITextComponent> tooltip, final ITooltipFlag flagIn) {
        tooltip.add(StringTextComponent.EMPTY);
        tooltip.add((ITextComponent) new StringTextComponent("Reforges a gear piece to").withStyle(TextFormatting.GRAY));
        tooltip.add((ITextComponent) new StringTextComponent("it's unidentified state,").withStyle(TextFormatting.GRAY));
        tooltip.add((ITextComponent) new StringTextComponent("allowing you to re-roll it").withStyle(TextFormatting.GRAY));
        final Pair<EquipmentSlotType, VAttribute<?, ?>> gearModifier = getPredefinedRoll(stack);
        if (gearModifier != null) {
            final String slotName = StringUtils.capitalize(((EquipmentSlotType) gearModifier.getFirst()).getName());
            final ITextComponent attributeTxt = VaultGearHelper.getDisplayName((VAttribute<?, ?>) gearModifier.getSecond());
            tooltip.add(StringTextComponent.EMPTY);
            tooltip.add((ITextComponent) new StringTextComponent("Only for: ").withStyle(TextFormatting.GRAY).append((ITextComponent) new StringTextComponent(slotName).withStyle(TextFormatting.AQUA)));
            tooltip.add((ITextComponent) new StringTextComponent("Adds: ").withStyle(TextFormatting.GRAY).append(attributeTxt));
        }
    }

    public void inventoryTick(final ItemStack stack, final World world, final Entity entity, final int itemSlot, final boolean isSelected) {
        if (isInitialized(stack) || !(entity instanceof ServerPlayerEntity)) {
            return;
        }
        if (world instanceof ServerWorld) {
            final ServerPlayerEntity player = (ServerPlayerEntity) entity;
            if (stack.getCount() > 1) {
                while (stack.getCount() > 1) {
                    stack.shrink(1);
                    final ItemStack scroll = stack.copy();
                    scroll.setCount(1);
                    MiscUtils.giveItem(player, scroll);
                }
            }
        }
        if (generateRoll(stack)) {
            setInitialized(stack, true);
        }
    }

    public static void setInitialized(final ItemStack stack, final boolean initialized) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ArtisanScrollItem)) {
            return;
        }
        stack.getOrCreateTag().putBoolean("initialized", initialized);
    }

    public static boolean isInitialized(final ItemStack stack) {
        return stack.isEmpty() || !(stack.getItem() instanceof ArtisanScrollItem) || stack.getOrCreateTag().getBoolean("initialized");
    }

    @Nullable
    public static Pair<EquipmentSlotType, VAttribute<?, ?>> getPredefinedRoll(final ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ArtisanScrollItem)) {
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

    public static void setPredefinedRoll(final ItemStack stack, final EquipmentSlotType slotType, final VAttribute<?, ?> attribute) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ArtisanScrollItem)) {
            return;
        }
        final CompoundNBT tag = stack.getOrCreateTag();
        tag.putInt("slot", slotType.ordinal());
        tag.putString("attribute", attribute.getId().toString());
    }

    private static boolean generateRoll(final ItemStack out) {
        final VaultGearConfig config = VaultGearConfig.get(VaultGear.Rarity.OMEGA);
        final VaultGearConfig.Tier tierConfig = config.TIERS.get(0);
        final String itemKey = MiscUtils.getRandomEntry(tierConfig.BASE_MODIFIERS.keySet(), ArtisanScrollItem.random);
        Item item;
        try {
            item = (Item) ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemKey));
        } catch (final Exception exc) {
            return false;
        }
        if (!(item instanceof VaultGear)) {
            return false;
        }
        EquipmentSlotType slotType = ((VaultGear)item).getIntendedSlot();
        slotType = ArtisanScrollItem.SLOTS.getRandom(new Random());
        if (slotType == null) {
            return false;
        }
        final VaultGearConfig.BaseModifiers modifiers = tierConfig.BASE_MODIFIERS.get(itemKey);
        final WeightedList<Pair<VAttribute<?, ?>, VAttribute.Instance.Generator<?>>> generatorList = new WeightedList<Pair<VAttribute<?, ?>, VAttribute.Instance.Generator<?>>>();
        ModAttributes.REGISTRY.values().stream().map(attr -> new Pair(attr, modifiers.getGenerator(attr))).filter(pair -> pair.getSecond() != null).forEach(pair -> generatorList.add(new Pair(pair.getFirst(), ((WeightedList.Entry) pair.getSecond()).value), ((WeightedList.Entry) pair.getSecond()).weight));
        if (generatorList.isEmpty()) {
            return false;
        }
        final Pair<VAttribute<?, ?>, VAttribute.Instance.Generator<?>> generatorPair = generatorList.getRandom(ArtisanScrollItem.random);
        if (generatorPair == null) {
            return false;
        }
        setPredefinedRoll(out, slotType, (VAttribute<?, ?>) generatorPair.getFirst());
        return true;
    }

    static {
        SLOTS = new WeightedList<EquipmentSlotType>().add(EquipmentSlotType.MAINHAND, 2).add(EquipmentSlotType.OFFHAND, 2).add(EquipmentSlotType.HEAD, 1).add(EquipmentSlotType.CHEST, 1).add(EquipmentSlotType.LEGS, 1).add(EquipmentSlotType.FEET, 1);
    }
}
