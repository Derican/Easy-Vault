package iskallia.vault.block.base;

import iskallia.vault.world.data.PlayerFavourData;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.function.Consumer;

public abstract class FillableAltarTileEntity extends TileEntity implements ITickableTileEntity {
    protected static final Random rand;
    private int currentProgress;
    private int maxProgress;

    public FillableAltarTileEntity(final TileEntityType<?> tileEntityTypeIn) {
        super((TileEntityType) tileEntityTypeIn);
        this.currentProgress = 0;
        this.maxProgress = 0;
    }

    public boolean initialized() {
        return this.getMaxProgress() > 0;
    }

    public int getCurrentProgress() {
        return this.currentProgress;
    }

    public int getMaxProgress() {
        return this.maxProgress;
    }

    public boolean isMaxedOut() {
        return this.currentProgress >= this.getMaxProgress();
    }

    public float progressPercentage() {
        return Math.min(this.getCurrentProgress() / (float) this.getMaxProgress(), 1.0f);
    }

    public void makeProgress(final ServerPlayerEntity sPlayer, final int deltaProgress, final Consumer<ServerPlayerEntity> onComplete) {
        if (!this.initialized()) {
            return;
        }
        this.currentProgress += deltaProgress;
        this.sendUpdates();
        if (this.isMaxedOut()) {
            onComplete.accept(sPlayer);
        }
    }

    public void tick() {
        if (this.initialized()) {
            return;
        }
        if (this.getLevel() instanceof ServerWorld) {
            this.getCurrentVault().flatMap(this::calcMaxProgress).ifPresent(maxProgress -> {
                this.maxProgress = maxProgress;
                this.sendUpdates();
            });
        }
    }

    private Optional<VaultRaid> getCurrentVault() {
        final ServerWorld sWorld = (ServerWorld) this.getLevel();
        return Optional.ofNullable(VaultRaidData.get(sWorld).getAt(sWorld, this.getBlockPos()));
    }

    protected float getMaxProgressMultiplier(final UUID playerUUID) {
        if (!(this.getLevel() instanceof ServerWorld)) {
            return 1.0f;
        }
        final ServerWorld sWorld = (ServerWorld) this.getLevel();
        final int favour = PlayerFavourData.get(sWorld).getFavour(playerUUID, this.getAssociatedVaultGod());
        if (favour < 0) {
            return 1.0f + 0.2f * (Math.abs(favour) / 6.0f);
        }
        return 1.0f - 0.75f * (Math.min((float) favour, 8.0f) / 8.0f);
    }

    public abstract ITextComponent getRequirementName();

    public abstract PlayerFavourData.VaultGodType getAssociatedVaultGod();

    public abstract ITextComponent getRequirementUnit();

    public abstract Color getFillColor();

    protected abstract Optional<Integer> calcMaxProgress(final VaultRaid p0);

    public CompoundNBT save(final CompoundNBT nbt) {
        nbt.putInt("CurrentProgress", this.currentProgress);
        nbt.putInt("CalculatedMaxProgress", this.maxProgress);
        return super.save(nbt);
    }

    public void load(final BlockState state, final CompoundNBT nbt) {
        this.currentProgress = nbt.getInt("CurrentProgress");
        this.maxProgress = (nbt.contains("CalculatedMaxProgress") ? nbt.getInt("CalculatedMaxProgress") : -1);
        super.load(state, nbt);
    }

    public CompoundNBT getUpdateTag() {
        final CompoundNBT nbt = super.getUpdateTag();
        this.save(nbt);
        return nbt;
    }

    public void handleUpdateTag(final BlockState state, final CompoundNBT nbt) {
        this.load(state, nbt);
    }

    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.worldPosition, 1, this.getUpdateTag());
    }

    public void onDataPacket(final NetworkManager net, final SUpdateTileEntityPacket pkt) {
        final CompoundNBT nbt = pkt.getTag();
        this.handleUpdateTag(this.getBlockState(), nbt);
    }

    public void sendUpdates() {
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
            this.level.updateNeighborsAt(this.worldPosition, this.getBlockState().getBlock());
            this.setChanged();
        }
    }

    static {
        rand = new Random();
    }
}
