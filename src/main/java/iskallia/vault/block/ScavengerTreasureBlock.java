package iskallia.vault.block;

import iskallia.vault.config.ScavengerHuntConfig;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModSounds;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.ScavengerHuntObjective;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ScavengerTreasureBlock extends ContainerBlock {
    private static final VoxelShape BOX = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 5.0D, 16.0D);

    public ScavengerTreasureBlock() {
        super(AbstractBlock.Properties.of(Material.METAL, MaterialColor.GOLD)
                .harvestLevel(0)
                .harvestTool(ToolType.PICKAXE)
                .strength(10.0F, 1.0F)
                .sound((SoundType) ModSounds.VAULT_GEM));
    }


    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return BOX;
    }


    @Nullable
    public TileEntity newBlockEntity(IBlockReader worldIn) {
        return ModBlocks.SCAVENGER_TREASURE_TILE_ENTITY.create();
    }

    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }


    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        ServerWorld world = builder.getLevel();
        BlockPos pos = new BlockPos((Vector3d) builder.getOptionalParameter(LootParameters.ORIGIN));
        VaultRaid vault = VaultRaidData.get(world).getAt(world, pos);
        if (vault == null) {
            return super.getDrops(state, builder);
        }
        Optional<ScavengerHuntObjective> objectiveOpt = vault.getActiveObjective(ScavengerHuntObjective.class);
        if (!objectiveOpt.isPresent()) {
            return super.getDrops(state, builder);
        }
        ScavengerHuntObjective objective = objectiveOpt.get();

        List<ItemStack> drops = new ArrayList<>(super.getDrops(state, builder));
        ModConfigs.SCAVENGER_HUNT.generateTreasureLoot(objective.getGenerationDropFilter()).stream()
                .map(ScavengerHuntConfig.ItemEntry::createItemStack)
                .filter(stack -> !stack.isEmpty())
                .peek(stack -> vault.getProperties().getBase(VaultRaid.IDENTIFIER).ifPresent((UUID) -> {
                }))


                .forEach(drops::add);
        return drops;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\ScavengerTreasureBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */