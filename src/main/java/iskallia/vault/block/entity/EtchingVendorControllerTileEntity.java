package iskallia.vault.block.entity;

import iskallia.vault.Vault;
import iskallia.vault.config.EtchingConfig;
import iskallia.vault.entity.EtchingVendorEntity;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModEntities;
import iskallia.vault.item.gear.EtchingItem;
import iskallia.vault.item.gear.VaultGear;
import iskallia.vault.util.MathUtilities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EtchingVendorControllerTileEntity extends TileEntity implements ITickableTileEntity {
    private int monitoredEntityId = -1;
    private final List<EtchingTrade> trades = new ArrayList<>();

    public EtchingVendorControllerTileEntity() {
        super(ModBlocks.ETCHING_CONTROLLER_TILE_ENTITY);
    }

    public void tick() {
        Entity monitoredEntity;
        if (getLevel().isClientSide() || !(getLevel() instanceof ServerWorld)) {
            return;
        }
        if (getLevel().dimension() != Vault.VAULT_KEY) {
            return;
        }

        if (this.trades.isEmpty()) {
            generateTrades();
            sendUpdates();
        }


        if (this.monitoredEntityId == -1) {
            monitoredEntity = createVendor();
        } else if ((monitoredEntity = this.level.getEntity(this.monitoredEntityId)) == null) {
            monitoredEntity = createVendor();
        }
        monitoredEntity.setPos(this.worldPosition.getX() + 0.5D, this.worldPosition.getY(), this.worldPosition.getZ() + 0.5D);
    }

    private Entity createVendor() {
        ServerWorld sWorld = (ServerWorld) getLevel();
        EtchingVendorEntity vendor = (EtchingVendorEntity) ModEntities.ETCHING_VENDOR.create(sWorld, null, null, null,
                getBlockPos(), SpawnReason.STRUCTURE, false, false);
        vendor.setVendorPos(getBlockPos());
        sWorld.addFreshEntity((Entity) vendor);
        this.monitoredEntityId = vendor.getId();
        return (Entity) vendor;
    }

    private void generateTrades() {
        Random r = new Random();
        for (int i = 0; i < 3; i++) {
            VaultGear.Set etchingSet = ModConfigs.ETCHING.getRandomSet();
            EtchingConfig.Etching etching = ModConfigs.ETCHING.getFor(etchingSet);

            ItemStack etchingStack = EtchingItem.createEtchingStack(etchingSet);
            int amount = MathUtilities.getRandomInt(etching.minValue, etching.maxValue + 1);
            this.trades.add(new EtchingTrade(etchingStack, amount, false));
        }
    }

    public int getMonitoredEntityId() {
        return this.monitoredEntityId;
    }

    public void setMonitoredEntityId(int id) {
        if (this.monitoredEntityId == -1) {
            this.monitoredEntityId = id;
        }
    }

    @Nullable
    public EtchingTrade getTrade(int id) {
        if (id < 0 || id >= this.trades.size()) {
            return null;
        }
        return this.trades.get(id);
    }


    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);

        ListNBT trades = nbt.getList("trades", 10);
        for (int i = 0; i < trades.size(); i++) {
            CompoundNBT tradeTag = trades.getCompound(i);

            this.trades.add(EtchingTrade.deserialize(tradeTag));
        }
    }


    public CompoundNBT save(CompoundNBT compound) {
        CompoundNBT tag = super.save(compound);

        ListNBT trades = new ListNBT();
        for (EtchingTrade trade : this.trades) {
            trades.add(trade.serialize());
        }

        compound.put("trades", (INBT) trades);
        return tag;
    }


    public CompoundNBT getUpdateTag() {
        return save(new CompoundNBT());
    }


    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        load(state, tag);
    }


    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.worldPosition, 1, getUpdateTag());
    }


    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT tag = pkt.getTag();
        handleUpdateTag(getBlockState(), tag);
    }

    public void sendUpdates() {
        this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), 11);
        this.level.updateNeighborsAt(this.worldPosition, getBlockState().getBlock());
        setChanged();
    }

    public static class EtchingTrade {
        private final ItemStack soldEtching;
        private final int requiredPlatinum;
        private boolean sold;

        public EtchingTrade(ItemStack soldEtching, int requiredPlatinum, boolean sold) {
            this.soldEtching = soldEtching;
            this.requiredPlatinum = requiredPlatinum;
            this.sold = sold;
        }

        public ItemStack getSoldEtching() {
            return this.soldEtching;
        }

        public int getRequiredPlatinum() {
            return this.requiredPlatinum;
        }

        public void setSold(boolean sold) {
            this.sold = sold;
        }

        public boolean isSold() {
            return this.sold;
        }

        public CompoundNBT serialize() {
            CompoundNBT nbt = new CompoundNBT();
            nbt.put("stack", (INBT) this.soldEtching.serializeNBT());
            nbt.putInt("amount", this.requiredPlatinum);
            nbt.putBoolean("sold", this.sold);
            return nbt;
        }

        public static EtchingTrade deserialize(CompoundNBT nbt) {
            ItemStack stack = ItemStack.of(nbt.getCompound("stack"));
            int amount = nbt.getInt("amount");
            boolean sold = nbt.getBoolean("sold");
            return new EtchingTrade(stack, amount, sold);
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\entity\EtchingVendorControllerTileEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */