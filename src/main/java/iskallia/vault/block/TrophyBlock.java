package iskallia.vault.block;

import iskallia.vault.block.entity.LootStatueTileEntity;
import iskallia.vault.block.entity.TrophyStatueTileEntity;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.util.StatueType;
import iskallia.vault.util.WeekKey;
import iskallia.vault.world.data.PlayerVaultStatsData;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;


public class TrophyBlock
        extends LootStatueBlock {
    public static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);

    public TrophyBlock() {
        super(StatueType.TROPHY, AbstractBlock.Properties.of(Material.METAL, MaterialColor.GOLD)
                .strength(5.0F, 3600000.0F)
                .noOcclusion()
                .noCollission());
    }


    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }


    protected void setStatueTileData(LootStatueTileEntity lootStatue, CompoundNBT blockEntityTag) {
        super.setStatueTileData(lootStatue, blockEntityTag);

        if (lootStatue instanceof TrophyStatueTileEntity) {
            TrophyStatueTileEntity trophyStatue = (TrophyStatueTileEntity) lootStatue;

            WeekKey week = WeekKey.deserialize(blockEntityTag.getCompound("trophyWeek"));
            PlayerVaultStatsData.PlayerRecordEntry recordEntry = PlayerVaultStatsData.PlayerRecordEntry.deserialize(blockEntityTag.getCompound("recordEntry"));

            trophyStatue.setWeek(week);
            trophyStatue.setRecordEntry(recordEntry);
        }
    }


    @Nullable
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModBlocks.TROPHY_STATUE_TILE_ENTITY.create();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\TrophyBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */