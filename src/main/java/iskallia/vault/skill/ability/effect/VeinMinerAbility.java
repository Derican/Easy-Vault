package iskallia.vault.skill.ability.effect;

import iskallia.vault.event.ActiveFlags;
import iskallia.vault.skill.ability.AbilityNode;
import iskallia.vault.skill.ability.AbilityTree;
import iskallia.vault.skill.ability.config.VeinMinerConfig;
import iskallia.vault.util.BlockDropCaptureHelper;
import iskallia.vault.util.BlockHelper;
import iskallia.vault.world.data.PlayerAbilitiesData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class VeinMinerAbility<C extends VeinMinerConfig> extends AbilityEffect<C> {
    @Override
    public String getAbilityGroupName() {
        return "Vein Miner";
    }

    @SubscribeEvent
    public void onBlockMined(final BlockEvent.BreakEvent event) {
        if (event.getWorld().isClientSide() || event.getPlayer() instanceof FakePlayer) {
            return;
        }
        final ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        final AbilityTree abilityTree = PlayerAbilitiesData.get((ServerWorld) event.getWorld()).getAbilities(player);
        if (!abilityTree.isActive()) {
            return;
        }
        ActiveFlags.IS_AOE_MINING.runIfNotSet(() -> {
            final AbilityNode<?, ?> focusedAbilityNode = abilityTree.getSelectedAbility();
            if (focusedAbilityNode != null && focusedAbilityNode.getAbility() == this) {
                final C veinMinerConfig = (C) focusedAbilityNode.getAbilityConfig();
                final ServerWorld world = (ServerWorld) event.getWorld();
                final BlockPos pos = event.getPos();
                final BlockState blockState = world.getBlockState(pos);
                if (this.captureVeinMining(player, world, blockState.getBlock(), pos, veinMinerConfig)) {
                    event.setCanceled(true);
                }
                abilityTree.setSwappingLocked(true);
            }
        });
    }

    protected boolean captureVeinMining(final ServerPlayerEntity player, final ServerWorld world, final Block targetBlock, final BlockPos pos, final C config) {
        BlockDropCaptureHelper.startCapturing();
        try {
            return this.doVeinMine(player, world, targetBlock, pos, config);
        } finally {
            BlockDropCaptureHelper.getCapturedStacksAndStop().forEach(entity -> Block.popResource(world, entity.blockPosition(), entity.getItem()));
        }
    }

    protected boolean doVeinMine(final ServerPlayerEntity player, final ServerWorld world, final Block targetBlock, final BlockPos pos, final C config) {
        final ItemStack heldItem = player.getItemInHand(Hand.MAIN_HAND);
        if (heldItem.isDamageableItem()) {
            final int usesLeft = heldItem.getMaxDamage() - heldItem.getDamageValue();
            if (usesLeft <= 1) {
                return false;
            }
        }
        final int limit = config.getBlockLimit();
        final Set<BlockPos> traversedBlocks = new HashSet<BlockPos>();
        final Queue<BlockPos> positionQueue = new LinkedList<BlockPos>();
        positionQueue.add(pos);
        while (!positionQueue.isEmpty()) {
            final BlockPos headPos = positionQueue.poll();
            BlockPos.withinManhattanStream(headPos, 1, 1, 1).forEach(offset -> {
                if (traversedBlocks.size() >= limit) {
                    positionQueue.clear();
                    return;
                } else if (traversedBlocks.contains(offset)) {
                    return;
                } else {
                    final BlockState at = world.getBlockState(offset);
                    if (at.isAir(world, offset) || at.getBlock() != targetBlock) {
                        return;
                    } else {
                        if (this.shouldVoid(world, targetBlock)) {
                            at.removedByPlayer(world, offset, player, false, at.getFluidState());
                            BlockHelper.damageMiningItem(heldItem, player, 1);
                        } else if (this.doDestroy(world, offset, player, config)) {
                            BlockHelper.damageMiningItem(heldItem, player, 1);
                        }
                        positionQueue.add(offset.immutable());
                        traversedBlocks.add(offset.immutable());
                        return;
                    }
                }
            });
        }
        return true;
    }

    private boolean doDestroy(final ServerWorld world, final BlockPos pos, final ServerPlayerEntity player, final C config) {
        final ItemStack miningStack = this.getVeinMiningItem(player, config);
        return BlockHelper.breakBlock(world, player, pos, world.getBlockState(pos), miningStack, true, false);
    }

    public void damageMiningItem(final ItemStack heldItem, final PlayerEntity player, final C config) {
        heldItem.hurtAndBreak(1, (LivingEntity) player, playerEntity -> {
        });
    }

    protected ItemStack getVeinMiningItem(final PlayerEntity player, final C config) {
        return player.getItemInHand(Hand.MAIN_HAND);
    }

    public boolean shouldVoid(final ServerWorld world, final Block targetBlock) {
        return false;
    }
}
