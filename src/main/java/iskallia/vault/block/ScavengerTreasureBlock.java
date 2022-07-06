package iskallia.vault.block;

import net.minecraft.block.Block;

import java.util.UUID;

import net.minecraft.world.server.ServerWorld;
import iskallia.vault.world.vault.logic.objective.TreasureHuntObjective;

import java.util.function.Consumer;

import iskallia.vault.item.BasicScavengerItem;
import iskallia.vault.world.vault.VaultRaid;

import java.util.function.Function;

import iskallia.vault.config.ScavengerHuntConfig;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.world.vault.logic.objective.ScavengerHuntObjective;

import java.util.Collection;
import java.util.ArrayList;

import iskallia.vault.world.data.VaultRaidData;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.item.ItemStack;

import java.util.List;

import net.minecraft.loot.LootContext;
import net.minecraft.block.BlockRenderType;

import javax.annotation.Nullable;

import iskallia.vault.init.ModBlocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import iskallia.vault.init.ModSounds;
import net.minecraftforge.common.ToolType;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.block.ContainerBlock;

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
        final List<ItemStack> drops = new ArrayList<ItemStack>(super.getDrops(state, builder));
        if (vault == null) {
            return drops;
        }
        vault.getActiveObjective(ScavengerHuntObjective.class).ifPresent(objective -> ModConfigs.SCAVENGER_HUNT.generateTreasureLoot(objective.getGenerationDropFilter()).stream().map(ScavengerHuntConfig.ItemEntry::createItemStack).filter(stack -> !stack.isEmpty()).peek(stack -> vault.getProperties().getBase(VaultRaid.IDENTIFIER).ifPresent(identifier -> BasicScavengerItem.setVaultIdentifier(stack, identifier))).forEach(drops::add));
        vault.getActiveObjective(TreasureHuntObjective.class).ifPresent(objective -> ModConfigs.TREASURE_HUNT.generateTreasureLoot(objective.getGenerationDropFilter()).stream().map(ScavengerHuntConfig.ItemEntry::createItemStack).filter(stack -> !stack.isEmpty()).peek(stack -> vault.getProperties().getBase(VaultRaid.IDENTIFIER).ifPresent(identifier -> BasicScavengerItem.setVaultIdentifier(stack, identifier))).forEach(drops::add));
        return drops;
    }

    static {
        BOX = Block.box(0.0, 0.0, 0.0, 16.0, 5.0, 16.0);
    }
}
