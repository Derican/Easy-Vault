package iskallia.vault.skill.talent.type;

import com.google.gson.annotations.Expose;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;

import java.util.List;

public class TwerkerTalent extends PlayerTalent {
    @Expose
    private final int tickDelay = 5;
    @Expose
    private final int xRange = 2;
    @Expose
    private final int yRange = 1;
    @Expose
    private final int zRange = 2;
    @Expose
    private boolean growsPumpkinsMelons = false;
    @Expose
    private boolean growsSugarcaneCactus = false;
    @Expose
    private boolean growsAnimals = false;

    public TwerkerTalent(int cost) {
        super(cost);
    }

    public int getTickDelay() {
        getClass();
        return 5;
    }

    public int getXRange() {
        getClass();
        return 2;
    }

    public int getYRange() {
        getClass();
        return 1;
    }

    public int getZRange() {
        getClass();
        return 2;
    }


    public void tick(PlayerEntity player) {
        if (player.isCrouching() && player.getCommandSenderWorld() instanceof ServerWorld) {
            ServerWorld world = (ServerWorld) player.getCommandSenderWorld();
            BlockPos playerPos = player.blockPosition();


            BlockPos pos = new BlockPos(playerPos.getX() + player.getRandom().nextInt(getXRange() * 2 + 1) - getXRange(), playerPos.getY() - player.getRandom().nextInt(getYRange() * 2 + 1) + getYRange(), playerPos.getZ() + player.getRandom().nextInt(getZRange() * 2 + 1) - getZRange());

            BlockState state = world.getBlockState(pos);
            Block block = world.getBlockState(pos).getBlock();

            if (block instanceof net.minecraft.block.CropsBlock || block instanceof net.minecraft.block.SaplingBlock) {
                BoneMealItem.applyBonemeal(new ItemStack((IItemProvider) Items.BONE_MEAL), (World) world, pos, player);
                world.sendParticles((IParticleData) ParticleTypes.HAPPY_VILLAGER, pos.getX(), pos.getY(), pos.getZ(), 100, 1.0D, 0.5D, 1.0D, 0.0D);
            }

            if (this.growsPumpkinsMelons &&
                    block instanceof StemBlock) {
                if (((StemBlock) block).isValidBonemealTarget((IBlockReader) world, pos, state, false)) {
                    BoneMealItem.applyBonemeal(new ItemStack((IItemProvider) Items.BONE_MEAL), (World) world, pos, player);
                } else {
                    for (int i = 0; i < 40; i++) {
                        state.randomTick(world, pos, world.random);
                    }
                }
                world.sendParticles((IParticleData) ParticleTypes.HAPPY_VILLAGER, pos.getX(), pos.getY(), pos.getZ(), 100, 1.0D, 0.5D, 1.0D, 0.0D);
            }


            if (this.growsSugarcaneCactus) {
                BlockPos above = (new BlockPos((Vector3i) pos)).above();
                if (!world.isEmptyBlock(above))
                    return;
                if (block instanceof net.minecraft.block.SugarCaneBlock || block instanceof net.minecraft.block.CactusBlock) {
                    int height = 1;
                    while (world.getBlockState(pos.below(height)).is(block)) {
                        height++;
                    }
                    if (height < 3 && rand.nextInt(3) == 0 &&
                            ForgeHooks.onCropsGrowPre((World) world, pos, state, true)) {
                        world.setBlockAndUpdate(above, block.defaultBlockState());
                        BlockState newState = (BlockState) state.setValue((Property) BlockStateProperties.AGE_15, Integer.valueOf(0));
                        world.setBlock(pos, newState, 4);
                        newState.neighborChanged((World) world, above, block, pos, false);
                        world.getBlockTicks().scheduleTick(above, block, 1, TickPriority.EXTREMELY_HIGH);

                        ForgeHooks.onCropsGrowPost((World) world, above, state);
                        world.sendParticles((IParticleData) ParticleTypes.HAPPY_VILLAGER, pos.getX(), pos.getY(), pos.getZ(), 100, 1.0D, 0.5D, 1.0D, 0.0D);
                    }
                }
            }


            if (this.growsAnimals) {
                AxisAlignedBB searchBox = player.getBoundingBox().inflate(getXRange(), getYRange(), getZRange());
                List<AgeableEntity> entities = world.getLoadedEntitiesOfClass(AgeableEntity.class, searchBox, entity ->
                        (entity.isAlive() && !entity.isSpectator() && entity.isBaby()));

                for (AgeableEntity entity : entities) {
                    if (rand.nextFloat() < 0.4F) {
                        world.sendParticles((IParticleData) ParticleTypes.HAPPY_VILLAGER, pos.getX(), pos.getY(), pos.getZ(), 100, 1.0D, 0.5D, 1.0D, 0.0D);
                    }

                    if (rand.nextFloat() < 0.05F)
                        entity.setBaby(false);
                }
            }
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\talent\type\TwerkerTalent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */