// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.block.entity;

import com.google.common.collect.Iterables;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.item.ItemTraderCore;
import iskallia.vault.util.SkinProfile;
import iskallia.vault.util.nbt.NBTSerializer;
import iskallia.vault.vending.TraderCore;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntityType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class VendingMachineTileEntity extends SkinnableTileEntity
{
    private List<TraderCore> cores;
    
    public VendingMachineTileEntity() {
        super(ModBlocks.VENDING_MACHINE_TILE_ENTITY);
        this.cores = new ArrayList<TraderCore>();
    }
    
    public <T extends VendingMachineTileEntity> VendingMachineTileEntity(final TileEntityType<T> type) {
        super(type);
        this.cores = new ArrayList<TraderCore>();
        this.skin = new SkinProfile();
    }
    
    public List<TraderCore> getCores() {
        return this.cores;
    }
    
    public void addCore(final TraderCore core) {
        this.cores.add(core);
        this.updateSkin();
        this.sendUpdates();
    }
    
    public TraderCore getLastCore() {
        if (this.cores == null || this.cores.size() == 0) {
            return null;
        }
        return this.cores.get(this.cores.size() - 1);
    }
    
    public ItemStack getTraderCoreStack() {
        final TraderCore lastCore = this.getLastCore();
        if (lastCore == null) {
            return ItemStack.EMPTY;
        }
        final ItemStack stack = ItemTraderCore.getStackFromCore(lastCore);
        this.cores.remove(lastCore);
        return stack;
    }
    
    public TraderCore getRenderCore() {
        if (this.cores == null) {
            return null;
        }
        return (TraderCore)Iterables.getFirst((Iterable)this.cores, null);
    }
    
    public void updateSkin() {
        final TraderCore lastCore = this.getLastCore();
        if (lastCore == null) {
            return;
        }
        this.skin.updateSkin(lastCore.getName());
    }
    
    public CompoundNBT save(final CompoundNBT compound) {
        final ListNBT list = new ListNBT();
        for (final TraderCore core : this.cores) {
            try {
                list.add(NBTSerializer.serialize(core));
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
        }
        compound.put("coresList", (INBT)list);
        return super.save(compound);
    }
    
    public void load(final BlockState state, final CompoundNBT nbt) {
        final ListNBT list = nbt.getList("coresList", 10);
        this.cores = new LinkedList<TraderCore>();
        for (final INBT tag : list) {
            TraderCore core = null;
            try {
                core = NBTSerializer.deserialize(TraderCore.class, (CompoundNBT)tag);
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
            this.cores.add(core);
        }
        this.updateSkin();
        super.load(state, nbt);
    }
    
    public CompoundNBT getUpdateTag() {
        final CompoundNBT nbt = super.getUpdateTag();
        final ListNBT list = new ListNBT();
        for (final TraderCore core : this.cores) {
            try {
                list.add(NBTSerializer.serialize(core));
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
        }
        nbt.put("coresList", (INBT)list);
        return nbt;
    }
    
    @Override
    public void handleUpdateTag(final BlockState state, final CompoundNBT tag) {
        this.load(state, tag);
    }
    
    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.worldPosition, 1, this.getUpdateTag());
    }
    
    @Override
    public void onDataPacket(final NetworkManager net, final SUpdateTileEntityPacket pkt) {
        final CompoundNBT nbt = pkt.getTag();
        this.handleUpdateTag(this.getBlockState(), nbt);
    }
}
