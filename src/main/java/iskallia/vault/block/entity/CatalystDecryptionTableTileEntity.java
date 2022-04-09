// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.block.entity;

import iskallia.vault.container.inventory.CatalystDecryptionContainer;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.item.VaultCatalystItem;
import iskallia.vault.item.VaultInhibitorItem;
import iskallia.vault.item.crystal.VaultCrystalItem;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CatalystDecryptionTableTileEntity extends TileEntity implements INamedContainerProvider, ITickableTileEntity
{
    private final ItemStackHandler handler;
    
    public CatalystDecryptionTableTileEntity() {
        super((TileEntityType)ModBlocks.CATALYST_DECRYPTION_TABLE_TILE_ENTITY);
        this.handler = new ItemStackHandler(11) {
            protected void onContentsChanged(final int slot) {
                super.onContentsChanged(slot);
                CatalystDecryptionTableTileEntity.this.sendUpdates();
            }
        };
    }
    
    public void sendUpdates() {
        this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        this.level.updateNeighborsAt(this.worldPosition, this.getBlockState().getBlock());
        this.setChanged();
    }
    
    public void tick() {
        for (int slot = 0; slot < this.handler.getSlots(); ++slot) {
            final ItemStack stack = this.handler.getStackInSlot(slot);
            if (stack.getItem() instanceof VaultCatalystItem) {
                VaultCatalystItem.getSeed(stack);
            }
            if (stack.getItem() instanceof VaultCrystalItem) {
                VaultCrystalItem.getSeed(stack);
            }
            if (stack.getItem() instanceof VaultInhibitorItem) {
                VaultInhibitorItem.getSeed(stack);
            }
        }
    }
    
    public void load(final BlockState state, final CompoundNBT tag) {
        this.handler.deserializeNBT(tag.getCompound("inventory"));
        super.load(state, tag);
    }
    
    public CompoundNBT save(final CompoundNBT tag) {
        tag.put("inventory", (INBT)this.handler.serializeNBT());
        return super.save(tag);
    }
    
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> cap, @Nullable final Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (LazyOptional<T>)LazyOptional.of(() -> this.handler).cast();
        }
        return (LazyOptional<T>)super.getCapability((Capability)cap, side);
    }
    
    public ITextComponent getDisplayName() {
        return (ITextComponent)new StringTextComponent("Catalyst Decryption Table");
    }
    
    @Nullable
    public Container createMenu(final int windowId, final PlayerInventory playerInventory, final PlayerEntity player) {
        if (this.getLevel() == null) {
            return null;
        }
        return new CatalystDecryptionContainer(windowId, this.getLevel(), this.getBlockPos(), playerInventory);
    }
}
