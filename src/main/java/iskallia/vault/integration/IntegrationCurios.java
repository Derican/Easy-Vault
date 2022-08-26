package iskallia.vault.integration;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.*;
import java.util.function.BiPredicate;

public class IntegrationCurios {
    public static Collection<CompoundNBT> getSerializedCuriosItemStacks(final PlayerEntity player) {
        return player.getCapability(CuriosCapability.INVENTORY).map(inv -> {
            final List<CompoundNBT> stacks = new ArrayList<CompoundNBT>();
            for (final ICurioStacksHandler handle : inv.getCurios().values()) {
                final IDynamicStackHandler stackHandler = handle.getStacks();
                for (int index = 0; index < stackHandler.getSlots(); ++index) {
                    final ItemStack stack = stackHandler.getStackInSlot(index);
                    if (!stack.isEmpty()) {
                        stacks.add(stack.serializeNBT());
                    }
                }
            }
            return stacks;
        }).orElse(Collections.emptyList());
    }

    public static CompoundNBT getMappedSerializedCuriosItemStacks(final PlayerEntity player, final BiPredicate<PlayerEntity, ItemStack> stackFilter, final boolean removeSnapshotItems) {
        return player.getCapability(CuriosCapability.INVENTORY).map(inv -> {
            final CompoundNBT tag = new CompoundNBT();
            inv.getCurios().forEach((key, handle) -> {
                final CompoundNBT keyMap = new CompoundNBT();
                final IDynamicStackHandler stackHandler = handle.getStacks();
                for (int slot = 0; slot < stackHandler.getSlots(); ++slot) {
                    final ItemStack stack = stackHandler.getStackInSlot(slot);
                    if (stackFilter.test(player, stack)) {
                        if (!stack.isEmpty()) {
                            keyMap.put(String.valueOf(slot), stack.serializeNBT());
                            if (removeSnapshotItems) {
                                stackHandler.setStackInSlot(slot, ItemStack.EMPTY);
                            }
                        }
                    }
                }
                tag.put(key, keyMap);
                return;
            });
            return tag;
        }).orElse(new CompoundNBT());
    }

    public static List<ItemStack> applyMappedSerializedCuriosItemStacks(final PlayerEntity player, final CompoundNBT tag, final boolean replaceExisting) {
        return player.getCapability(CuriosCapability.INVENTORY).map(inv -> {
            final List<ItemStack> filledItems = new ArrayList<ItemStack>();
            for (final String handlerKey : tag.getAllKeys()) {
                inv.getStacksHandler(handlerKey).ifPresent(handle -> {
                    final IDynamicStackHandler stackHandler = handle.getStacks();
                    final CompoundNBT handlerKeyMap = tag.getCompound(handlerKey);

                    for (String strSlot : handlerKeyMap.getAllKeys()) {
                        int slot;
                        try {
                            slot = Integer.parseInt(strSlot);
                        } catch (final NumberFormatException exc) {
                            continue;
                        }
                        if (slot >= 0) {
                            if (slot >= stackHandler.getSlots()) {
                                continue;
                            } else {
                                final ItemStack stack = ItemStack.of(handlerKeyMap.getCompound(strSlot));
                                if (replaceExisting || stackHandler.getStackInSlot(slot).isEmpty()) {
                                    stackHandler.setStackInSlot(slot, stack);
                                } else {
                                    filledItems.add(stack);
                                }
                            }
                        }
                    }
                    return;
                });
            }
            return filledItems;
        }).orElse(Collections.emptyList());
    }
}
