// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.logic;

import iskallia.vault.network.message.SandEventUpdateMessage;
import java.util.Iterator;
import net.minecraft.nbt.INBT;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import iskallia.vault.entity.VaultSandEntity;
import net.minecraft.world.IBlockReader;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraft.util.text.ITextComponent;
import iskallia.vault.network.message.SandEventContributorMessage;
import iskallia.vault.init.ModNetwork;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.entity.player.PlayerEntity;
import iskallia.vault.init.ModConfigs;
import java.util.function.Supplier;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;
import iskallia.vault.world.vault.player.VaultPlayer;
import iskallia.vault.world.vault.VaultRaid;
import java.util.HashMap;
import java.util.UUID;
import java.util.Map;
import java.util.Random;
import iskallia.vault.world.vault.logic.task.IVaultTask;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class VaultSandEvent implements INBTSerializable<CompoundNBT>, IVaultTask
{
    private static final Random rand;
    private final Map<UUID, SandProgress> playerProgress;
    
    public VaultSandEvent() {
        this.playerProgress = new HashMap<UUID, SandProgress>();
    }
    
    public void execute(final VaultRaid vault, final VaultPlayer player, final ServerWorld world) {
        if (world.getGameTime() % 10L == 0L) {
            player.runIfPresent(world.getServer(), this::sendUpdate);
        }
    }
    
    public void addSand(final ServerPlayerEntity player, final String contributor, final TextFormatting contributorColor, final int amount, final Supplier<Boolean> onSandFilled) {
        final SandProgress progress = this.playerProgress.computeIfAbsent(player.getUUID(), uuid -> new SandProgress());
        final int requiredTotal = ModConfigs.SAND_EVENT.getRedemptionsRequiredPerSand((PlayerEntity)player);
        final int current = (int)(requiredTotal * progress.fillPercent);
        final int newAmount = current + amount;
        if (newAmount > requiredTotal) {
            if (onSandFilled.get()) {
                progress.fillPercent = 0.0f;
                progress.spawnedSands++;
                player.getLevel().playSound((PlayerEntity)null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_LEVELUP, SoundCategory.PLAYERS, 0.6f, 1.0f);
            }
        }
        else {
            progress.fillPercent = Math.min(newAmount / (float)requiredTotal, 1.0f);
        }
        this.sendUpdate(player, progress);
        final IFormattableTextComponent display = new StringTextComponent(contributor).withStyle(contributorColor);
        ModNetwork.CHANNEL.sendTo((Object)new SandEventContributorMessage((ITextComponent)display), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
    
    public void pickupSand(final ServerPlayerEntity player) {
        final SandProgress progress = this.playerProgress.computeIfAbsent(player.getUUID(), uuid -> new SandProgress());
        progress.collectedSands++;
        this.sendUpdate(player, progress);
    }
    
    private void sendUpdate(final ServerPlayerEntity sPlayer) {
        final SandProgress progress = this.playerProgress.computeIfAbsent(sPlayer.getUUID(), uuid -> new SandProgress());
        this.sendUpdate(sPlayer, progress);
    }
    
    private void sendUpdate(final ServerPlayerEntity sPlayer, final SandProgress progress) {
        ModNetwork.CHANNEL.sendTo((Object)progress.makeUpdatePacket(), sPlayer.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
    
    public static boolean spawnSand(final ServerWorld world, final ServerPlayerEntity player) {
        int attempts = 100000;
        final float min = ModConfigs.SAND_EVENT.getMinDistance();
        final float max = ModConfigs.SAND_EVENT.getMaxDistance();
        final BlockPos offset = player.blockPosition();
        while (attempts > 0) {
            final int x = Math.round(min + VaultSandEvent.rand.nextFloat() * (max - min)) * (VaultSandEvent.rand.nextBoolean() ? 1 : -1);
            final int y = Math.round(VaultSandEvent.rand.nextFloat() * 30.0f) * (VaultSandEvent.rand.nextBoolean() ? 1 : -1);
            final int z = Math.round(min + VaultSandEvent.rand.nextFloat() * (max - min)) * (VaultSandEvent.rand.nextBoolean() ? 1 : -1);
            final BlockPos pos = offset.offset(x, y, z);
            if (world.isAreaLoaded(pos, 1)) {
                final BlockState state = world.getBlockState(pos);
                if (state.isAir((IBlockReader)world, pos)) {
                    world.addFreshEntity((Entity)VaultSandEntity.create((World)world, pos));
                    return true;
                }
            }
            --attempts;
        }
        return false;
    }
    
    public CompoundNBT serializeNBT() {
        final CompoundNBT nbt = new CompoundNBT();
        this.playerProgress.forEach((uuid, progress) -> nbt.put(uuid.toString(), (INBT)progress.serialize()));
        return nbt;
    }
    
    public void deserializeNBT(final CompoundNBT nbt) {
        this.playerProgress.clear();
        for (final String key : nbt.getAllKeys()) {
            UUID playerUUID;
            try {
                playerUUID = UUID.fromString(key);
            }
            catch (final IllegalArgumentException exc) {
                continue;
            }
            final CompoundNBT tag = nbt.getCompound(key);
            this.playerProgress.put(playerUUID, SandProgress.deserialize(tag));
        }
    }
    
    static {
        rand = new Random();
    }
    
    private static class SandProgress
    {
        private float fillPercent;
        private int spawnedSands;
        private int collectedSands;
        
        private SandProgress() {
            this.fillPercent = 0.0f;
            this.spawnedSands = 0;
            this.collectedSands = 0;
        }
        
        private CompoundNBT serialize() {
            final CompoundNBT nbt = new CompoundNBT();
            nbt.putFloat("fillPercent", this.fillPercent);
            nbt.putInt("spawnedSands", this.spawnedSands);
            nbt.putInt("collectedSands", this.collectedSands);
            return nbt;
        }
        
        private static SandProgress deserialize(final CompoundNBT nbt) {
            final SandProgress progress = new SandProgress();
            progress.fillPercent = nbt.getFloat("fillPercent");
            progress.spawnedSands = nbt.getInt("spawnedSands");
            progress.collectedSands = nbt.getInt("collectedSands");
            return progress;
        }
        
        private SandEventUpdateMessage makeUpdatePacket() {
            return new SandEventUpdateMessage(this.fillPercent, this.spawnedSands, this.collectedSands);
        }
    }
}
