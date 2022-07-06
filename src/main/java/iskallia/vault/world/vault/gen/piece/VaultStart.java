package iskallia.vault.world.vault.gen.piece;

import iskallia.vault.Vault;
import iskallia.vault.block.entity.HourglassTileEntity;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.TreasureHuntObjective;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.server.ServerWorld;

import java.util.Optional;

public class VaultStart extends VaultPiece {
    public static final ResourceLocation ID;

    public VaultStart() {
        super(VaultStart.ID);
    }

    public VaultStart(final ResourceLocation template, final MutableBoundingBox boundingBox, final Rotation rotation) {
        super(VaultStart.ID, template, boundingBox, rotation);
    }

    @Override
    public void tick(final ServerWorld world, final VaultRaid vault) {
        vault.getActiveObjective(TreasureHuntObjective.class).ifPresent(treasureHunt -> {
            final Optional<BlockPos> opt = vault.getProperties().getBase(VaultRaid.START_POS);
            final Direction facing = vault.getProperties().getBaseOrDefault(VaultRaid.START_FACING, Direction.NORTH);
            opt.ifPresent(pos -> {
                pos = pos.relative(facing, 1).relative(facing.getClockWise(), 8).relative(Direction.DOWN, 1);
                if (world.getBlockState(pos).getBlock() != ModBlocks.HOURGLASS) {
                    world.setBlock(pos, ModBlocks.HOURGLASS.defaultBlockState(), 3);
                }
                final TileEntity te = world.getBlockEntity(pos);
                if (te instanceof HourglassTileEntity) {
                    final int totalSand = vault.getGenerator().getPieces(VaultRoom.class).size() * ModConfigs.TREASURE_HUNT.sandPerRoom;
                    ((HourglassTileEntity)te).setTotalSand(totalSand);
                }
            });
        });
    }

    static {
        ID = Vault.id("start");
    }
}
