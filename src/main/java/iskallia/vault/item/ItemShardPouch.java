// 
// Decompiled by Procyon v0.6.0
// 

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

public class ItemShardPouch extends Item
{
    public ItemShardPouch(final ResourceLocation id) {
        super(new Item.Properties().stacksTo(1).tab(ModItems.VAULT_MOD_GROUP));
        this.setRegistryName(id);
    }
    
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(final ItemStack stack, @Nullable final World world, final List<ITextComponent> tooltip, final ITooltipFlag flag) {
        final ItemStack contained = getContainedStack(stack);
        if (!contained.isEmpty()) {
            final int count = contained.getCount();
            tooltip.add((ITextComponent)new StringTextComponent(count + " Shard" + ((count > 1) ? "s" : "")).withStyle(TextFormatting.GOLD));
        }
        else {
            tooltip.add((ITextComponent)new StringTextComponent("Empty").withStyle(TextFormatting.GOLD));
        }
    }
    
    public static int getShardCount(final PlayerInventory playerInventory) {
        int shards = 0;
        for (int slot = 0; slot < playerInventory.getContainerSize(); ++slot) {
            final ItemStack stack = playerInventory.getItem(slot);
            if (stack.getItem() instanceof ItemShardPouch) {
                shards += getContainedStack(stack).getCount();
            }
            else if (stack.getItem() == ModItems.SOUL_SHARD) {
                shards += stack.getCount();
            }
        }
        return shards;
    }
    
    public static boolean reduceShardAmount(final PlayerInventory playerInventory, int count, final boolean simulate) {
        for (int slot = 0; slot < playerInventory.getContainerSize(); ++slot) {
            final ItemStack stack = playerInventory.getItem(slot);
            if (stack.getItem() instanceof ItemShardPouch) {
                final ItemStack shardStack = getContainedStack(stack);
                final int toReduce = Math.min(count, shardStack.getCount());
                if (!simulate) {
                    shardStack.setCount(shardStack.getCount() - toReduce);
                    setContainedStack(stack, shardStack);
                }
                count -= toReduce;
            }
            else if (stack.getItem() == ModItems.SOUL_SHARD) {
                final int toReduce2 = Math.min(count, stack.getCount());
                if (!simulate) {
                    stack.shrink(toReduce2);
                    playerInventory.setItem(slot, stack);
                }
                count -= toReduce2;
            }
            if (count <= 0) {
                return true;
            }
        }
        return false;
    }
    
    public static ItemStack getContainedStack(final ItemStack pouch) {
        final CompoundNBT invTag = pouch.getOrCreateTagElement("Inventory");
        final ItemStack stack = ItemStack.of(invTag.getCompound("Stack"));
        stack.setCount(invTag.getInt("StackSize"));
        return stack;
    }
    
    public static void setContainedStack(final ItemStack pouch, final ItemStack contained) {
        final CompoundNBT invTag = pouch.getOrCreateTagElement("Inventory");
        invTag.put("Stack", (INBT)contained.serializeNBT());
        invTag.putInt("StackSize", contained.getCount());
    }
    
    public ActionResult<ItemStack> use(final World world, final PlayerEntity player, final Hand hand) {
        final ItemStack stack = player.getItemInHand(hand);
        if (!world.isClientSide() && player instanceof ServerPlayerEntity) {
            int pouchSlot;
            if (hand == Hand.OFF_HAND) {
                pouchSlot = 40;
            }
            else {
                pouchSlot = player.inventory.selected;
            }
            NetworkHooks.openGui((ServerPlayerEntity)player, (INamedContainerProvider)new INamedContainerProvider() {
                public ITextComponent getDisplayName() {
                    return (ITextComponent)new StringTextComponent("Shard Pouch");
                }
                
                @Nullable
                public Container createMenu(final int windowId, final PlayerInventory inventory, final PlayerEntity player) {
                    return new ShardPouchContainer(windowId, inventory, pouchSlot);
                }
            }, buf -> buf.writeInt(pouchSlot));
        }
        return (ActionResult<ItemStack>)ActionResult.pass(stack);
    }
    
    public boolean shouldCauseReequipAnimation(final ItemStack oldStack, final ItemStack newStack, final boolean slotChanged) {
        return oldStack.getItem() != newStack.getItem();
    }
    
    public static NonNullSupplier<IItemHandler> getInventorySupplier(final ItemStack stack) {
        return (NonNullSupplier<IItemHandler>)new NonNullSupplier<IItemHandler>() {
            @Nonnull
            public IItemHandler get() {
                return (IItemHandler)new Handler(stack);
            }
        };
    }
    
    @Nullable
    public ICapabilityProvider initCapabilities(final ItemStack stack, @Nullable final CompoundNBT nbt) {
        return (ICapabilityProvider)new ICapabilityProvider() {
            @Nonnull
            public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> cap, @Nullable final Direction side) {
                return (LazyOptional<T>)((cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) ? LazyOptional.of((NonNullSupplier)ItemShardPouch.getInventorySupplier(stack)).cast() : LazyOptional.empty());
            }
        };
    }
    
    public static class Handler extends ItemStackHandler
    {
        private final ItemStack delegate;
        
        public Handler(final ItemStack delegate) {
            super(1);
            this.delegate = delegate;
            this.stacks.set(0, ItemShardPouch.getContainedStack(this.delegate));
        }
        
        protected void onContentsChanged(final int slot) {
            super.onContentsChanged(slot);
            ItemShardPouch.setContainedStack(this.delegate, this.getStackInSlot(0));
        }
        
        public int getSlotLimit(final int slot) {
            return 2147483582;
        }
        
        protected int getStackLimit(final int slot, @Nonnull final ItemStack stack) {
            return this.getSlotLimit(slot);
        }
        
        public boolean isItemValid(final int slot, @Nonnull final ItemStack stack) {
            return stack.getItem() == ModItems.SOUL_SHARD;
        }
    }
}
