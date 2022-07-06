package iskallia.vault.entity.ai.eyesore;

import iskallia.vault.world.vault.gen.piece.VaultPiece;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import iskallia.vault.init.ModConfigs;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Vector3d;
import iskallia.vault.world.vault.gen.piece.FinalVaultBoss;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Optional;
import iskallia.vault.world.vault.player.VaultRunner;
import net.minecraft.entity.player.ServerPlayerEntity;
import java.util.List;
import net.minecraft.entity.LivingEntity;
import java.util.UUID;
import iskallia.vault.entity.EyesoreEntity;

public class LaserAttackTask extends EyesoreTask<EyesoreEntity>
{
    public int tick;
    public UUID target;
    
    public LaserAttackTask(final EyesoreEntity entity) {
        super(entity);
        this.tick = 0;
    }
    
    @Override
    public void tick() {
        if (this.isFinished()) {
            return;
        }
        if (this.target == null) {
            final ServerPlayerEntity player;
            final List<ServerPlayerEntity> players = this.getVault().getPlayers().stream().filter(player1 -> player1 instanceof VaultRunner).map(p -> p.getServerPlayer(this.getWorld().getServer())).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
            if (players.size() == 0) {
                return;
            }
            player = players.get(this.getRandom().nextInt(players.size()));
            this.target = player.getUUID();
            this.getEntity().getEntityData().set((DataParameter)EyesoreEntity.LASER_TARGET, (Object)Optional.of(player.getUUID()));
            this.getVault().getPlayers().stream().map(p -> p.getServerPlayer(this.getWorld().getServer())).filter(Optional::isPresent).map(Optional::get).forEach(p -> this.getWorld().playSound((PlayerEntity)null, p.getX(), p.getY(), p.getZ(), SoundEvents.ELDER_GUARDIAN_CURSE, SoundCategory.HOSTILE, 10.0f, 1.0f));
        }
        final Entity entity = this.getWorld().getEntity(this.target);
        final LivingEntity targetEntity = (entity instanceof LivingEntity) ? (LivingEntity) entity : null;
        if (targetEntity != null) {
            final double distance = this.getEntity().blockPosition().distSqr((Vector3i)targetEntity.blockPosition());
            final Optional<VaultPiece> finalVault = this.getVault().getGenerator().getPieces(FinalVaultBoss.class).stream().findFirst();
            if (finalVault.isPresent()) {
                final Vector3i center = finalVault.get().getCenter();
                this.getEntity().path.stayInRange(new Vector3d((double)center.getX(), (double)center.getY(), (double)center.getZ()), (Entity)targetEntity, 0.15, 30.0, 2.0);
            }
            else {
                this.getEntity().path.stayInRange(this.getEntity(), (Entity)targetEntity, 0.15, 30.0, 2.0);
            }
            final Vector3d eyePos1 = this.getEntity().getEyePosition(1.0f);
            final Vector3d eyePos2 = this.getPosition((Entity)targetEntity);
            final RayTraceContext context = new RayTraceContext(eyePos1, eyePos2, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, (this).getEntity());
            final BlockRayTraceResult result = this.getWorld().clip(context);
            if (result.getType() == RayTraceResult.Type.MISS) {
                targetEntity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 60, 2, false, false));
                final DamageSource source = new EntityDamageSource("laser", (this).getEntity()).setMagic();
                final float damage = ModConfigs.EYESORE.laserAttack.getDamage(this.getEntity(), this.tick);
                if (damage > 0.0f) {
                    targetEntity.hurt(source, damage);
                }
                if (this.getWorld().getGameTime() % 10L == 0L) {
                    this.getWorld().sendParticles((IParticleData)ParticleTypes.SMOKE, targetEntity.getX(), targetEntity.getY(), targetEntity.getZ(), 300, 0.0, 0.0, 0.0, 0.001);
                }
            }
            else {
                if (this.getWorld().getGameTime() % 10L == 0L) {
                    this.getWorld().destroyBlock(result.getBlockPos(), true, (this).getEntity());
                }
                if (this.getWorld().getGameTime() % 10L == 0L) {
                    this.getWorld().sendParticles((IParticleData)ParticleTypes.SMOKE, result.getLocation().x, result.getLocation().y, result.getLocation().z, 300, 0.0, 0.0, 0.0, 0.001);
                }
            }
        }
        ++this.tick;
        if (this.isFinished()) {
            this.getEntity().getEntityData().set((DataParameter)EyesoreEntity.LASER_TARGET, (Object)Optional.empty());
        }
    }
    
    protected void lookAtTarget(final LivingEntity target) {
        this.getEntity().xRot = this.getTargetPitch(target);
        this.getEntity().yHeadRot = this.getTargetYaw(target);
    }
    
    private double getEyePosition(final Entity entity) {
        return (entity instanceof LivingEntity) ? entity.getEyeY() : ((entity.getBoundingBox().minY + entity.getBoundingBox().maxY) / 2.0);
    }
    
    protected float getTargetPitch(final LivingEntity target) {
        final double d0 = target.getX() - this.getEntity().getX();
        final double d2 = this.getEyePosition((Entity)target) - this.getEntity().getEyeY();
        final double d3 = target.getZ() - this.getEntity().getZ();
        final double d4 = MathHelper.sqrt(d0 * d0 + d3 * d3);
        return (float)(-(MathHelper.atan2(d2, d4) * 57.2957763671875));
    }
    
    protected float getTargetYaw(final LivingEntity target) {
        final double d0 = target.getX() - this.getEntity().getX();
        final double d2 = target.getZ() - this.getEntity().getZ();
        return (float)(MathHelper.atan2(d2, d0) * 57.2957763671875) - 90.0f;
    }
    
    private Vector3d getPosition(final Entity entityLivingBaseIn) {
        final double d0 = entityLivingBaseIn.getX();
        final double d2 = entityLivingBaseIn.getY() + entityLivingBaseIn.getBbHeight() / 2.0f;
        final double d3 = entityLivingBaseIn.getZ();
        return new Vector3d(d0, d2, d3);
    }
    
    @Override
    public boolean isFinished() {
        return this.getVault() == null || this.tick >= 100;
    }
    
    @Override
    public void reset() {
        this.tick = 0;
        this.target = null;
    }
}
