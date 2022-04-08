package iskallia.vault.item.paxel.enhancement;

import iskallia.vault.event.ActiveFlags;
import iskallia.vault.util.BlockDropCaptureHelper;
import iskallia.vault.util.BlockHelper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.Color;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.LinkedList;
import java.util.List;

@EventBusSubscriber
public class HammerEnhancement
        extends PaxelEnhancement {
    public Color getColor() {
        return Color.fromRgb(-10042064);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onBlockMined(BlockEvent.BreakEvent event) {
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        ItemStack heldStack = player.getMainHandItem();

        if (!(PaxelEnhancements.getEnhancement(heldStack) instanceof HammerEnhancement)) {
            return;
        }

        ServerWorld world = (ServerWorld) event.getWorld();
        BlockPos centerPos = event.getPos();

        Direction.Axis axis = calcBreakAxis(player, centerPos);
        List<BlockPos> sidePoses = breakPoses(centerPos, axis);

        BlockState centerState = world.getBlockState(centerPos);
        float centerHardness = centerState.getDestroySpeed((IBlockReader) world, centerPos);

        ActiveFlags.IS_AOE_MINING.runIfNotSet(() -> {
            for (BlockPos sidePos : sidePoses) {
                BlockState state = world.getBlockState(sidePos);
                if (state.getBlock().isAir(state, (IBlockReader) world, sidePos)) {
                    continue;
                }
                float sideHardness = state.getDestroySpeed((IBlockReader) world, sidePos);
                if (sideHardness > centerHardness || sideHardness == -1.0F) {
                    continue;
                }
                BlockDropCaptureHelper.startCapturing();
                try {
                    BlockHelper.breakBlock(world, player, sidePos, true, true);
                    BlockHelper.damageMiningItem(heldStack, player, 1);
                } finally {
                    BlockDropCaptureHelper.getCapturedStacksAndStop().forEach(());
                }
            }
        });
    }


    public static Direction.Axis calcBreakAxis(ServerPlayerEntity player, BlockPos blockPos) {
        Vector3d eyePosition = player.getEyePosition(1.0F);
        Vector3d look = player.getViewVector(1.0F);
        Vector3d endPos = eyePosition.add(look.x * 5.0D, look.y * 5.0D, look.z * 5.0D);

        RayTraceContext rayTraceContext = new RayTraceContext(player.getEyePosition(1.0F), endPos, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, (Entity) player);


        BlockRayTraceResult result = player.level.clip(rayTraceContext);

        return result.getDirection().getAxis();
    }

    public static List<BlockPos> breakPoses(BlockPos blockPos, Direction.Axis axis) {
        List<BlockPos> poses = new LinkedList<>();

        if (axis == Direction.Axis.Y) {
            poses.add(blockPos.west());
            poses.add(blockPos.west().north());
            poses.add(blockPos.west().south());
            poses.add(blockPos.east());
            poses.add(blockPos.east().north());
            poses.add(blockPos.east().south());
            poses.add(blockPos.north());
            poses.add(blockPos.south());
        } else if (axis == Direction.Axis.X) {
            poses.add(blockPos.above());
            poses.add(blockPos.above().north());
            poses.add(blockPos.above().south());
            poses.add(blockPos.below());
            poses.add(blockPos.below().north());
            poses.add(blockPos.below().south());
            poses.add(blockPos.north());
            poses.add(blockPos.south());
        } else if (axis == Direction.Axis.Z) {
            poses.add(blockPos.above());
            poses.add(blockPos.above().west());
            poses.add(blockPos.above().east());
            poses.add(blockPos.below());
            poses.add(blockPos.below().west());
            poses.add(blockPos.below().east());
            poses.add(blockPos.west());
            poses.add(blockPos.east());
        }

        return poses;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\paxel\enhancement\HammerEnhancement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */