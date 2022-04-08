package iskallia.vault.block;

import iskallia.vault.block.entity.VaultRuneTileEntity;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModItems;
import iskallia.vault.world.gen.PortalPlacer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class VaultRuneBlock extends Block {
    static {
        PORTAL_PLACER = new PortalPlacer((pos, random, facing) -> (BlockState) ModBlocks.FINAL_VAULT_PORTAL.defaultBlockState().setValue((Property) VaultPortalBlock.AXIS, (Comparable) facing.getAxis()), (pos, random, facing) -> {
            Block[] blocks = {Blocks.BLACKSTONE, Blocks.BLACKSTONE, Blocks.POLISHED_BLACKSTONE, Blocks.POLISHED_BLACKSTONE_BRICKS, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS};
            return blocks[random.nextInt(blocks.length)].defaultBlockState();
        });
    }


    public static final PortalPlacer PORTAL_PLACER;


    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty RUNE_PLACED = BooleanProperty.create("rune_placed");

    public VaultRuneBlock() {
        super(AbstractBlock.Properties.of(Material.STONE, MaterialColor.STONE)
                .strength(Float.MAX_VALUE, Float.MAX_VALUE)
                .noOcclusion());

        registerDefaultState((BlockState) ((BlockState) ((BlockState) this.stateDefinition.any())
                .setValue((Property) FACING, (Comparable) Direction.SOUTH))
                .setValue((Property) RUNE_PLACED, Boolean.valueOf(false)));
    }


    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return (BlockState) ((BlockState) defaultBlockState()
                .setValue((Property) FACING, (Comparable) context.getHorizontalDirection()))
                .setValue((Property) RUNE_PLACED, Boolean.valueOf(false));
    }


    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{(Property) FACING});
        builder.add(new Property[]{(Property) RUNE_PLACED});
    }


    public boolean hasTileEntity(BlockState state) {
        return true;
    }


    @Nullable
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModBlocks.VAULT_RUNE_TILE_ENTITY.create();
    }


    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (world.isClientSide) {
            return super.use(state, world, pos, player, hand, hit);
        }

        TileEntity tileEntity = world.getBlockEntity(pos);

        if (tileEntity instanceof VaultRuneTileEntity) {
            VaultRuneTileEntity vaultRuneTE = (VaultRuneTileEntity) tileEntity;
            String playerNick = player.getDisplayName().getString();

            if (vaultRuneTE.getBelongsTo().equals(playerNick)) {
                ItemStack heldStack = player.getItemInHand(hand);

                if (heldStack.getItem() == ModItems.VAULT_RUNE) {
                    BlockState blockState = world.getBlockState(pos);
                    world.setBlock(pos, (BlockState) blockState.setValue((Property) RUNE_PLACED, Boolean.valueOf(true)), 3);
                    heldStack.shrink(1);

                    world.playSound(null, pos

                            .getX(), pos
                            .getY(), pos
                            .getZ(), SoundEvents.END_PORTAL_FRAME_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);


                    createPortalFromRune(world, pos);
                }
            } else {
                StringTextComponent text = new StringTextComponent(vaultRuneTE.getBelongsTo() + " is responsible with this block.");
                text.setStyle(Style.EMPTY.withColor(Color.fromRgb(-26266)));
                player.displayClientMessage((ITextComponent) text, true);
            }
        }

        return ActionResultType.SUCCESS;
    }

    private void createPortalFromRune(World world, BlockPos pos) {
        Direction axis = null;

        for (int i = 1; i < 48; i++) {
            if (world.getBlockState(pos.offset(i, 0, 0)).getBlock() == ModBlocks.VAULT_RUNE_BLOCK || world
                    .getBlockState(pos.offset(-i, 0, 0)).getBlock() == ModBlocks.VAULT_RUNE_BLOCK) {
                axis = Direction.EAST;
                break;
            }
            if (world.getBlockState(pos.offset(0, 0, i)).getBlock() == ModBlocks.VAULT_RUNE_BLOCK || world
                    .getBlockState(pos.offset(0, 0, -i)).getBlock() == ModBlocks.VAULT_RUNE_BLOCK) {
                axis = Direction.SOUTH;

                break;
            }
        }
        if (axis == null)
            return;
        BlockPos corner = null;

        for (int j = -48; j <= 0; j++) {
            if (world.getBlockState(pos.relative(axis, j)).getBlock() == ModBlocks.VAULT_RUNE_BLOCK) {
                corner = pos.relative(axis, j);

                break;
            }
        }
        if (corner == null)
            return;
        List<BlockPos> positions = new ArrayList<>();

        for (int k = 0; k <= 48; k++) {
            if (world.getBlockState(corner.relative(axis, k)).getBlock() == ModBlocks.VAULT_RUNE_BLOCK) {
                positions.add(corner.relative(axis, k));
            }
        }

        for (BlockPos position : positions) {
            TileEntity tileEntity = world.getBlockEntity(pos);
            if (!(tileEntity instanceof VaultRuneTileEntity))
                continue;
            VaultRuneTileEntity vaultRuneTE = (VaultRuneTileEntity) tileEntity;
            BlockState state = world.getBlockState(position);

            if (!vaultRuneTE.getBelongsTo().isEmpty() && !((Boolean) state.getValue((Property) RUNE_PLACED)).booleanValue()) {
                return;
            }
        }

        BlockPos low = positions.get(0);
        BlockPos high = positions.get(positions.size() - 1);

        BlockPos portalPos = low.above().relative(axis, -1);

        int width = axis.getAxis().choose(high.getX(), high.getY(), high.getZ()) - axis.getAxis().choose(low.getX(), low.getY(), low.getZ()) + 3;

        PORTAL_PLACER.place((IWorld) world, portalPos, axis, width, 28);

        if (!world.isClientSide) {
            ServerWorld sWorld = (ServerWorld) world;
            sWorld.setWeatherParameters(0, 1000000, true, true);

            for (int m = 0; m < 10; m++) {
                BlockPos pos2 = pos.offset(world.random.nextInt(100) - 50, 0, world.random.nextInt(100) - 50);
                pos2 = world.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING, pos2);

                LightningBoltEntity lightningboltentity = (LightningBoltEntity) EntityType.LIGHTNING_BOLT.create((World) sWorld);
                lightningboltentity.moveTo(Vector3d.atBottomCenterOf((Vector3i) pos2));
                sWorld.addFreshEntity((Entity) lightningboltentity);
            }
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\VaultRuneBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */