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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.List;


public class ArtisanScrollItem
        extends BasicItem {
    public ArtisanScrollItem(ResourceLocation id, Item.Properties properties) {
        super(id, properties);
    }


    public ITextComponent getName(ItemStack stack) {
        IFormattableTextComponent displayName = (IFormattableTextComponent) super.getName(stack);
        return (ITextComponent) displayName.setStyle(Style.EMPTY.withColor(Color.fromRgb(-1213660)));
    }


    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(StringTextComponent.EMPTY);
        tooltip.add((new StringTextComponent("Reforges a gear piece to")).withStyle(TextFormatting.GRAY));
        tooltip.add((new StringTextComponent("it's unidentified state,")).withStyle(TextFormatting.GRAY));
        tooltip.add((new StringTextComponent("allowing you to re-roll it")).withStyle(TextFormatting.GRAY));

        Pair<EquipmentSlotType, VAttribute<?, ?>> gearModifier = getPredefinedRoll(stack);
        if (gearModifier != null) {
            String slotName = StringUtils.capitalize(((EquipmentSlotType) gearModifier.getFirst()).getName());
            ITextComponent attributeTxt = VaultGearHelper.getDisplayName((VAttribute) gearModifier.getSecond());

            tooltip.add(StringTextComponent.EMPTY);
            tooltip.add((new StringTextComponent("Only for: ")).withStyle(TextFormatting.GRAY)
                    .append((ITextComponent) (new StringTextComponent(slotName)).withStyle(TextFormatting.AQUA)));
            tooltip.add((new StringTextComponent("Adds: ")).withStyle(TextFormatting.GRAY).append(attributeTxt));
        }
    }


    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (isInitialized(stack) || !(entity instanceof ServerPlayerEntity)) {
            return;
        }
        if (world instanceof net.minecraft.world.server.ServerWorld) {
            ServerPlayerEntity player = (ServerPlayerEntity) entity;
            if (stack.getCount() > 1) {
                while (stack.getCount() > 1) {
                    stack.shrink(1);

                    ItemStack scroll = stack.copy();
                    scroll.setCount(1);
                    MiscUtils.giveItem(player, scroll);
                }
            }
        }

        if (generateRoll(stack)) {
            setInitialized(stack, true);
        }
    }

    public static void setInitialized(ItemStack stack, boolean initialized) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ArtisanScrollItem)) {
            return;
        }
        stack.getOrCreateTag().putBoolean("initialized", initialized);
    }

    public static boolean isInitialized(ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ArtisanScrollItem)) {
            return true;
        }
        return stack.getOrCreateTag().getBoolean("initialized");
    }

    @Nullable
    public static Pair<EquipmentSlotType, VAttribute<?, ?>> getPredefinedRoll(ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ArtisanScrollItem)) {
            return null;
        }

        CompoundNBT tag = stack.getOrCreateTag();
        if (!tag.contains("slot", 3) || !tag.contains("attribute", 8)) {
            return null;
        }

        EquipmentSlotType slotType = (EquipmentSlotType) MiscUtils.getEnumEntry(EquipmentSlotType.class, tag.getInt("slot"));
        VAttribute<?, ?> attribute = (VAttribute<?, ?>) ModAttributes.REGISTRY.get(new ResourceLocation(tag.getString("attribute")));
        if (attribute == null) {
            return null;
        }
        return new Pair(slotType, attribute);
    }

    public static void setPredefinedRoll(ItemStack stack, EquipmentSlotType slotType, VAttribute<?, ?> attribute) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ArtisanScrollItem)) {
            return;
        }

        CompoundNBT tag = stack.getOrCreateTag();
        tag.putInt("slot", slotType.ordinal());
        tag.putString("attribute", attribute.getId().toString());
    }

    private static boolean generateRoll(ItemStack out) {
        Item item;
        VaultGearConfig config = VaultGearConfig.get(VaultGear.Rarity.OMEGA);
        VaultGearConfig.Tier tierConfig = config.TIERS.get(0);
        String itemKey = (String) MiscUtils.getRandomEntry(tierConfig.BASE_MODIFIERS.keySet(), random);

        try {
            item = (Item) ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemKey));
        } catch (Exception exc) {
            return false;
        }
        if (!(item instanceof VaultGear)) {
            return false;
        }
        EquipmentSlotType slotType = ((VaultGear) item).getIntendedSlot();
        if (slotType == null) {
            return false;
        }

        VaultGearConfig.BaseModifiers modifiers = (VaultGearConfig.BaseModifiers) tierConfig.BASE_MODIFIERS.get(itemKey);
        WeightedList<Pair<VAttribute<?, ?>, VAttribute.Instance.Generator<?>>> generatorList = new WeightedList();
        ModAttributes.REGISTRY.values().stream()
                .map(attr -> new Pair(attr, modifiers.getGenerator(attr)))
                .filter(pair -> (pair.getSecond() != null))
                .forEach(pair -> generatorList.add(new Pair(pair.getFirst(), ((WeightedList.Entry) pair.getSecond()).value), ((WeightedList.Entry) pair.getSecond()).weight));


        if (generatorList.isEmpty()) {
            return false;
        }
        Pair<VAttribute<?, ?>, VAttribute.Instance.Generator<?>> generatorPair = (Pair<VAttribute<?, ?>, VAttribute.Instance.Generator<?>>) generatorList.getRandom(random);
        if (generatorPair == null) {
            return false;
        }
        setPredefinedRoll(out, slotType, (VAttribute<?, ?>) generatorPair.getFirst());
        return true;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\ArtisanScrollItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */