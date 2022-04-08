package iskallia.vault.skill.ability.effect.sub;

import iskallia.vault.skill.ability.AbilityNode;
import iskallia.vault.skill.ability.AbilityTree;
import iskallia.vault.skill.ability.config.MegaJumpConfig;
import iskallia.vault.skill.ability.config.sub.MegaJumpBreakConfig;
import iskallia.vault.skill.ability.effect.MegaJumpAbility;
import iskallia.vault.util.BlockHelper;
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
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.UUID;

public class MegaJumpBreakAbility extends MegaJumpAbility<MegaJumpBreakConfig> {
    private final Map<UUID, Integer> playerBreakMap = new HashMap<>();


    public boolean onAction(MegaJumpBreakConfig config, ServerPlayerEntity player, boolean active) {
        if (super.onAction((MegaJumpConfig) config, player, active)) {

            this.playerBreakMap.put(player.getUUID(), Integer.valueOf(30));
            return true;
        }
        return false;
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START || event.player
                .getCommandSenderWorld().isClientSide() ||
                !(event.player.getCommandSenderWorld() instanceof ServerWorld)) {
            return;
        }

        PlayerEntity player = event.player;
        UUID plUUID = player.getUUID();
        if (!this.playerBreakMap.containsKey(plUUID)) {
            return;
        }

        int ticks = ((Integer) this.playerBreakMap.get(plUUID)).intValue();
        ticks--;
        if (ticks <= 0) {
            this.playerBreakMap.remove(plUUID);
            return;
        }
        this.playerBreakMap.put(plUUID, Integer.valueOf(ticks));


        ServerWorld sWorld = (ServerWorld) player.getCommandSenderWorld();
        AbilityTree abilityTree = PlayerAbilitiesData.get(sWorld).getAbilities(player);
        AbilityNode<?, ?> focusedAbilityNode = abilityTree.getSelectedAbility();
        if (focusedAbilityNode != null && focusedAbilityNode.getAbility() == this) {
            for (BlockPos offset : BlockHelper.getOvalPositions(player.blockPosition().above(3), 4.0F, 6.0F)) {
                BlockState state = sWorld.getBlockState(offset);
                if (!state.isAir((IBlockReader) sWorld, offset) && (!state.requiresCorrectToolForDrops() || state.getHarvestLevel() <= 2)) {
                    float hardness = state.getDestroySpeed((IBlockReader) sWorld, offset);
                    if (hardness >= 0.0F && hardness <= 25.0F) {
                        destroyBlock(sWorld, offset, player);
                    }
                }
            }
        }
    }


    private void destroyBlock(ServerWorld world, BlockPos pos, PlayerEntity player) {
        ItemStack miningItem = new ItemStack((IItemProvider) Items.DIAMOND_PICKAXE);
        Block.dropResources(world.getBlockState(pos), (World) world, pos, world.getBlockEntity(pos), null, miningItem);
        world.destroyBlock(pos, false, (Entity) player);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\effect\sub\MegaJumpBreakAbility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */