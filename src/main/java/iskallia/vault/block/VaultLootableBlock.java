package iskallia.vault.block;

import iskallia.vault.block.entity.VaultLootableTileEntity;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.world.vault.logic.objective.VaultObjective;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Supplier;


public class VaultLootableBlock
        extends Block {
    private final Type type;

    public VaultLootableBlock(Type type) {
        super(AbstractBlock.Properties.copy((AbstractBlock) Blocks.BLACK_WOOL));
        this.type = type;
    }

    public Type getType() {
        return this.type;
    }


    public boolean hasTileEntity(BlockState state) {
        return true;
    }


    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return (TileEntity) (new VaultLootableTileEntity()).setType(getType());
    }

    public enum Type {
        ORE( iskallia.vault.block.entity.VaultLootableTileEntity.VaultOreBlockGenerator::new),
        RICHITY( (() -> ModConfigs.VAULT_LOOTABLES.RICHITY::get)),
        RESOURCE( iskallia.vault.block.entity.VaultLootableTileEntity.VaultResourceBlockGenerator::new),
        MISC( (() -> ModConfigs.VAULT_LOOTABLES.MISC::get)),
        VAULT_CHEST( (() -> ModConfigs.VAULT_LOOTABLES.VAULT_CHEST::get)),
        VAULT_TREASURE( (() -> ModConfigs.VAULT_LOOTABLES.VAULT_TREASURE::get)),
        VAULT_OBJECTIVE( VaultObjective::getObjectiveBlock);

        private final Supplier<VaultLootableTileEntity.Generator> generator;

        Type(Supplier<VaultLootableTileEntity.Generator> generator) {
            this.generator = generator;
        }

        public VaultLootableBlock.GeneratedBlockState generateBlock(ServerWorld world, BlockPos pos, Random random, UUID playerUUID) {
            VaultLootableTileEntity.Generator gen = this.generator.get();
            BlockState generated = gen.generate(world, pos, random, name(), playerUUID);
            if (gen instanceof VaultLootableTileEntity.ExtendedGenerator) {
                return new VaultLootableBlock.GeneratedBlockState(generated, ((VaultLootableTileEntity.ExtendedGenerator) gen)::postProcess);
            }
            return new VaultLootableBlock.GeneratedBlockState(generated);
        }
    }

    public static class GeneratedBlockState {
        private final BlockState state;
        private final BiConsumer<ServerWorld, BlockPos> postProcess;

        public GeneratedBlockState(BlockState state) {
            this(state, (sWorld, pos) -> {

            });
        }

        public GeneratedBlockState(BlockState state, BiConsumer<ServerWorld, BlockPos> postProcess) {
            this.state = state;
            this.postProcess = postProcess;
        }

        public BlockState getState() {
            return this.state;
        }

        public BiConsumer<ServerWorld, BlockPos> getPostProcessor() {
            return this.postProcess;
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\VaultLootableBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */