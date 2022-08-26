package iskallia.vault.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import iskallia.vault.block.entity.VaultDoorTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.item.ItemGroup;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.state.properties.DoorHingeSide;
import net.minecraft.state.Property;
import net.minecraft.util.Direction;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.DoorBlock;

public class UnknownVaultDoorBlock extends DoorBlock {
    public UnknownVaultDoorBlock() {
        super(AbstractBlock.Properties.of(Material.METAL, MaterialColor.DIAMOND).strength(-1.0f, 3600000.0f).sound(SoundType.METAL).noOcclusion());
        this.registerDefaultState(this.getStateDefinition().any().setValue((Property) UnknownVaultDoorBlock.FACING, (Comparable) Direction.NORTH).setValue((Property) UnknownVaultDoorBlock.OPEN, (Comparable) Boolean.FALSE).setValue((Property) UnknownVaultDoorBlock.HINGE, (Comparable) DoorHingeSide.LEFT).setValue((Property) UnknownVaultDoorBlock.POWERED, (Comparable) Boolean.FALSE).setValue((Property) UnknownVaultDoorBlock.HALF, (Comparable) DoubleBlockHalf.LOWER));
    }

    public void fillItemCategory(final ItemGroup group, final NonNullList<ItemStack> items) {
    }

    public boolean hasTileEntity(final BlockState state) {
        return true;
    }

    public TileEntity createTileEntity(final BlockState state, final IBlockReader world) {
        return new VaultDoorTileEntity();
    }

    public void onPlace(final BlockState state, final World world, final BlockPos pos, final BlockState oldState, final boolean isMoving) {
        super.onPlace(state, world, pos, oldState, isMoving);
    }
}
