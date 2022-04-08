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
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.function.Consumer;

public abstract class FillableAltarTileEntity
        extends TileEntity
        implements ITickableTileEntity {
    protected static final Random rand = new Random();
    private int currentProgress = 0;
    private int maxProgress = 0;

    public FillableAltarTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public boolean initialized() {
        return (getMaxProgress() > 0);
    }

    public int getCurrentProgress() {
        return this.currentProgress;
    }

    public int getMaxProgress() {
        return this.maxProgress;
    }

    public boolean isMaxedOut() {
        return (this.currentProgress >= getMaxProgress());
    }

    public float progressPercentage() {
        return Math.min(getCurrentProgress() / getMaxProgress(), 1.0F);
    }

    public void makeProgress(ServerPlayerEntity sPlayer, int deltaProgress, Consumer<ServerPlayerEntity> onComplete) {
        if (!initialized()) {
            return;
        }
        this.currentProgress += deltaProgress;
        sendUpdates();
        if (isMaxedOut()) {
            onComplete.accept(sPlayer);
        }
    }


    public void tick() {
        if (initialized()) {
            return;
        }
        if (getLevel() instanceof ServerWorld) {
            getCurrentVault().flatMap(this::calcMaxProgress).ifPresent(maxProgress -> {
                this.maxProgress = maxProgress.intValue();
                sendUpdates();
            });
        }
    }

    private Optional<VaultRaid> getCurrentVault() {
        ServerWorld sWorld = (ServerWorld) getLevel();
        return Optional.ofNullable(VaultRaidData.get(sWorld).getAt(sWorld, getBlockPos()));
    }

    protected float getMaxProgressMultiplier(UUID playerUUID) {
        if (getLevel() instanceof ServerWorld) {
            ServerWorld sWorld = (ServerWorld) getLevel();
            int favour = PlayerFavourData.get(sWorld).getFavour(playerUUID, getAssociatedVaultGod());
            if (favour < 0) {
                return 1.0F + 0.2F * Math.abs(favour) / 6.0F;
            }
            return 1.0F - 0.75F * Math.min(favour, 8.0F) / 8.0F;
        }
        return 1.0F;
    }


    public CompoundNBT save(CompoundNBT nbt) {
        nbt.putInt("CurrentProgress", this.currentProgress);
        nbt.putInt("CalculatedMaxProgress", this.maxProgress);
        return super.save(nbt);
    }


    public void load(BlockState state, CompoundNBT nbt) {
        this.currentProgress = nbt.getInt("CurrentProgress");
        this.maxProgress = nbt.contains("CalculatedMaxProgress") ? nbt.getInt("CalculatedMaxProgress") : -1;
        super.load(state, nbt);
    }


    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = super.getUpdateTag();
        save(nbt);
        return nbt;
    }


    public void handleUpdateTag(BlockState state, CompoundNBT nbt) {
        load(state, nbt);
    }


    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.worldPosition, 1, getUpdateTag());
    }


    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT nbt = pkt.getTag();
        handleUpdateTag(getBlockState(), nbt);
    }

    public void sendUpdates() {
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), 3);
            this.level.updateNeighborsAt(this.worldPosition, getBlockState().getBlock());
            setChanged();
        }
    }

    public abstract ITextComponent getRequirementName();

    public abstract PlayerFavourData.VaultGodType getAssociatedVaultGod();

    public abstract ITextComponent getRequirementUnit();

    public abstract Color getFillColor();

    protected abstract Optional<Integer> calcMaxProgress(VaultRaid paramVaultRaid);
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\base\FillableAltarTileEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */