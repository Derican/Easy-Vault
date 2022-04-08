package iskallia.vault.skill.set;

import com.google.gson.annotations.Expose;
import iskallia.vault.attribute.EnumAttribute;
import iskallia.vault.init.ModAttributes;
import iskallia.vault.item.gear.VaultGear;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiPredicate;

public class PlayerSet {
    @Expose
    private String set;

    public PlayerSet(VaultGear.Set set) {
        this.set = set.name();
    }

    public VaultGear.Set getSet() {
        return VaultGear.Set.valueOf(this.set);
    }

    public boolean shouldBeActive(LivingEntity player) {
        return isActive(getSet(), player);
    }


    public void onAdded(PlayerEntity player) {
    }


    public void onTick(PlayerEntity player) {
    }


    public void onRemoved(PlayerEntity player) {
    }


    public static boolean allMatch(LivingEntity player, BiPredicate<EquipmentSlotType, ItemStack> predicate, EquipmentSlotType... slots) {
        return Arrays.<EquipmentSlotType>stream(slots).allMatch(slot -> predicate.test(slot, player.getItemBySlot(slot)));
    }

    public static boolean allMatch(LivingEntity player, BiPredicate<EquipmentSlotType, ItemStack> predicate) {
        return allMatch(player, predicate, new EquipmentSlotType[]{EquipmentSlotType.HEAD, EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET});
    }

    public static boolean isActive(VaultGear.Set set, LivingEntity player) {
        return allMatch(player, (slot, stack) -> {
            Optional<EnumAttribute<VaultGear.Set>> attribute = ModAttributes.GEAR_SET.get(stack);
            return (attribute.isPresent() && ((EnumAttribute) attribute.get()).getValue(stack) == set);
        });
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\set\PlayerSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */