// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.item.paxel.enhancement;

import iskallia.vault.event.ActiveFlags;
import iskallia.vault.util.BlockDropCaptureHelper;
import iskallia.vault.util.BlockHelper;
import net.minecraft.block.Block;
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
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Mod.EventBusSubscriber
public class HammerEnhancement extends PaxelEnhancement
{
    @Override
    public Color getColor() {
        return Color.fromRgb(-10042064);
    }
    
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onBlockMined(final BlockEvent.BreakEvent event) {
        final ServerPlayerEntity player = (ServerPlayerEntity)event.getPlayer();
        final ItemStack heldStack = player.getMainHandItem();
        if (!(PaxelEnhancements.getEnhancement(heldStack) instanceof HammerEnhancement)) {
            return;
        }
        final ServerWorld world = (ServerWorld)event.getWorld();
        final BlockPos centerPos = event.getPos();
        final Direction.Axis axis = calcBreakAxis(player, centerPos);
        final List<BlockPos> sidePoses = breakPoses(centerPos, axis);
        final BlockState centerState = world.getBlockState(centerPos);
        final float centerHardness = centerState.getDestroySpeed((IBlockReader)world, centerPos);
        ActiveFlags.IS_AOE_MINING.runIfNotSet(() -> {

            final Iterator<BlockPos> iterator=sidePoses.iterator();
            while (iterator.hasNext()) {
                final BlockPos sidePos = iterator.next();
                final BlockState state = world.getBlockState(sidePos);
                if (state.getBlock().isAir(state, (IBlockReader)world, sidePos)) {
                    continue;
                }
                else {
                    final float sideHardness = state.getDestroySpeed((IBlockReader)world, sidePos);
                    if (sideHardness <= centerHardness) {
                        if (sideHardness == -1.0f) {
                            continue;
                        }
                        else {
                            BlockDropCaptureHelper.startCapturing();
                            try {
                                BlockHelper.breakBlock(world, player, sidePos, true, true);
                                BlockHelper.damageMiningItem(heldStack, player, 1);
                            }
                            finally {
                                BlockDropCaptureHelper.getCapturedStacksAndStop().forEach(entity -> Block.popResource((World)world, entity.blockPosition(), entity.getItem()));
                            }
                        }
                    }
                    else {
                        continue;
                    }
                }
            }
        });
    }
    
    public static Direction.Axis calcBreakAxis(final ServerPlayerEntity player, final BlockPos blockPos) {
        final Vector3d eyePosition = player.getEyePosition(1.0f);
        final Vector3d look = player.getViewVector(1.0f);
        final Vector3d endPos = eyePosition.add(look.x * 5.0, look.y * 5.0, look.z * 5.0);
        final RayTraceContext rayTraceContext = new RayTraceContext(player.getEyePosition(1.0f), endPos, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, (Entity)player);
        final BlockRayTraceResult result = player.level.clip(rayTraceContext);
        return result.getDirection().getAxis();
    }
    
    public static List<BlockPos> breakPoses(final BlockPos blockPos, final Direction.Axis axis) {
        final List<BlockPos> poses = new LinkedList<BlockPos>();
        if (axis == Direction.Axis.Y) {
            poses.add(blockPos.west());
            poses.add(blockPos.west().north());
            poses.add(blockPos.west().south());
            poses.add(blockPos.east());
            poses.add(blockPos.east().north());
            poses.add(blockPos.east().south());
            poses.add(blockPos.north());
            poses.add(blockPos.south());
        }
        else if (axis == Direction.Axis.X) {
            poses.add(blockPos.above());
            poses.add(blockPos.above().north());
            poses.add(blockPos.above().south());
            poses.add(blockPos.below());
            poses.add(blockPos.below().north());
            poses.add(blockPos.below().south());
            poses.add(blockPos.north());
            poses.add(blockPos.south());
        }
        else if (axis == Direction.Axis.Z) {
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
