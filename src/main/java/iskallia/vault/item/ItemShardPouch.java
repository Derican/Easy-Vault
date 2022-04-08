package iskallia.vault.item;

import iskallia.vault.container.inventory.ShardPouchContainer;
import iskallia.vault.init.ModItems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;


public class ItemShardPouch
        extends Item {
    public ItemShardPouch(ResourceLocation id) {
        super((new Item.Properties())
                .stacksTo(1)
                .tab(ModItems.VAULT_MOD_GROUP));

        setRegistryName(id);
    }


    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        ItemStack contained = getContainedStack(stack);
        if (!contained.isEmpty()) {
            int count = contained.getCount();
            tooltip.add((new StringTextComponent(count + " Shard" + ((count > 1) ? "s" : ""))).withStyle(TextFormatting.GOLD));
        } else {
            tooltip.add((new StringTextComponent("Empty")).withStyle(TextFormatting.GOLD));
        }
    }

    public static int getShardCount(PlayerInventory playerInventory) {
        int shards = 0;
        for (int slot = 0; slot < playerInventory.getContainerSize(); slot++) {
            ItemStack stack = playerInventory.getItem(slot);
            if (stack.getItem() instanceof ItemShardPouch) {
                shards += getContainedStack(stack).getCount();
            } else if (stack.getItem() == ModItems.SOUL_SHARD) {
                shards += stack.getCount();
            }
        }
        return shards;
    }

    public static boolean reduceShardAmount(PlayerInventory playerInventory, int count, boolean simulate) {
        for (int slot = 0; slot < playerInventory.getContainerSize(); slot++) {
            ItemStack stack = playerInventory.getItem(slot);
            if (stack.getItem() instanceof ItemShardPouch) {
                ItemStack shardStack = getContainedStack(stack);
                int toReduce = Math.min(count, shardStack.getCount());
                if (!simulate) {
                    shardStack.setCount(shardStack.getCount() - toReduce);
                    setContainedStack(stack, shardStack);
                }
                count -= toReduce;
            } else if (stack.getItem() == ModItems.SOUL_SHARD) {
                int toReduce = Math.min(count, stack.getCount());
                if (!simulate) {
                    stack.shrink(toReduce);
                    playerInventory.setItem(slot, stack);
                }
                count -= toReduce;
            }
            if (count <= 0) {
                return true;
            }
        }
        return false;
    }

    public static ItemStack getContainedStack(ItemStack pouch) {
        CompoundNBT invTag = pouch.getOrCreateTagElement("Inventory");
        ItemStack stack = ItemStack.of(invTag.getCompound("Stack"));
        stack.setCount(invTag.getInt("StackSize"));
        return stack;
    }

    public static void setContainedStack(ItemStack pouch, ItemStack contained) {
        CompoundNBT invTag = pouch.getOrCreateTagElement("Inventory");
        invTag.put("Stack", (INBT) contained.serializeNBT());
        invTag.putInt("StackSize", contained.getCount());
    }


    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!world.isClientSide() && player instanceof ServerPlayerEntity) {
            final int pouchSlot;
            if (hand == Hand.OFF_HAND) {
                pouchSlot = 40;
            } else {
                pouchSlot = player.inventory.selected;
            }

            NetworkHooks.openGui((ServerPlayerEntity) player, new INamedContainerProvider() {
                public ITextComponent getDisplayName() {
                    return (ITextComponent) new StringTextComponent("Shard Pouch");
                }


                @Nullable
                public Container createMenu(int windowId, PlayerInventory inventory, PlayerEntity player) {
                    return (Container) new ShardPouchContainer(windowId, inventory, pouchSlot);
                }
            }buf -> buf.writeInt(pouchSlot));
        }
        return ActionResult.pass(stack);
    }


    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return (oldStack.getItem() != newStack.getItem());
    }

    public static NonNullSupplier<IItemHandler> getInventorySupplier(final ItemStack stack) {
        return new NonNullSupplier<IItemHandler>() {
            @Nonnull
            public IItemHandler get() {
                return (IItemHandler) new ItemShardPouch.Handler(stack);
            }
        };
    }


    @Nullable
    public ICapabilityProvider initCapabilities(final ItemStack stack, @Nullable CompoundNBT nbt) {
        return new ICapabilityProvider() {
            @Nonnull
            public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
                return (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) ? LazyOptional.of(ItemShardPouch.getInventorySupplier(stack)).cast() : LazyOptional.empty();
            }
        };
    }

    public static class Handler
            extends ItemStackHandler {
        private final ItemStack delegate;

        public Handler(ItemStack delegate) {
            super(1);
            this.delegate = delegate;
            this.stacks.set(0, ItemShardPouch.getContainedStack(this.delegate));
        }


        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            ItemShardPouch.setContainedStack(this.delegate, getStackInSlot(0));
        }


        public int getSlotLimit(int slot) {
            return 2147483582;
        }


        protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
            return getSlotLimit(slot);
        }


        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return (stack.getItem() == ModItems.SOUL_SHARD);
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\ItemShardPouch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */