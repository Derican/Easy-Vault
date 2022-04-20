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
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class DrillArrowEntity extends ArrowEntity {
    private int maxBreakCount;
    private int breakCount;
    private boolean doBreak;

    public DrillArrowEntity(final EntityType<? extends DrillArrowEntity> type, final World worldIn) {
        super((EntityType) type, worldIn);
        this.maxBreakCount = 0;
        this.breakCount = 0;
        this.doBreak = true;
    }

    public DrillArrowEntity(final World worldIn, final double x, final double y, final double z) {
        this(ModEntities.DRILL_ARROW, worldIn);
        this.setPos(x, y, z);
    }

    public DrillArrowEntity(final World worldIn, final LivingEntity shooter) {
        this(worldIn, shooter.getX(), shooter.getEyeY() - 0.10000000149011612, shooter.getZ());
        this.setOwner((Entity) shooter);
        if (shooter instanceof PlayerEntity) {
            this.pickup = AbstractArrowEntity.PickupStatus.ALLOWED;
        }
    }

    public DrillArrowEntity setMaxBreakCount(final int maxBreakCount) {
        this.maxBreakCount = maxBreakCount;
        return this;
    }

    public void tick() {
        if (this.doBreak && !this.getCommandSenderWorld().isClientSide()) {
            this.aoeBreak();
        }
        if (this.getCommandSenderWorld().isClientSide()) {
            this.playEffects();
        }
        super.tick();
    }

    private void playEffects() {
        final Vector3d vec = this.position();
        for (int i = 0; i < 5; ++i) {
            final Vector3d v = vec.add((double) (this.random.nextFloat() * 0.4f * (this.random.nextBoolean() ? 1 : -1)), (double) (this.random.nextFloat() * 0.4f * (this.random.nextBoolean() ? 1 : -1)), (double) (this.random.nextFloat() * 0.4f * (this.random.nextBoolean() ? 1 : -1)));
            this.level.addParticle((IParticleData) ParticleTypes.CAMPFIRE_COSY_SMOKE, v.x, v.y, v.z, 0.0, 0.0, 0.0);
        }
    }

    private void aoeBreak() {
        final Entity shooter = this.getOwner();
        if (!(shooter instanceof ServerPlayerEntity)) {
            return;
        }
        final ServerPlayerEntity player = (ServerPlayerEntity) shooter;
        final World world = this.getCommandSenderWorld();
        final float vel = (float) this.getDeltaMovement().length();
        for (final BlockPos offset : BlockHelper.getSphericalPositions(this.blockPosition(), Math.max(4.5f, 4.5f * vel))) {
            if (this.breakCount >= this.maxBreakCount) {
                break;
            }
            final BlockState state = world.getBlockState(offset);
            if (state.isAir((IBlockReader) world, offset) || (state.requiresCorrectToolForDrops() && state.getHarvestLevel() > 2)) {
                continue;
            }
            final float hardness = state.getDestroySpeed((IBlockReader) world, offset);
            if (hardness < 0.0f || hardness > 25.0f || !this.destroyBlock(world, offset, state, player)) {
                continue;
            }
            ++this.breakCount;
        }
    }

    private boolean destroyBlock(final World world, final BlockPos pos, final BlockState state, final ServerPlayerEntity player) {
        final ItemStack miningItem = new ItemStack((IItemProvider) Items.DIAMOND_PICKAXE);
        Block.dropResources(world.getBlockState(pos), world, pos, world.getBlockEntity(pos), (Entity) null, miningItem);
        return state.removedByPlayer(world, pos, (PlayerEntity) player, true, state.getFluidState());
    }

    protected void onHit(final RayTraceResult result) {
        if (result instanceof BlockRayTraceResult && this.breakCount < this.maxBreakCount && !this.getCommandSenderWorld().isClientSide()) {
            this.aoeBreak();
        }
        if (this.breakCount >= this.maxBreakCount) {
            this.doBreak = false;
            super.onHit(result);
        }
    }

    public void readAdditionalSaveData(final CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        this.doBreak = compound.getBoolean("break");
        this.breakCount = compound.getInt("breakCount");
        this.maxBreakCount = compound.getInt("maxBreakCount");
    }

    public void addAdditionalSaveData(final CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("break", this.doBreak);
        compound.putInt("breakCount", this.breakCount);
        compound.putInt("maxBreakCount", this.maxBreakCount);
    }

    public IPacket<?> getAddEntityPacket() {
        return (IPacket<?>) NetworkHooks.getEntitySpawningPacket((Entity) this);
    }
}
