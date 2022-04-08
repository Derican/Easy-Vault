package iskallia.vault.block.entity;

import iskallia.vault.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class VaultCrateTileEntity
        extends TileEntity {
    private ItemStackHandler itemHandler = createHandler();
    private LazyOptional<IItemHandler> handler = LazyOptional.of(() -> this.itemHandler);

    public VaultCrateTileEntity() {
        super(ModBlocks.VAULT_CRATE_TILE_ENTITY);
    }

    public void sendUpdates() {
        this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), 3);
        this.level.updateNeighborsAt(this.worldPosition, getBlockState().getBlock());
        setChanged();
    }


    public CompoundNBT save(CompoundNBT compound) {
        compound.put("inv", (INBT) this.itemHandler.serializeNBT());
        return super.save(compound);
    }


    public void load(BlockState state, CompoundNBT nbt) {
        nbt.getCompound("inv").remove("Size");
        this.itemHandler.deserializeNBT(nbt.getCompound("inv"));
        super.load(state, nbt);
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(54) {
            protected void onContentsChanged(int slot) {
                VaultCrateTileEntity.this.sendUpdates();
            }


            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                if (Block.byItem(stack.getItem()) instanceof net.minecraft.block.ShulkerBoxBlock ||
                        Block.byItem(stack.getItem()) instanceof iskallia.vault.block.VaultCrateBlock) {
                    return false;
                }
                return true;
            }


            @Nonnull
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                return super.insertItem(slot, stack, simulate);
            }
        };
    }


    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return this.handler.cast();
        }
        return super.getCapability(cap, side);
    }

    public CompoundNBT saveToNbt() {
        return this.itemHandler.serializeNBT();
    }

    public void loadFromNBT(CompoundNBT nbt) {
        this.itemHandler.deserializeNBT(nbt);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\entity\VaultCrateTileEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */