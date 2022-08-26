package iskallia.vault.block.entity;

import net.minecraft.network.NetworkManager;

import javax.annotation.Nullable;

import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.entity.player.ServerPlayerEntity;
import iskallia.vault.world.vault.logic.objective.TreasureHuntObjective;
import iskallia.vault.world.data.VaultRaidData;
import net.minecraft.world.server.ServerWorld;
import iskallia.vault.init.ModConfigs;
import net.minecraft.entity.player.PlayerEntity;

import javax.annotation.Nonnull;

import net.minecraft.tileentity.TileEntityType;
import iskallia.vault.init.ModBlocks;

import java.util.UUID;

import net.minecraft.tileentity.TileEntity;

public class HourglassTileEntity extends TileEntity {
    private UUID ownerUUID;
    private String ownerPlayerName;
    private int currentSand;
    private int totalSand;

    public HourglassTileEntity() {
        super(ModBlocks.HOURGLASS_TILE_ENTITY);
        this.ownerPlayerName = "Unknown";
        this.currentSand = 0;
        this.totalSand = -1;
    }

    public void setOwner(@Nonnull final UUID ownerUUID, @Nonnull final String playerName) {
        this.ownerUUID = ownerUUID;
        this.ownerPlayerName = playerName;
    }

    @Nonnull
    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }

    @Nonnull
    public String getOwnerPlayerName() {
        return this.ownerPlayerName;
    }

    public void setTotalSand(final int totalSand) {
        final int totalSand2 = this.totalSand;
        this.totalSand = totalSand;
        if (totalSand2 != totalSand) {
            this.markForUpdate();
        }
    }

    public boolean addSand(final PlayerEntity player, final int amount) {
        final int total = (this.totalSand <= 0) ? ModConfigs.SAND_EVENT.getTotalSandRequired(player) : this.totalSand;
        if (this.currentSand >= total) {
            return false;
        }
        if (this.ownerUUID != null && !player.getUUID().equals(this.getOwnerUUID())) {
            return false;
        }
        this.currentSand += amount;
        final VaultRaid vault = VaultRaidData.get((ServerWorld) player.level).getActiveFor(player.getUUID());
        if (vault != null) {
            vault.getActiveObjective(TreasureHuntObjective.class).ifPresent(treasureHunt -> treasureHunt.depositSand(vault, (ServerPlayerEntity) player, amount));
        }
        this.markForUpdate();
        return true;
    }

    public float getFilledPercentage() {
        return MathHelper.clamp(this.currentSand / (float) this.totalSand, 0.0f, 1.0f);
    }

    public CompoundNBT save(final CompoundNBT nbt) {
        super.save(nbt);
        if (this.ownerUUID != null) {
            nbt.putUUID("ownerUUID", this.ownerUUID);
        }
        nbt.putString("ownerPlayerName", this.ownerPlayerName);
        nbt.putInt("currentSand", this.currentSand);
        nbt.putInt("totalSand", this.totalSand);
        return nbt;
    }

    public void load(final BlockState state, final CompoundNBT nbt) {
        super.load(state, nbt);
        this.ownerUUID = (nbt.contains("ownerUUID", 11) ? nbt.getUUID("ownerUUID") : null);
        this.ownerPlayerName = nbt.getString("ownerPlayerName");
        this.currentSand = nbt.getInt("currentSand");
        this.totalSand = nbt.getInt("totalSand");
    }

    public CompoundNBT getUpdateTag() {
        final CompoundNBT nbt = super.getUpdateTag();
        this.save(nbt);
        return nbt;
    }

    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.worldPosition, 1, this.getUpdateTag());
    }

    public void onDataPacket(final NetworkManager net, final SUpdateTileEntityPacket pkt) {
        final CompoundNBT nbt = pkt.getTag();
        this.handleUpdateTag(this.getBlockState(), nbt);
    }

    public void markForUpdate() {
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
            this.level.updateNeighborsAt(this.worldPosition, this.getBlockState().getBlock());
            this.setChanged();
        }
    }
}
