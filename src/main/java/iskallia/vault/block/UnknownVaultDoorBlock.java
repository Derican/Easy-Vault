package iskallia.vault.block;

import iskallia.vault.block.entity.VaultDoorTileEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.state.Property;
import net.minecraft.state.properties.DoorHingeSide;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class UnknownVaultDoorBlock extends DoorBlock {
    public UnknownVaultDoorBlock() {
        super(AbstractBlock.Properties.of(Material.METAL, MaterialColor.DIAMOND)
                .strength(-1.0F, 3600000.0F)
                .sound(SoundType.METAL)
                .noOcclusion());
        registerDefaultState((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) getStateDefinition().any())
                .setValue((Property) FACING, (Comparable) Direction.NORTH))
                .setValue((Property) OPEN, Boolean.FALSE))
                .setValue((Property) HINGE, (Comparable) DoorHingeSide.LEFT))
                .setValue((Property) POWERED, Boolean.FALSE))
                .setValue((Property) HALF, (Comparable) DoubleBlockHalf.LOWER));
    }


    public boolean hasTileEntity(BlockState state) {
        return true;
    }


    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return (TileEntity) new VaultDoorTileEntity();
    }


    public void onPlace(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, world, pos, oldState, isMoving);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\UnknownVaultDoorBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */