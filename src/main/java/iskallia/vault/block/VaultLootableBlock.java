// 
// Decompiled by Procyon v0.6.0
// 

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

public class VaultLootableBlock extends Block
{
    private final Type type;
    
    public VaultLootableBlock(final Type type) {
        super(AbstractBlock.Properties.copy((AbstractBlock)Blocks.BLACK_WOOL));
        this.type = type;
    }
    
    public Type getType() {
        return this.type;
    }
    
    public boolean hasTileEntity(final BlockState state) {
        return true;
    }
    
    public TileEntity createTileEntity(final BlockState state, final IBlockReader world) {
        return new VaultLootableTileEntity().setType(this.getType());
    }
    
    public enum Type
    {
        ORE((Supplier<VaultLootableTileEntity.Generator>)VaultLootableTileEntity.VaultOreBlockGenerator::new), 
        RICHITY(() -> ModConfigs.VAULT_LOOTABLES.RICHITY::get), 
        RESOURCE((Supplier<VaultLootableTileEntity.Generator>)VaultLootableTileEntity.VaultResourceBlockGenerator::new), 
        MISC(() -> ModConfigs.VAULT_LOOTABLES.MISC::get), 
        VAULT_CHEST(() -> ModConfigs.VAULT_LOOTABLES.VAULT_CHEST::get), 
        VAULT_TREASURE(() -> ModConfigs.VAULT_LOOTABLES.VAULT_TREASURE::get), 
        VAULT_OBJECTIVE((Supplier<VaultLootableTileEntity.Generator>)VaultObjective::getObjectiveBlock);
        
        private final Supplier<VaultLootableTileEntity.Generator> generator;
        
        private Type(final Supplier<VaultLootableTileEntity.Generator> generator) {
            this.generator = generator;
        }
        
        public GeneratedBlockState generateBlock(final ServerWorld world, final BlockPos pos, final Random random, final UUID playerUUID) {
            final VaultLootableTileEntity.Generator gen = this.generator.get();
            final BlockState generated = gen.generate(world, pos, random, this.name(), playerUUID);
            if (gen instanceof VaultLootableTileEntity.ExtendedGenerator) {
                return new GeneratedBlockState(generated, ((VaultLootableTileEntity.ExtendedGenerator)gen)::postProcess);
            }
            return new GeneratedBlockState(generated);
        }
    }
    
    public static class GeneratedBlockState
    {
        private final BlockState state;
        private final BiConsumer<ServerWorld, BlockPos> postProcess;
        
        public GeneratedBlockState(final BlockState state) {
            this(state, (sWorld, pos) -> {});
        }
        
        public GeneratedBlockState(final BlockState state, final BiConsumer<ServerWorld, BlockPos> postProcess) {
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
