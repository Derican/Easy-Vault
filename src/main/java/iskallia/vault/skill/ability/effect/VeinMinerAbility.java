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
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class VeinMinerAbility<C extends VeinMinerConfig> extends AbilityEffect<C> {
    public String getAbilityGroupName() {
        return "Vein Miner";
    }

    @SubscribeEvent
    public void onBlockMined(BlockEvent.BreakEvent event) {
        if (event.getWorld().isClientSide() || event.getPlayer() instanceof net.minecraftforge.common.util.FakePlayer) {
            return;
        }

        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        AbilityTree abilityTree = PlayerAbilitiesData.get((ServerWorld) event.getWorld()).getAbilities((PlayerEntity) player);
        if (!abilityTree.isActive()) {
            return;
        }

        ActiveFlags.IS_AOE_MINING.runIfNotSet(() -> {
            AbilityNode<?, ?> focusedAbilityNode = abilityTree.getSelectedAbility();
            if (focusedAbilityNode != null && focusedAbilityNode.getAbility() == this) {
                VeinMinerConfig veinMinerConfig = (VeinMinerConfig) focusedAbilityNode.getAbilityConfig();
                ServerWorld world = (ServerWorld) event.getWorld();
                BlockPos pos = event.getPos();
                BlockState blockState = world.getBlockState(pos);
                if (captureVeinMining(player, world, blockState.getBlock(), pos, (C) veinMinerConfig)) {
                    event.setCanceled(true);
                }
                abilityTree.setSwappingLocked(true);
            }
        });
    }


    protected boolean captureVeinMining(ServerPlayerEntity player, ServerWorld world, Block targetBlock, BlockPos pos, C config) {
        BlockDropCaptureHelper.startCapturing();
        try {
            return doVeinMine(player, world, targetBlock, pos, config);
        } finally {
            BlockDropCaptureHelper.getCapturedStacksAndStop()
                    .forEach(entity -> Block.popResource((World) world, entity.blockPosition(), entity.getItem()));
        }
    }


    protected boolean doVeinMine(ServerPlayerEntity player, ServerWorld world, Block targetBlock, BlockPos pos, C config) {
        ItemStack heldItem = player.getItemInHand(Hand.MAIN_HAND);
        if (heldItem.isDamageableItem()) {
            int usesLeft = heldItem.getMaxDamage() - heldItem.getDamageValue();
            if (usesLeft <= 1) return false;

        }
        int limit = config.getBlockLimit();
        Set<BlockPos> traversedBlocks = new HashSet<>();
        Queue<BlockPos> positionQueue = new LinkedList<>();

        positionQueue.add(pos);


        while (!positionQueue.isEmpty()) {
            BlockPos headPos = positionQueue.poll();

            BlockPos.withinManhattanStream(headPos, 1, 1, 1).forEach(offset -> {
                if (traversedBlocks.size() >= limit) {
                    positionQueue.clear();

                    return;
                }
                if (traversedBlocks.contains(offset)) {
                    return;
                }
                BlockState at = world.getBlockState(offset);
                if (at.isAir((IBlockReader) world, offset) || at.getBlock() != targetBlock) {
                    return;
                }
                if (shouldVoid(world, targetBlock)) {
                    at.removedByPlayer((World) world, offset, (PlayerEntity) player, false, at.getFluidState());
                    BlockHelper.damageMiningItem(heldItem, player, 1);
                } else if (doDestroy(world, offset, player, (C) config)) {
                    BlockHelper.damageMiningItem(heldItem, player, 1);
                }
                positionQueue.add(offset.immutable());
                traversedBlocks.add(offset.immutable());
            });
        }
        return true;
    }

    private boolean doDestroy(ServerWorld world, BlockPos pos, ServerPlayerEntity player, C config) {
        ItemStack miningStack = getVeinMiningItem((PlayerEntity) player, config);
        return BlockHelper.breakBlock(world, player, pos, world.getBlockState(pos), miningStack, true, false);
    }


    public void damageMiningItem(ItemStack heldItem, PlayerEntity player, C config) {
        heldItem.hurtAndBreak(1, (LivingEntity) player, playerEntity -> {

        });
    }

    protected ItemStack getVeinMiningItem(PlayerEntity player, C config) {
        return player.getItemInHand(Hand.MAIN_HAND);
    }

    public boolean shouldVoid(ServerWorld world, Block targetBlock) {
        return false;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\effect\VeinMinerAbility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */