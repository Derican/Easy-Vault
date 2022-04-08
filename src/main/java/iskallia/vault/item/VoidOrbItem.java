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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.List;


public class VoidOrbItem
        extends BasicItem {
    public VoidOrbItem(ResourceLocation id, Item.Properties properties) {
        super(id, properties);
    }


    public Rarity getRarity(ItemStack stack) {
        return Rarity.RARE;
    }


    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(StringTextComponent.EMPTY);
        tooltip.add((new StringTextComponent("Removes a modifier at random from")).withStyle(TextFormatting.GRAY));
        tooltip.add((new StringTextComponent("a vault gear item when combined")).withStyle(TextFormatting.GRAY));
        tooltip.add((new StringTextComponent("in an anvil.")).withStyle(TextFormatting.GRAY));

        Pair<EquipmentSlotType, VAttribute<?, ?>> gearModifier = getPredefinedRemoval(stack);
        if (gearModifier != null) {
            String slotName = StringUtils.capitalize(((EquipmentSlotType) gearModifier.getFirst()).getName());
            ITextComponent attributeTxt = VaultGearHelper.getDisplayName((VAttribute) gearModifier.getSecond());

            tooltip.add(StringTextComponent.EMPTY);
            tooltip.add((new StringTextComponent("Only for: ")).withStyle(TextFormatting.GRAY)
                    .append((ITextComponent) (new StringTextComponent(slotName)).withStyle(TextFormatting.AQUA)));
            tooltip.add((new StringTextComponent("Removes: ")).withStyle(TextFormatting.GRAY).append(attributeTxt));
        }
    }


    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (world instanceof net.minecraft.world.server.ServerWorld && entity instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) entity;
            if (stack.getCount() > 1) {
                while (stack.getCount() > 1) {
                    stack.shrink(1);

                    ItemStack orb = stack.copy();
                    orb.setCount(1);
                    MiscUtils.giveItem(player, orb);
                }
            }
        }
    }

    @Nullable
    public static Pair<EquipmentSlotType, VAttribute<?, ?>> getPredefinedRemoval(ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof VoidOrbItem)) {
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

    public static void setPredefinedRemoval(ItemStack stack, EquipmentSlotType slotType, VAttribute<?, ?> attribute) {
        if (stack.isEmpty() || !(stack.getItem() instanceof VoidOrbItem)) {
            return;
        }

        CompoundNBT tag = stack.getOrCreateTag();
        tag.putInt("slot", slotType.ordinal());
        tag.putString("attribute", attribute.getId().toString());
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\VoidOrbItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */