// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.block.entity;

import iskallia.vault.block.VaultLootableBlock;
import iskallia.vault.block.VaultOreBlock;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.gen.piece.VaultTreasure;
import iskallia.vault.world.vault.logic.objective.TroveObjective;
import iskallia.vault.world.vault.logic.objective.VaultObjective;
import iskallia.vault.world.vault.logic.objective.raid.RaidChallengeObjective;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import java.util.Random;
import java.util.UUID;

public class VaultLootableTileEntity extends TileEntity implements ITickableTileEntity
{
    private VaultLootableBlock.Type type;
    
    public VaultLootableTileEntity() {
        super((TileEntityType)ModBlocks.VAULT_LOOTABLE_TILE_ENTITY);
    }
    
    public VaultLootableTileEntity setType(final VaultLootableBlock.Type type) {
        this.type = type;
        return this;
    }
    
    public void tick() {
        if (this.type == null || this.getLevel() == null || this.getLevel().isClientSide()) {
            return;
        }
        final ServerWorld world = (ServerWorld)this.getLevel();
        final BlockState state = world.getBlockState(this.getBlockPos());
        if (state.getBlock() instanceof VaultLootableBlock) {
            final VaultRaid vault = VaultRaidData.get(world).getAt(world, this.getBlockPos());
            if (vault == null) {
                return;
            }
            final VaultLootableBlock.GeneratedBlockState placingState = vault.getProperties().getBase(VaultRaid.HOST).map(hostUUID -> this.type.generateBlock(world, this.getBlockPos(), world.getRandom(), hostUUID)).orElse(new VaultLootableBlock.GeneratedBlockState(Blocks.AIR.defaultBlockState()));
            if (world.setBlock(this.getBlockPos(), placingState.getState(), 19)) {
                placingState.getPostProcessor().accept(world, this.getBlockPos());
            }
        }
    }
    
    public void load(final BlockState state, final CompoundNBT nbt) {
        super.load(state, nbt);
        if (nbt.contains("Type", 3)) {
            this.type = VaultLootableBlock.Type.values()[nbt.getInt("Type")];
        }
    }
    
    public CompoundNBT save(final CompoundNBT compound) {
        final CompoundNBT nbt = super.save(compound);
        if (this.type != null) {
            nbt.putInt("Type", this.type.ordinal());
        }
        return nbt;
    }
    
    public static class VaultResourceBlockGenerator implements Generator
    {
        @Nonnull
        @Override
        public BlockState generate(final ServerWorld world, final BlockPos pos, final Random random, final String poolName, final UUID playerUUID) {
            final VaultRaid vault = VaultRaidData.get(world).getAt(world, pos);
            if (vault == null) {
                return Blocks.AIR.defaultBlockState();
            }
            final VaultObjective objective = vault.getActiveObjective(RaidChallengeObjective.class).orElse(null);
            if (objective != null) {
                return this.getRandomNonMinecraftBlock(world, pos, random, poolName, playerUUID);
            }
            return ModConfigs.VAULT_LOOTABLES.RESOURCE.get(world, pos, random, poolName, playerUUID);
        }
        
        private BlockState getRandomNonMinecraftBlock(final ServerWorld world, final BlockPos pos, final Random random, final String poolName, final UUID playerUUID) {
            BlockState generatedBlock;
            do {
                generatedBlock = ModConfigs.VAULT_LOOTABLES.RESOURCE.get(world, pos, random, poolName, playerUUID);
            } while (generatedBlock.getBlock().getRegistryName().getNamespace().equals("minecraft"));
            return generatedBlock;
        }
    }
    
    public static class VaultOreBlockGenerator implements Generator
    {
        @Nonnull
        @Override
        public BlockState generate(final ServerWorld world, final BlockPos pos, final Random random, final String poolName, final UUID playerUUID) {
            final VaultRaid vault = VaultRaidData.get(world).getAt(world, pos);
            if (vault == null) {
                return Blocks.AIR.defaultBlockState();
            }
            final VaultObjective objective = vault.getActiveObjective(TroveObjective.class).orElse(null);
            if (objective != null) {
                return this.getRandomVaultOreBlock(world, pos, random, poolName, playerUUID);
            }
            if (!vault.getGenerator().getPiecesAt(pos, VaultTreasure.class).isEmpty()) {
                return this.getRandomVaultOreBlock(world, pos, random, poolName, playerUUID);
            }
            return ModConfigs.VAULT_LOOTABLES.ORE.get(world, pos, random, poolName, playerUUID);
        }
        
        private BlockState getRandomVaultOreBlock(final ServerWorld world, final BlockPos pos, final Random random, final String poolName, final UUID playerUUID) {
            BlockState generatedBlock;
            do {
                generatedBlock = ModConfigs.VAULT_LOOTABLES.ORE.get(world, pos, random, poolName, playerUUID);
            } while (!(generatedBlock.getBlock() instanceof VaultOreBlock));
            return generatedBlock;
        }
    }
    
    public interface Generator
    {
        @Nonnull
        BlockState generate(final ServerWorld p0, final BlockPos p1, final Random p2, final String p3, final UUID p4);
    }
    
    public interface ExtendedGenerator extends Generator
    {
        void postProcess(final ServerWorld p0, final BlockPos p1);
    }
}
