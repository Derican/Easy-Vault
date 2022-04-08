package iskallia.vault.world.vault.logic.objective.architect.modifier;

import com.google.gson.annotations.Expose;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.architect.ArchitectObjective;
import iskallia.vault.world.vault.logic.objective.architect.processor.BlockPlacementPostProcessor;
import iskallia.vault.world.vault.logic.objective.architect.processor.VaultPieceProcessor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nullable;

public class BlockPlacementModifier extends VoteModifier {
    @Expose
    private final String block;
    @Expose
    private final int blocksPerSpawn;

    public BlockPlacementModifier(String name, String description, int voteLockDurationChangeSeconds, Block block, int blocksPerSpawn) {
        super(name, description, voteLockDurationChangeSeconds);
        this.block = block.getRegistryName().toString();
        this.blocksPerSpawn = blocksPerSpawn;
    }

    public BlockState getBlock() {
        return ((Block) Registry.BLOCK.getOptional(new ResourceLocation(this.block)).orElse(Blocks.AIR)).defaultBlockState();
    }

    public int getBlocksPerSpawn() {
        return this.blocksPerSpawn;
    }


    @Nullable
    public VaultPieceProcessor getPostProcessor(ArchitectObjective objective, VaultRaid vault) {
        return (VaultPieceProcessor) new BlockPlacementPostProcessor(getBlock(), getBlocksPerSpawn());
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\objective\architect\modifier\BlockPlacementModifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */