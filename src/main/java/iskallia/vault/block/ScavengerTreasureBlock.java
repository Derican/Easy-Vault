package iskallia.vault.block;

import iskallia.vault.config.ScavengerHuntConfig;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModSounds;
import iskallia.vault.item.BasicScavengerItem;
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
    private static final VoxelShape BOX;

    public ScavengerTreasureBlock() {
        super(AbstractBlock.Properties.of(Material.METAL, MaterialColor.GOLD).harvestLevel(0).harvestTool(ToolType.PICKAXE).strength(10.0f, 1.0f).sound((SoundType) ModSounds.VAULT_GEM));
    }

    public VoxelShape getShape(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final ISelectionContext context) {
        return ScavengerTreasureBlock.BOX;
    }

    @Nullable
    public TileEntity newBlockEntity(final IBlockReader worldIn) {
        return ModBlocks.SCAVENGER_TREASURE_TILE_ENTITY.create();
    }

    public BlockRenderType getRenderShape(final BlockState state) {
        return BlockRenderType.MODEL;
    }

    public List<ItemStack> getDrops(final BlockState state, final LootContext.Builder builder) {
        final ServerWorld world = builder.getLevel();
        final BlockPos pos = new BlockPos((Vector3d) builder.getOptionalParameter(LootParameters.ORIGIN));
        final VaultRaid vault = VaultRaidData.get(world).getAt(world, pos);
        if (vault == null) {
            return super.getDrops(state, builder);
        }
        final Optional<ScavengerHuntObjective> objectiveOpt = vault.getActiveObjective(ScavengerHuntObjective.class);
        if (!objectiveOpt.isPresent()) {
            return super.getDrops(state, builder);
        }
        final ScavengerHuntObjective objective = objectiveOpt.get();
        final List<ItemStack> drops = new ArrayList<ItemStack>(super.getDrops(state, builder));
        ModConfigs.SCAVENGER_HUNT.generateTreasureLoot(objective.getGenerationDropFilter()).stream().map(ScavengerHuntConfig.ItemEntry::createItemStack).filter(stack -> !stack.isEmpty()).peek(stack -> vault.getProperties().getBase(VaultRaid.IDENTIFIER).ifPresent(identifier -> BasicScavengerItem.setVaultIdentifier(stack, identifier))).forEach(drops::add);
        return drops;
    }

    static {
        BOX = Block.box(0.0, 0.0, 0.0, 16.0, 5.0, 16.0);
    }
}
