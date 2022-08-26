package iskallia.vault.block;

import iskallia.vault.init.ModBlocks;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
    public static final List<VaultDoorBlock> VAULT_DOORS;
    protected Item keyItem;

    public VaultDoorBlock(final Item keyItem) {
        super(AbstractBlock.Properties.of(Material.METAL, MaterialColor.DIAMOND).strength(-1.0f, 3600000.0f).sound(SoundType.METAL).noOcclusion());
        this.registerDefaultState((((((this.getStateDefinition().any()).setValue(VaultDoorBlock.FACING, Direction.NORTH)).setValue(VaultDoorBlock.OPEN, Boolean.FALSE)).setValue(VaultDoorBlock.HINGE, DoorHingeSide.LEFT)).setValue(VaultDoorBlock.POWERED, Boolean.FALSE)).setValue(VaultDoorBlock.HALF, DoubleBlockHalf.LOWER));
        this.keyItem = keyItem;
        VaultDoorBlock.VAULT_DOORS.add(this);
    }

    public Item getKeyItem() {
        return this.keyItem;
    }

    public PushReaction getPistonPushReaction(final BlockState state) {
        return PushReaction.BLOCK;
    }

    public void neighborChanged(final BlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos, final boolean isMoving) {
    }

    public boolean hasTileEntity(final BlockState state) {
        return true;
    }

    public TileEntity createTileEntity(final BlockState state, final IBlockReader world) {
        return ModBlocks.VAULT_DOOR_TILE_ENTITY.create();
    }

    public ActionResultType use(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockRayTraceResult hit) {
        final ItemStack heldStack = player.getItemInHand(hand);
        final Boolean isOpen = state.getValue(VaultDoorBlock.OPEN);
        if (!isOpen && heldStack.getItem() == this.getKeyItem()) {
            heldStack.shrink(1);
            this.setOpen(world, state, pos, true);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.SUCCESS;
    }

    static {
        VAULT_DOORS = new ArrayList<VaultDoorBlock>();
    }
}
