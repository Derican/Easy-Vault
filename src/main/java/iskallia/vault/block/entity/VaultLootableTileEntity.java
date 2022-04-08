package iskallia.vault.block.entity;

import iskallia.vault.block.VaultLootableBlock;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import java.util.Random;
import java.util.UUID;


public class VaultLootableTileEntity
        extends TileEntity
        implements ITickableTileEntity {
    private VaultLootableBlock.Type type;

    public VaultLootableTileEntity() {
        super(ModBlocks.VAULT_LOOTABLE_TILE_ENTITY);
    }

    public VaultLootableTileEntity setType(VaultLootableBlock.Type type) {
        this.type = type;
        return this;
    }


    public void tick() {
        if (this.type == null || getLevel() == null || getLevel().isClientSide())
            return;
        ServerWorld world = (ServerWorld) getLevel();
        BlockState state = world.getBlockState(getBlockPos());

        if (state.getBlock() instanceof VaultLootableBlock) {
            VaultRaid vault = VaultRaidData.get(world).getAt(world, getBlockPos());
            if (vault == null) {
                return;
            }


            VaultLootableBlock.GeneratedBlockState placingState = vault.getProperties().getBase(VaultRaid.HOST).map(hostUUID -> this.type.generateBlock(world, getBlockPos(), world.getRandom(), hostUUID)).orElse(new VaultLootableBlock.GeneratedBlockState(Blocks.AIR.defaultBlockState()));
            if (world.setBlock(getBlockPos(), placingState.getState(), 19)) {
                placingState.getPostProcessor().accept(world, getBlockPos());
            }
        }
    }


    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);

        if (nbt.contains("Type", 3)) {
            this.type = VaultLootableBlock.Type.values()[nbt.getInt("Type")];
        }
    }


    public CompoundNBT save(CompoundNBT compound) {
        CompoundNBT nbt = super.save(compound);

        if (this.type != null) {
            nbt.putInt("Type", this.type.ordinal());
        }

        return nbt;
    }


    public static interface Generator {
        @Nonnull
        BlockState generate(ServerWorld param1ServerWorld, BlockPos param1BlockPos, Random param1Random, String param1String, UUID param1UUID);
    }

    public static interface ExtendedGenerator
            extends Generator {
        void postProcess(ServerWorld param1ServerWorld, BlockPos param1BlockPos);
    }

    public static class VaultResourceBlockGenerator
            implements Generator {
        @Nonnull
        public BlockState generate(ServerWorld world, BlockPos pos, Random random, String poolName, UUID playerUUID) {
            VaultRaid vault = VaultRaidData.get(world).getAt(world, pos);
            if (vault == null) {
                return Blocks.AIR.defaultBlockState();
            }
            VaultObjective objective = vault.getActiveObjective(RaidChallengeObjective.class).orElse(null);
            if (objective != null) {
                return getRandomNonMinecraftBlock(world, pos, random, poolName, playerUUID);
            }
            return ModConfigs.VAULT_LOOTABLES.RESOURCE.get(world, pos, random, poolName, playerUUID);
        }


        private BlockState getRandomNonMinecraftBlock(ServerWorld world, BlockPos pos, Random random, String poolName, UUID playerUUID) {
            while (true) {
                BlockState generatedBlock = ModConfigs.VAULT_LOOTABLES.RESOURCE.get(world, pos, random, poolName, playerUUID);
                if (!generatedBlock.getBlock().getRegistryName().getNamespace().equals("minecraft"))
                    return generatedBlock;
            }
        }
    }

    public static class VaultOreBlockGenerator
            implements Generator {
        @Nonnull
        public BlockState generate(ServerWorld world, BlockPos pos, Random random, String poolName, UUID playerUUID) {
            VaultRaid vault = VaultRaidData.get(world).getAt(world, pos);
            if (vault == null) {
                return Blocks.AIR.defaultBlockState();
            }
            VaultObjective objective = vault.getActiveObjective(TroveObjective.class).orElse(null);
            if (objective != null)
                return getRandomVaultOreBlock(world, pos, random, poolName, playerUUID);
            if (!vault.getGenerator().getPiecesAt(pos, VaultTreasure.class).isEmpty()) {
                return getRandomVaultOreBlock(world, pos, random, poolName, playerUUID);
            }
            return ModConfigs.VAULT_LOOTABLES.ORE.get(world, pos, random, poolName, playerUUID);
        }


        private BlockState getRandomVaultOreBlock(ServerWorld world, BlockPos pos, Random random, String poolName, UUID playerUUID) {
            while (true) {
                BlockState generatedBlock = ModConfigs.VAULT_LOOTABLES.ORE.get(world, pos, random, poolName, playerUUID);
                if (generatedBlock.getBlock() instanceof iskallia.vault.block.VaultOreBlock)
                    return generatedBlock;
            }
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\entity\VaultLootableTileEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */