// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.block;

import net.minecraft.state.properties.BlockStateProperties;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.architect.SummonAndKillBossesVotingSession;
import iskallia.vault.world.vault.logic.objective.architect.ArchitectSummonAndKillBossesObjective;
import iskallia.vault.world.data.VaultRaidData;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.Hand;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.state.StateContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.item.ItemGroup;
import net.minecraft.state.Property;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.state.EnumProperty;
import net.minecraft.block.Block;

public class StabilizerCompassBlock extends Block
{
    public static final EnumProperty<Direction> DIRECTION;
    
    public StabilizerCompassBlock() {
        super(AbstractBlock.Properties.of(Material.STONE).sound(SoundType.STONE).strength(-1.0f, 3.6E8f).noDrops());
        this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)StabilizerCompassBlock.DIRECTION, (Comparable)Direction.NORTH));
    }
    
    public void fillItemCategory(final ItemGroup group, final NonNullList<ItemStack> items) {
    }
    
    protected void createBlockStateDefinition(final StateContainer.Builder<Block, BlockState> builder) {
        builder.add(new Property[] { (Property)StabilizerCompassBlock.DIRECTION });
    }
    
    public ActionResultType use(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockRayTraceResult hit) {
        if (hand != Hand.MAIN_HAND) {
            return ActionResultType.PASS;
        }
        if (world instanceof ServerWorld) {
            final ServerWorld sWorld = (ServerWorld)world;
            final VaultRaid vault = VaultRaidData.get(sWorld).getAt(sWorld, pos);
            if (vault != null) {
                vault.getActiveObjective(ArchitectSummonAndKillBossesObjective.class).ifPresent(objective -> {
                    if (objective.getActiveSession() instanceof SummonAndKillBossesVotingSession) {
                        final SummonAndKillBossesVotingSession session = (SummonAndKillBossesVotingSession)objective.getActiveSession();
                        final Direction direction = (Direction)state.getValue((Property)StabilizerCompassBlock.DIRECTION);
                        final BlockPos stabilizerPos = pos.above().relative(direction.getOpposite());
                        if (stabilizerPos.equals((Object)session.getStabilizerPos()) && session.hasDirectionChoice(direction)) {
                            session.setVotedDirection(direction);
                        }
                    }
                    return;
                });
            }
        }
        return ActionResultType.SUCCESS;
    }
    
    static {
        DIRECTION = (EnumProperty)BlockStateProperties.HORIZONTAL_FACING;
    }
}
