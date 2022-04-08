package iskallia.vault.block.entity;

import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoorBlock;
import net.minecraft.state.Property;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.UUID;

public class VaultDoorTileEntity
        extends TileEntity
        implements ITickableTileEntity {
    public VaultDoorTileEntity() {
        super(ModBlocks.VAULT_DOOR_TILE_ENTITY);
    }


    public void tick() {
        if (getLevel() == null || (getLevel()).isClientSide)
            return;
        ServerWorld world = (ServerWorld) getLevel();

        BlockState state = world.getBlockState(getBlockPos());

        if (state.getBlock() instanceof iskallia.vault.block.UnknownVaultDoorBlock && state.getValue((Property) DoorBlock.HALF) == DoubleBlockHalf.LOWER) {
            VaultRaid vault = VaultRaidData.get(world).getAt(world, getBlockPos());
            if (vault == null)
                return;
            UUID hostUUID = vault.getProperties().getBase(VaultRaid.HOST).orElse(null);
            BlockState newBlock = ModConfigs.VAULT_LOOTABLES.DOOR.get(world, getBlockPos(), world.getRandom(), "DOOR", hostUUID);

            if (newBlock.getBlock() instanceof DoorBlock) {


                BlockState newState = (BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) newBlock.setValue((Property) DoorBlock.FACING, state.getValue((Property) DoorBlock.FACING))).setValue((Property) DoorBlock.OPEN, state.getValue((Property) DoorBlock.OPEN))).setValue((Property) DoorBlock.HINGE, state.getValue((Property) DoorBlock.HINGE))).setValue((Property) DoorBlock.POWERED, state.getValue((Property) DoorBlock.POWERED))).setValue((Property) DoorBlock.HALF, state.getValue((Property) DoorBlock.HALF));

                world.setBlock(getBlockPos().above(), Blocks.AIR.defaultBlockState(), 27);
                world.setBlock(getBlockPos(), newState, 11);
                world.setBlock(getBlockPos().above(), (BlockState) newState.setValue((Property) DoorBlock.HALF, (Comparable) DoubleBlockHalf.UPPER), 11);
            }

            boolean drilling = false;

            for (int i = 1; i < 32; i++) {
                BlockPos p = getBlockPos().relative(((Direction) state.getValue((Property) DoorBlock.FACING)).getOpposite(), i);

                if (getLevel().getBlockState(p).isAir() && getLevel().getBlockState(p.above()).isAir()) {
                    if (drilling)
                        break;
                } else if (!drilling) {
                    drilling = true;
                }


                getLevel().setBlockAndUpdate(p, Blocks.AIR.defaultBlockState());
                getLevel().setBlockAndUpdate(p.above(), Blocks.AIR.defaultBlockState());
            }
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\entity\VaultDoorTileEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */