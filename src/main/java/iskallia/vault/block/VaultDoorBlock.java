package iskallia.vault.block;

import iskallia.vault.init.ModBlocks;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.Property;
import net.minecraft.state.properties.DoorHingeSide;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class VaultDoorBlock extends DoorBlock {
    public static final List<VaultDoorBlock> VAULT_DOORS = new ArrayList<>();

    protected Item keyItem;

    public VaultDoorBlock(Item keyItem) {
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
        this.keyItem = keyItem;
        VAULT_DOORS.add(this);
    }

    public Item getKeyItem() {
        return this.keyItem;
    }


    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }


    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
    }


    public boolean hasTileEntity(BlockState state) {
        return true;
    }


    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModBlocks.VAULT_DOOR_TILE_ENTITY.create();
    }


    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        ItemStack heldStack = player.getItemInHand(hand);
        Boolean isOpen = (Boolean) state.getValue((Property) OPEN);

        if (!isOpen.booleanValue() && heldStack.getItem() == getKeyItem()) {
            heldStack.shrink(1);
            setOpen(world, state, pos, true);
            return ActionResultType.SUCCESS;
        }

        return ActionResultType.SUCCESS;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\VaultDoorBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */