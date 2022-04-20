package iskallia.vault.util;

import iskallia.vault.skill.ability.AbilityNode;
import iskallia.vault.skill.ability.AbilityTree;
import iskallia.vault.skill.ability.config.AbilityConfig;
import iskallia.vault.skill.ability.config.VeinMinerConfig;
import iskallia.vault.skill.ability.effect.VeinMinerAbility;
import iskallia.vault.world.data.PlayerAbilitiesData;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

public class BlockHelper {
    public static Iterable<BlockPos> getSphericalPositions(final BlockPos center, final float radius) {
        return getOvalPositions(center, radius, radius);
    }

    public static Iterable<BlockPos> getOvalPositions(final BlockPos center, final float widthRadius, final float heightRadius) {
        final Collection<BlockPos> positions = new Stack<BlockPos>();
        final int wRadius = MathHelper.ceil(widthRadius);
        final int hRadius = MathHelper.ceil(heightRadius);
        final BlockPos pos = BlockPos.ZERO;
        for (int xx = -wRadius; xx <= wRadius; ++xx) {
            for (int yy = -hRadius; yy <= hRadius; ++yy) {
                for (int zz = -wRadius; zz <= wRadius; ++zz) {
                    if (pos.distSqr((double) (xx + 0.5f), (double) (yy + 0.5f), (double) (zz + 0.5f), true) <= Math.max(widthRadius, heightRadius)) {
                        positions.add(pos.offset((Vector3i) center).offset(xx, yy, zz));
                    }
                }
            }
        }
        return positions;
    }

    public static void damageMiningItem(final ItemStack stack, final ServerPlayerEntity player, final int amount) {
        Runnable damageItem = () -> stack.hurtAndBreak(amount, (LivingEntity) player, playerEntity -> {
        });
        final AbilityTree abilityTree = PlayerAbilitiesData.get(player.getLevel()).getAbilities((PlayerEntity) player);
        if (abilityTree.isActive()) {
            final AbilityNode<?, ?> focusedAbilityNode = abilityTree.getSelectedAbility();
            if (focusedAbilityNode != null && focusedAbilityNode.getAbility() instanceof VeinMinerAbility) {
                final AbilityConfig cfg = (AbilityConfig) focusedAbilityNode.getAbilityConfig();
                if (cfg instanceof VeinMinerConfig) {
                    damageItem = (() -> ((VeinMinerAbility) focusedAbilityNode.getAbility()).damageMiningItem(stack, (PlayerEntity) player, (VeinMinerConfig) cfg));
                }
            }
        }
        for (int i = 0; i < amount; ++i) {
            damageItem.run();
        }
    }

    public static boolean breakBlock(final ServerWorld world, final ServerPlayerEntity player, final BlockPos pos, final boolean breakBlock, final boolean ignoreHarvestRestrictions) {
        return breakBlock(world, player, pos, world.getBlockState(pos), player.getMainHandItem(), breakBlock, ignoreHarvestRestrictions);
    }

    public static boolean breakBlock(final ServerWorld world, final ServerPlayerEntity player, final BlockPos pos, final BlockState stateBroken, final ItemStack heldItem, final boolean breakBlock, final boolean ignoreHarvestRestrictions) {
        final ItemStack original = player.getItemInHand(Hand.MAIN_HAND);
        try {
            player.setItemInHand(Hand.MAIN_HAND, heldItem);
            return doNativeBreakBlock(world, player, pos, stateBroken, heldItem, breakBlock, ignoreHarvestRestrictions);
        } finally {
            player.setItemInHand(Hand.MAIN_HAND, original);
        }
    }

