// 
// Decompiled by Procyon v0.6.0
// 

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

public class TrophyBlock extends LootStatueBlock
{
    public static final VoxelShape SHAPE;
    
    public TrophyBlock() {
        super(StatueType.TROPHY, AbstractBlock.Properties.of(Material.METAL, MaterialColor.GOLD).strength(5.0f, 3600000.0f).noOcclusion().noCollission());
    }
    
    @Override
    public VoxelShape getShape(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final ISelectionContext context) {
        return TrophyBlock.SHAPE;
    }
    
    @Override
    protected void setStatueTileData(final LootStatueTileEntity lootStatue, final CompoundNBT blockEntityTag) {
        super.setStatueTileData(lootStatue, blockEntityTag);
        if (lootStatue instanceof TrophyStatueTileEntity) {
            final TrophyStatueTileEntity trophyStatue = (TrophyStatueTileEntity)lootStatue;
            final WeekKey week = WeekKey.deserialize(blockEntityTag.getCompound("trophyWeek"));
            final PlayerVaultStatsData.PlayerRecordEntry recordEntry = PlayerVaultStatsData.PlayerRecordEntry.deserialize(blockEntityTag.getCompound("recordEntry"));
            trophyStatue.setWeek(week);
            trophyStatue.setRecordEntry(recordEntry);
        }
    }
    
    @Nullable
    @Override
    public TileEntity createTileEntity(final BlockState state, final IBlockReader world) {
        return ModBlocks.TROPHY_STATUE_TILE_ENTITY.create();
    }
    
    static {
        SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0);
    }
}
