package iskallia.vault.skill.ability.effect.sub;

import iskallia.vault.skill.ability.AbilityNode;
import iskallia.vault.skill.ability.AbilityTree;
import iskallia.vault.skill.ability.config.sub.MegaJumpBreakConfig;
import iskallia.vault.skill.ability.effect.MegaJumpAbility;
import iskallia.vault.util.BlockHelper;
import iskallia.vault.world.data.PlayerAbilitiesData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MegaJumpBreakAbility extends MegaJumpAbility<MegaJumpBreakConfig> {
    private final Map<UUID, Integer> playerBreakMap;

    public MegaJumpBreakAbility() {
        this.playerBreakMap = new HashMap<UUID, Integer>();
    }

    @Override
    public boolean onAction(final MegaJumpBreakConfig config, final ServerPlayerEntity player, final boolean active) {
        if (super.onAction(config, player, active)) {
            this.playerBreakMap.put(player.getUUID(), 30);
            return true;
        }
        return false;
    }

    @SubscribeEvent
    public void onPlayerTick(final TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START || event.player.getCommandSenderWorld().isClientSide() || !(event.player.getCommandSenderWorld() instanceof ServerWorld)) {
            return;
        }
        final PlayerEntity player = event.player;
        final UUID plUUID = player.getUUID();
        if (!this.playerBreakMap.containsKey(plUUID)) {
            return;
        }
        int ticks = this.playerBreakMap.get(plUUID);
        if (--ticks <= 0) {
            this.playerBreakMap.remove(plUUID);
            return;
        }
        this.playerBreakMap.put(plUUID, ticks);
        final ServerWorld sWorld = (ServerWorld) player.getCommandSenderWorld();
        final AbilityTree abilityTree = PlayerAbilitiesData.get(sWorld).getAbilities(player);
        final AbilityNode<?, ?> focusedAbilityNode = abilityTree.getSelectedAbility();
        if (focusedAbilityNode != null && focusedAbilityNode.getAbility() == this) {
            for (final BlockPos offset : BlockHelper.getOvalPositions(player.blockPosition().above(3), 4.0f, 6.0f)) {
                final BlockState state = sWorld.getBlockState(offset);
                if (!state.isAir(sWorld, offset) && (!state.requiresCorrectToolForDrops() || state.getHarvestLevel() <= 2)) {
                    final float hardness = state.getDestroySpeed(sWorld, offset);
                    if (hardness < 0.0f || hardness > 25.0f) {
                        continue;
                    }
                    this.destroyBlock(sWorld, offset, player);
                }
            }
        }
    }

    private void destroyBlock(final ServerWorld world, final BlockPos pos, final PlayerEntity player) {
        final ItemStack miningItem = new ItemStack(Items.DIAMOND_PICKAXE);
        Block.dropResources(world.getBlockState(pos), world, pos, world.getBlockEntity(pos), null, miningItem);
        world.destroyBlock(pos, false, player);
    }
}
