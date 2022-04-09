// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.block.entity;

import iskallia.vault.block.UnknownVaultDoorBlock;
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
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.UUID;

public class VaultDoorTileEntity extends TileEntity implements ITickableTileEntity
{
    public VaultDoorTileEntity() {
        super((TileEntityType)ModBlocks.VAULT_DOOR_TILE_ENTITY);
    }
    
    public void tick() {
        if (this.getLevel() == null || this.getLevel().isClientSide) {
            return;
        }
        final ServerWorld world = (ServerWorld)this.getLevel();
        final BlockState state = world.getBlockState(this.getBlockPos());
        if (state.getBlock() instanceof UnknownVaultDoorBlock && state.getValue(DoorBlock.HALF) == DoubleBlockHalf.LOWER) {
            final VaultRaid vault = VaultRaidData.get(world).getAt(world, this.getBlockPos());
            if (vault == null) {
                return;
            }
            final UUID hostUUID = vault.getProperties().getBase(VaultRaid.HOST).orElse(null);
            final BlockState newBlock = ModConfigs.VAULT_LOOTABLES.DOOR.get(world, this.getBlockPos(), world.getRandom(), "DOOR", hostUUID);
            if (newBlock.getBlock() instanceof DoorBlock) {
                final BlockState newState = ((((newBlock.setValue(DoorBlock.FACING, state.getValue(DoorBlock.FACING))).setValue(DoorBlock.OPEN, state.getValue(DoorBlock.OPEN))).setValue(DoorBlock.HINGE, state.getValue(DoorBlock.HINGE))).setValue(DoorBlock.POWERED, state.getValue(DoorBlock.POWERED))).setValue(DoorBlock.HALF, state.getValue(DoorBlock.HALF));
                world.setBlock(this.getBlockPos().above(), Blocks.AIR.defaultBlockState(), 27);
                world.setBlock(this.getBlockPos(), newState, 11);
                world.setBlock(this.getBlockPos().above(), newState.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER), 11);
            }
            boolean drilling = false;
            for (int i = 1; i < 32; ++i) {
                final BlockPos p = this.getBlockPos().relative((state.getValue(DoorBlock.FACING)).getOpposite(), i);
                if (this.getLevel().getBlockState(p).isAir() && this.getLevel().getBlockState(p.above()).isAir()) {
                    if (drilling) {
                        break;
                    }
                }
                else if (!drilling) {
                    drilling = true;
                }
                this.getLevel().setBlockAndUpdate(p, Blocks.AIR.defaultBlockState());
                this.getLevel().setBlockAndUpdate(p.above(), Blocks.AIR.defaultBlockState());
            }
        }
    }
}
