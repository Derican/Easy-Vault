package iskallia.vault.entity.eternal;

import iskallia.vault.entity.EternalEntity;
import iskallia.vault.init.ModAttributes;
import iskallia.vault.init.ModEntities;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.*;

public class EternalHelper {
    private static final UUID ETERNAL_SIZE_INCREASE = UUID.fromString("de6b75be-deb2-4711-8fac-08465031b2c3");

    public static EternalEntity spawnEternal(World world, EternalDataAccess dataAccess) {
        return spawnEternal(world, dataAccess.getLevel(), dataAccess.isAncient(), dataAccess.getName(), dataAccess.getEquipment(), dataAccess.getEntityAttributes());
    }


    private static EternalEntity spawnEternal(World world, int level, boolean isAncient, String name, Map<EquipmentSlotType, ItemStack> equipment, Map<Attribute, Float> attributes) {
        EternalEntity eternal = (EternalEntity) ModEntities.ETERNAL.create(world);
        eternal.setCustomName((ITextComponent) (new StringTextComponent("[")).withStyle(TextFormatting.GREEN)
                .append((ITextComponent) (new StringTextComponent(String.valueOf(level))).withStyle(TextFormatting.RED))
                .append((ITextComponent) (new StringTextComponent("] " + name)).withStyle(TextFormatting.GREEN)));
        eternal.setSkinName(name);

        equipment.forEach((slot, stack) -> {
            eternal.setItemSlot(slot, stack.copy());

            eternal.setDropChance(slot, 0.0F);
        });
        attributes.forEach((attribute, value) -> eternal.getAttribute(attribute).setBaseValue(value.floatValue()));


        eternal.heal(2.14748365E9F);

        if (isAncient) {
            eternal.getAttribute(ModAttributes.SIZE_SCALE).setBaseValue(1.2000000476837158D);
        }
        return eternal;
    }

    public static float getEternalGearModifierAdjustments(EternalDataAccess dataAccess, Attribute attribute, float value) {
        Map<AttributeModifier.Operation, List<AttributeModifier>> modifiers = new HashMap<>();
        for (EquipmentSlotType slotType : EquipmentSlotType.values()) {
            ItemStack stack = dataAccess.getEquipment().getOrDefault(slotType, ItemStack.EMPTY);
            if (!stack.isEmpty()) {

                stack.getAttributeModifiers(slotType).get(attribute).forEach(modifier -> ((List<AttributeModifier>) modifiers.computeIfAbsent(modifier.getOperation(), (AttributeModifier.Operation operation) -> {
                    return null;
                })).add(modifier));
            }
        }


        for (AttributeModifier modifier : modifiers.getOrDefault(AttributeModifier.Operation.ADDITION, Collections.emptyList())) {
            value = (float) (value + modifier.getAmount());
        }
        float val = value;
        for (AttributeModifier modifier : modifiers.getOrDefault(AttributeModifier.Operation.MULTIPLY_BASE, Collections.emptyList())) {
            val = (float) (val + value * modifier.getAmount());
        }
        for (AttributeModifier modifier : modifiers.getOrDefault(AttributeModifier.Operation.MULTIPLY_TOTAL, Collections.emptyList())) {
            val = (float) (val * modifier.getAmount());
        }
        return val;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\entity\eternal\EternalHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */