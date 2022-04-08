package iskallia.vault.util;

import iskallia.vault.skill.ability.AbilityNode;
import iskallia.vault.skill.ability.AbilityTree;
import iskallia.vault.skill.ability.config.AbilityConfig;
import iskallia.vault.skill.ability.config.VeinMinerConfig;
import iskallia.vault.skill.ability.effect.VeinMinerAbility;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

public class BlockHelper {
    public static Iterable<BlockPos> getSphericalPositions(BlockPos center, float radius) {
        return getOvalPositions(center, radius, radius);
    }

    public static Iterable<BlockPos> getOvalPositions(BlockPos center, float widthRadius, float heightRadius) {
        Collection<BlockPos> positions = new Stack<>();
        int wRadius = MathHelper.ceil(widthRadius);
        int hRadius = MathHelper.ceil(heightRadius);
        BlockPos pos = BlockPos.ZERO;
        for (int xx = -wRadius; xx <= wRadius; xx++) {
            for (int yy = -hRadius; yy <= hRadius; yy++) {
                for (int zz = -wRadius; zz <= wRadius; zz++) {
                    if (pos.distSqr((xx + 0.5F), (yy + 0.5F), (zz + 0.5F), true) <= Math.max(widthRadius, heightRadius)) {
                        positions.add(pos.offset((Vector3i) center).offset(xx, yy, zz));
                    }
                }
            }
        }
        return positions;
    }

    public static void damageMiningItem(ItemStack stack, ServerPlayerEntity player, int amount) {
        Runnable damageItem = () -> stack.hurtAndBreak(amount, (LivingEntity) player, ());
        AbilityTree abilityTree = PlayerAbilitiesData.get(player.getLevel()).getAbilities((PlayerEntity) player);
        if (abilityTree.isActive()) {
            AbilityNode<?, ?> focusedAbilityNode = abilityTree.getSelectedAbility();
            if (focusedAbilityNode != null && focusedAbilityNode.getAbility() instanceof VeinMinerAbility) {
                AbilityConfig cfg = focusedAbilityNode.getAbilityConfig();
                if (cfg instanceof VeinMinerConfig) {
                    damageItem = (() -> ((VeinMinerAbility) focusedAbilityNode.getAbility()).damageMiningItem(stack, (PlayerEntity) player, (VeinMinerConfig) cfg));
                }
            }
        }
        for (int i = 0; i < amount; i++) {
            damageItem.run();
        }
    }

    public static boolean breakBlock(ServerWorld world, ServerPlayerEntity player, BlockPos pos, boolean breakBlock, boolean ignoreHarvestRestrictions) {
        return breakBlock(world, player, pos, world.getBlockState(pos), player.getMainHandItem(), breakBlock, ignoreHarvestRestrictions);
    }

    public static boolean breakBlock(ServerWorld world, ServerPlayerEntity player, BlockPos pos, BlockState stateBroken, ItemStack heldItem, boolean breakBlock, boolean ignoreHarvestRestrictions) {
        ItemStack original = player.getItemInHand(Hand.MAIN_HAND);
        try {
            player.setItemInHand(Hand.MAIN_HAND, heldItem);
            return doNativeBreakBlock(world, player, pos, stateBroken, heldItem, breakBlock, ignoreHarvestRestrictions);
        } finally {
            player.setItemInHand(Hand.MAIN_HAND, original);
        }
    }

    private static boolean doNativeBreakBlock(ServerWorld world, ServerPlayerEntity player, BlockPos pos, BlockState stateBroken, ItemStack heldItem, boolean breakBlock, boolean ignoreHarvestRestrictions) {
        int xp;
        try {
            boolean preCancelEvent = false;
            if (!heldItem.isEmpty() && !heldItem.getItem().canAttackBlock(stateBroken, (World) world, pos, (PlayerEntity) player)) {
                preCancelEvent = true;
            }
            BlockEvent.BreakEvent event = new BlockEvent.BreakEvent((World) world, pos, stateBroken, (PlayerEntity) player);
            event.setCanceled(preCancelEvent);
            MinecraftForge.EVENT_BUS.post((Event) event);

            if (event.isCanceled()) {
                return false;
            }
            xp = event.getExpToDrop();
        } catch (Exception exc) {
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
        } catch (Exception exc) {
            return false;
        }

        try {
            heldItem.copy().mineBlock((World) world, stateBroken, pos, (PlayerEntity) player);
        } catch (Exception exc) {
            return false;
        }

        boolean wasCapturingStates = world.captureBlockSnapshots;
        List<BlockSnapshot> previousCapturedStates = new ArrayList<>(world.capturedBlockSnapshots);

        TileEntity tileEntity = world.getBlockEntity(pos);

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
        } catch (Exception exc) {
            restoreWorldState((World) world, wasCapturingStates, previousCapturedStates);
            return false;
        }

        stateBroken.getBlock().destroy((IWorld) world, pos, stateBroken);

        if (harvestable) {
            try {
                stateBroken.getBlock().playerDestroy((World) world, (PlayerEntity) player, pos, stateBroken, tileEntity, heldItem.copy());
            } catch (Exception exc) {
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

    private static void restoreWorldState(World world, boolean prevCaptureFlag, List<BlockSnapshot> prevSnapshots) {
        world.captureBlockSnapshots = false;

        world.restoringBlockSnapshots = true;
        world.capturedBlockSnapshots.forEach(s -> s.restore(true));
        world.restoringBlockSnapshots = false;

        world.capturedBlockSnapshots.clear();

        world.captureBlockSnapshots = prevCaptureFlag;
        world.capturedBlockSnapshots.addAll(prevSnapshots);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\BlockHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */