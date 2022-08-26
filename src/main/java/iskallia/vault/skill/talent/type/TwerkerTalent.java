package iskallia.vault.skill.talent.type;

import com.google.gson.annotations.Expose;
import net.minecraft.block.*;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;

import java.util.Iterator;
import java.util.List;

public class TwerkerTalent extends PlayerTalent {
    @Expose
    private final int tickDelay = 5;
    @Expose
    private int xRange = 2;
    @Expose
    private final int yRange = 1;
    @Expose
    private int zRange = 2;
    @Expose
    private final boolean growsPumpkinsMelons;
    @Expose
    private final boolean growsSugarcaneCactus;
    @Expose
    private final boolean growsAnimals;

    public TwerkerTalent(final int cost, int range, boolean growsPumpkinsMelons, boolean growsSugarcaneCactus, boolean growsAnimals) {
        super(cost);
        this.xRange = range;
        this.zRange = range;
        this.growsPumpkinsMelons = growsPumpkinsMelons;
        this.growsSugarcaneCactus = growsSugarcaneCactus;
        this.growsAnimals = growsAnimals;
    }

    public int getTickDelay() {
        return this.tickDelay;
    }

    public int getXRange() {
        return this.xRange;
    }

    public int getYRange() {
        return this.yRange;
    }

    public int getZRange() {
        return this.zRange;
    }

    @Override
    public void tick(final PlayerEntity player) {
        if (player.isCrouching() && player.getCommandSenderWorld() instanceof ServerWorld) {
            final ServerWorld world = (ServerWorld) player.getCommandSenderWorld();
            final BlockPos playerPos = player.blockPosition();
            final BlockPos pos = new BlockPos(playerPos.getX() + player.getRandom().nextInt(this.getXRange() * 2 + 1) - this.getXRange(), playerPos.getY() - player.getRandom().nextInt(this.getYRange() * 2 + 1) + this.getYRange(), playerPos.getZ() + player.getRandom().nextInt(this.getZRange() * 2 + 1) - this.getZRange());
            final BlockState state = world.getBlockState(pos);
            final Block block = world.getBlockState(pos).getBlock();
            if (block instanceof CropsBlock || block instanceof SaplingBlock) {
                BoneMealItem.applyBonemeal(new ItemStack(Items.BONE_MEAL), world, pos, player);
                world.sendParticles((IParticleData) ParticleTypes.HAPPY_VILLAGER, pos.getX(), pos.getY(), pos.getZ(), 100, 1.0, 0.5, 1.0, 0.0);
            }
            if (this.growsPumpkinsMelons && block instanceof StemBlock) {
                if (((StemBlock) block).isValidBonemealTarget(world, pos, state, false)) {
                    BoneMealItem.applyBonemeal(new ItemStack(Items.BONE_MEAL), world, pos, player);
                } else {
                    for (int i = 0; i < 40; ++i) {
                        state.randomTick(world, pos, world.random);
                    }
                }
                world.sendParticles((IParticleData) ParticleTypes.HAPPY_VILLAGER, pos.getX(), pos.getY(), pos.getZ(), 100, 1.0, 0.5, 1.0, 0.0);
            }
            if (this.growsSugarcaneCactus) {
                final BlockPos above = new BlockPos(pos).above();
                if (!world.isEmptyBlock(above)) {
                    return;
                }
                if (block instanceof SugarCaneBlock || block instanceof CactusBlock) {
                    int height;
                    for (height = 1; world.getBlockState(pos.below(height)).is(block); ++height) {
                    }
                    if (height < 3 && TwerkerTalent.rand.nextInt(3) == 0 && ForgeHooks.onCropsGrowPre(world, pos, state, true)) {
                        world.setBlockAndUpdate(above, block.defaultBlockState());
                        final BlockState newState = state.setValue(BlockStateProperties.AGE_15, 0);
                        world.setBlock(pos, newState, 4);
                        newState.neighborChanged(world, above, block, pos, false);
                        world.getBlockTicks().scheduleTick(above, block, 1, TickPriority.EXTREMELY_HIGH);
                        ForgeHooks.onCropsGrowPost(world, above, state);
                        world.sendParticles((IParticleData) ParticleTypes.HAPPY_VILLAGER, pos.getX(), pos.getY(), pos.getZ(), 100, 1.0, 0.5, 1.0, 0.0);
                    }
                }
            }
            if (this.growsAnimals) {
                final AxisAlignedBB searchBox = player.getBoundingBox().inflate(this.getXRange(), this.getYRange(), this.getZRange());
                final List<AgeableEntity> entities = world.getLoadedEntitiesOfClass(AgeableEntity.class, searchBox, entity -> entity.isAlive() && !entity.isSpectator() && entity.isBaby());
                AgeableEntity entity = null;
                for (AgeableEntity ageableEntity : entities) {
                    entity = ageableEntity;
                    if (TwerkerTalent.rand.nextFloat() < 0.4f) {
                        world.sendParticles((IParticleData) ParticleTypes.HAPPY_VILLAGER, pos.getX(), pos.getY(), pos.getZ(), 100, 1.0, 0.5, 1.0, 0.0);
                    }
                    if (TwerkerTalent.rand.nextFloat() < 0.05f) {
                        entity.setBaby(false);
                    }
                }
            }
        }
    }
}