    private static boolean doNativeBreakBlock(final ServerWorld world, final ServerPlayerEntity player, final BlockPos pos, final BlockState stateBroken, final ItemStack heldItem, final boolean breakBlock, final boolean ignoreHarvestRestrictions) {
        int xp;
        try {
            boolean preCancelEvent = false;
            if (!heldItem.isEmpty() && !heldItem.getItem().canAttackBlock(stateBroken, (World) world, pos, (PlayerEntity) player)) {
                preCancelEvent = true;
            }
            final BlockEvent.BreakEvent event = new BlockEvent.BreakEvent((World) world, pos, stateBroken, (PlayerEntity) player);
            event.setCanceled(preCancelEvent);
            MinecraftForge.EVENT_BUS.post((Event) event);
            if (event.isCanceled()) {
                return false;
            }
            xp = event.getExpToDrop();
        } catch (final Exception exc) {
            return false;
        }
        if (xp == -1) {
            return false;
        }
        if (heldItem.onBlockStartBreak(pos, (PlayerEntity) player)) {
            return false;
        }
        boolean harvestable = true;
        try {
            if (!ignoreHarvestRestrictions) {
                harvestable = stateBroken.canHarvestBlock((IBlockReader) world, pos, (PlayerEntity) player);
            }
        } catch (final Exception exc2) {
            return false;
        }
        try {
            heldItem.copy().mineBlock((World) world, stateBroken, pos, (PlayerEntity) player);
        } catch (final Exception exc2) {
            return false;
        }
        final boolean wasCapturingStates = world.captureBlockSnapshots;
        final List<BlockSnapshot> previousCapturedStates = new ArrayList<BlockSnapshot>(world.capturedBlockSnapshots);
        final TileEntity tileEntity = world.getBlockEntity(pos);
        world.captureBlockSnapshots = true;
        try {
            if (breakBlock) {
                if (!stateBroken.removedByPlayer((World) world, pos, (PlayerEntity) player, harvestable, Fluids.EMPTY.defaultFluidState())) {
                    restoreWorldState((World) world, wasCapturingStates, previousCapturedStates);
                    return false;
                }
            } else {
                stateBroken.getBlock().playerWillDestroy((World) world, pos, stateBroken, (PlayerEntity) player);
            }
        } catch (final Exception exc3) {
            restoreWorldState((World) world, wasCapturingStates, previousCapturedStates);
            return false;
        }
        stateBroken.getBlock().destroy((IWorld) world, pos, stateBroken);
        if (harvestable) {
            try {
                stateBroken.getBlock().playerDestroy((World) world, (PlayerEntity) player, pos, stateBroken, tileEntity, heldItem.copy());
            } catch (final Exception exc3) {
                restoreWorldState((World) world, wasCapturingStates, previousCapturedStates);
                return false;
            }
        }
        if (xp > 0) {
            stateBroken.getBlock().popExperience(world, pos, xp);
        }
        BlockDropCaptureHelper.startCapturing();
        try {
            world.captureBlockSnapshots = false;
            world.restoringBlockSnapshots = true;
            world.capturedBlockSnapshots.forEach(s -> {
                world.sendBlockUpdated(s.getPos(), s.getReplacedBlock(), s.getCurrentBlock(), s.getFlag());
                s.getCurrentBlock().updateNeighbourShapes((IWorld) world, s.getPos(), 11);
                return;
            });
            world.restoringBlockSnapshots = false;
        } finally {
            BlockDropCaptureHelper.getCapturedStacksAndStop();
            world.capturedBlockSnapshots.clear();
            world.captureBlockSnapshots = wasCapturingStates;
            world.capturedBlockSnapshots.addAll(previousCapturedStates);
        }
        return true;
    }

    private static void restoreWorldState(final World world, final boolean prevCaptureFlag, final List<BlockSnapshot> prevSnapshots) {
        world.captureBlockSnapshots = false;
        world.restoringBlockSnapshots = true;
        world.capturedBlockSnapshots.forEach(s -> s.restore(true));
        world.restoringBlockSnapshots = false;
        world.capturedBlockSnapshots.clear();
        world.captureBlockSnapshots = prevCaptureFlag;
        world.capturedBlockSnapshots.addAll(prevSnapshots);
    }
}
