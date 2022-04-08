package iskallia.vault.integration;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;

public class IntegrationCurios {
    public static Collection<CompoundNBT> getSerializedCuriosItemStacks(PlayerEntity player) {
        return player.getCapability(CuriosCapability.INVENTORY).map(inv -> {
            List<CompoundNBT> stacks = new ArrayList<>();
            for (ICurioStacksHandler handle : inv.getCurios().values()) {
                IDynamicStackHandler stackHandler = handle.getStacks();
                for (int index = 0; index < stackHandler.getSlots(); index++) {
                    ItemStack stack = stackHandler.getStackInSlot(index);
                    if (!stack.isEmpty()) {
                        stacks.add(stack.serializeNBT());
                    }
                }
            }
            return stacks;
        }).orElse(Collections.emptyList());
    }

    public static CompoundNBT getMappedSerializedCuriosItemStacks(PlayerEntity player, BiPredicate<PlayerEntity, ItemStack> stackFilter, boolean removeSnapshotItems) {
        return player.getCapability(CuriosCapability.INVENTORY).map(inv -> {
            CompoundNBT tag = new CompoundNBT();


//            inv.getCurios().forEach(());


            return tag;
        }).orElse(new CompoundNBT());
    }

    public static List<ItemStack> applyMappedSerializedCuriosItemStacks(PlayerEntity player, CompoundNBT tag, boolean replaceExisting) {
        return player.getCapability(CuriosCapability.INVENTORY).map(inv -> {
            List<ItemStack> filledItems = new ArrayList<>();


            for (String handlerKey : tag.getAllKeys()) {
//                inv.getStacksHandler(handlerKey).ifPresent(());
            }


            return filledItems;
        }).orElse(Collections.emptyList());
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\integration\IntegrationCurios.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */