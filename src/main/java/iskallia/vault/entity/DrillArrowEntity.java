package iskallia.vault.entity;

import iskallia.vault.init.ModEntities;
import iskallia.vault.util.BlockHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class DrillArrowEntity extends ArrowEntity {
    private int maxBreakCount = 0;
    private int breakCount = 0;
    private boolean doBreak = true;

    public DrillArrowEntity(EntityType<? extends DrillArrowEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public DrillArrowEntity(World worldIn, double x, double y, double z) {
        this(ModEntities.DRILL_ARROW, worldIn);
        setPos(x, y, z);
    }

    public DrillArrowEntity(World worldIn, LivingEntity shooter) {
        this(worldIn, shooter.getX(), shooter.getEyeY() - 0.10000000149011612D, shooter.getZ());
        setOwner((Entity) shooter);
        if (shooter instanceof PlayerEntity) {
            this.pickup = AbstractArrowEntity.PickupStatus.ALLOWED;
        }
    }

    public DrillArrowEntity setMaxBreakCount(int maxBreakCount) {
        this.maxBreakCount = maxBreakCount;
        return this;
    }


    public void tick() {
        if (this.doBreak && !getCommandSenderWorld().isClientSide()) {
            aoeBreak();
        }
        if (getCommandSenderWorld().isClientSide()) {
            playEffects();
        }

        super.tick();
    }

    private void playEffects() {
        Vector3d vec = position();
        for (int i = 0; i < 5; i++) {
            Vector3d v = vec.add((this.random
                    .nextFloat() * 0.4F * (this.random.nextBoolean() ? 1 : -1)), (this.random
                    .nextFloat() * 0.4F * (this.random.nextBoolean() ? 1 : -1)), (this.random
                    .nextFloat() * 0.4F * (this.random.nextBoolean() ? 1 : -1)));
            this.level.addParticle((IParticleData) ParticleTypes.CAMPFIRE_COSY_SMOKE, v.x, v.y, v.z, 0.0D, 0.0D, 0.0D);
        }
    }

    private void aoeBreak() {
        Entity shooter = getOwner();
        if (!(shooter instanceof ServerPlayerEntity)) {
            return;
        }
        ServerPlayerEntity player = (ServerPlayerEntity) shooter;
        World world = getCommandSenderWorld();
        float vel = (float) getDeltaMovement().length();
        for (BlockPos offset : BlockHelper.getSphericalPositions(blockPosition(), Math.max(4.5F, 4.5F * vel))) {
            if (this.breakCount >= this.maxBreakCount) {
                break;
            }
            BlockState state = world.getBlockState(offset);
            if (!state.isAir((IBlockReader) world, offset) && (!state.requiresCorrectToolForDrops() || state.getHarvestLevel() <= 2)) {
                float hardness = state.getDestroySpeed((IBlockReader) world, offset);
                if (hardness >= 0.0F && hardness <= 25.0F &&
                        destroyBlock(world, offset, state, player)) {
                    this.breakCount++;
                }
            }
        }
    }


    private boolean destroyBlock(World world, BlockPos pos, BlockState state, ServerPlayerEntity player) {
        ItemStack miningItem = new ItemStack((IItemProvider) Items.DIAMOND_PICKAXE);
        Block.dropResources(world.getBlockState(pos), world, pos, world.getBlockEntity(pos), null, miningItem);
        return state.removedByPlayer(world, pos, (PlayerEntity) player, true, state.getFluidState());
    }


    protected void onHit(RayTraceResult result) {
        if (result instanceof net.minecraft.util.math.BlockRayTraceResult && this.breakCount < this.maxBreakCount &&
                !getCommandSenderWorld().isClientSide()) {
            aoeBreak();
        }

        if (this.breakCount >= this.maxBreakCount) {
            this.doBreak = false;
            super.onHit(result);
        }
    }


    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);

        this.doBreak = compound.getBoolean("break");
        this.breakCount = compound.getInt("breakCount");
        this.maxBreakCount = compound.getInt("maxBreakCount");
    }


    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);

        compound.putBoolean("break", this.doBreak);
        compound.putInt("breakCount", this.breakCount);
        compound.putInt("maxBreakCount", this.maxBreakCount);
    }


    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket((Entity) this);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\entity\DrillArrowEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */