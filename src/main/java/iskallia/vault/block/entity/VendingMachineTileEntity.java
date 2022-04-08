package iskallia.vault.block.entity;

import com.google.common.collect.Iterables;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.item.ItemTraderCore;
import iskallia.vault.util.SkinProfile;
import iskallia.vault.util.nbt.INBTSerializable;
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


public class VendingMachineTileEntity
        extends SkinnableTileEntity {
    private List<TraderCore> cores = new ArrayList<>();

    public VendingMachineTileEntity() {
        super(ModBlocks.VENDING_MACHINE_TILE_ENTITY);
    }

    public <T extends VendingMachineTileEntity> VendingMachineTileEntity(TileEntityType<T> type) {
        super(type);
        this.skin = new SkinProfile();
    }


    public List<TraderCore> getCores() {
        return this.cores;
    }

    public void addCore(TraderCore core) {
        this.cores.add(core);
        updateSkin();
        sendUpdates();
    }

    public TraderCore getLastCore() {
        if (this.cores == null || this.cores.size() == 0) return null;
        return this.cores.get(this.cores.size() - 1);
    }

    public ItemStack getTraderCoreStack() {
        TraderCore lastCore = getLastCore();
        if (lastCore == null) return ItemStack.EMPTY;
        ItemStack stack = ItemTraderCore.getStackFromCore(lastCore);
        this.cores.remove(lastCore);
        return stack;
    }

    public TraderCore getRenderCore() {
        if (this.cores == null) {
            return null;
        }
        return (TraderCore) Iterables.getFirst(this.cores, null);
    }


    public void updateSkin() {
        TraderCore lastCore = getLastCore();
        if (lastCore == null)
            return;
        this.skin.updateSkin(lastCore.getName());
    }


    public CompoundNBT save(CompoundNBT compound) {
        ListNBT list = new ListNBT();
        for (TraderCore core : this.cores) {
            try {
                list.add(NBTSerializer.serialize((INBTSerializable) core));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        compound.put("coresList", (INBT) list);
        return super.save(compound);
    }


    public void load(BlockState state, CompoundNBT nbt) {
        ListNBT list = nbt.getList("coresList", 10);
        this.cores = new LinkedList<>();
        for (INBT tag : list) {
            TraderCore core = null;
            try {
                core = (TraderCore) NBTSerializer.deserialize(TraderCore.class, (CompoundNBT) tag);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.cores.add(core);
        }
        updateSkin();
        super.load(state, nbt);
    }


    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = super.getUpdateTag();

        ListNBT list = new ListNBT();
        for (TraderCore core : this.cores) {
            try {
                list.add(NBTSerializer.serialize((INBTSerializable) core));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        nbt.put("coresList", (INBT) list);

        return nbt;
    }


    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        load(state, tag);
    }


    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.worldPosition, 1, getUpdateTag());
    }


    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT nbt = pkt.getTag();
        handleUpdateTag(getBlockState(), nbt);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\entity\VendingMachineTileEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */