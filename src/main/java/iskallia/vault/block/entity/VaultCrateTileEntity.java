package iskallia.vault.block.entity;

import iskallia.vault.block.VaultCrateBlock;
import iskallia.vault.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class VaultCrateTileEntity extends TileEntity {
    private final ItemStackHandler itemHandler;
    private final LazyOptional<IItemHandler> handler;

    public VaultCrateTileEntity() {
        super(ModBlocks.VAULT_CRATE_TILE_ENTITY);
        this.itemHandler = this.createHandler();
        this.handler = LazyOptional.of(() -> this.itemHandler);
    }

    public void sendUpdates() {
        this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        this.level.updateNeighborsAt(this.worldPosition, this.getBlockState().getBlock());
        this.setChanged();
    }

    public CompoundNBT save(final CompoundNBT compound) {
        compound.put("inv", this.itemHandler.serializeNBT());
        return super.save(compound);
    }

    public void load(final BlockState state, final CompoundNBT nbt) {
        nbt.getCompound("inv").remove("Size");
        this.itemHandler.deserializeNBT(nbt.getCompound("inv"));
        super.load(state, nbt);
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(54) {
            protected void onContentsChanged(final int slot) {
                VaultCrateTileEntity.this.sendUpdates();
            }

            public boolean isItemValid(final int slot, @Nonnull final ItemStack stack) {
                return !(Block.byItem(stack.getItem()) instanceof ShulkerBoxBlock) && !(Block.byItem(stack.getItem()) instanceof VaultCrateBlock);
            }

            @Nonnull
            public ItemStack insertItem(final int slot, @Nonnull final ItemStack stack, final boolean simulate) {
                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> cap, @Nullable final Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return this.handler.cast();
        }
        return (LazyOptional<T>) super.getCapability((Capability) cap, side);
    }

    public CompoundNBT saveToNbt() {
        return this.itemHandler.serializeNBT();
    }

    public void loadFromNBT(final CompoundNBT nbt) {
        this.itemHandler.deserializeNBT(nbt);
    }
}
