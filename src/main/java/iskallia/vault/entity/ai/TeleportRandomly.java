package iskallia.vault.entity.ai;

import iskallia.vault.init.ModSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.util.INBTSerializable;

public class TeleportRandomly<T extends LivingEntity> implements INBTSerializable<CompoundNBT> {
    protected T entity;
    private final Condition<T>[] conditions;

    public TeleportRandomly(final T entity) {
        this(entity, new Condition[0]);
    }

    public TeleportRandomly(final T entity, final Condition<T>... conditions) {
        this.entity = entity;
        this.conditions = conditions;
    }

    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        for (final Condition<T> condition : this.conditions) {
            final double chance = condition.getChance(this.entity, source, amount);
            if (this.entity.level.random.nextDouble() < chance) {
                for (int i = 0; i < 64; ++i) {
                    if (this.teleportRandomly()) {
                        this.entity.level.playSound(null, this.entity.xo, this.entity.yo, this.entity.zo, ModSounds.BOSS_TP_SFX, this.entity.getSoundSource(), 1.0f, 1.0f);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean teleportRandomly() {
        if (!this.entity.level.isClientSide() && this.entity.isAlive()) {
            final double d0 = this.entity.getX() + (this.entity.level.random.nextDouble() - 0.5) * 64.0;
            final double d2 = this.entity.getY() + (this.entity.level.random.nextInt(64) - 32);
            final double d3 = this.entity.getZ() + (this.entity.level.random.nextDouble() - 0.5) * 64.0;
            return this.entity.randomTeleport(d0, d2, d3, true);
        }
        return false;
    }

    public CompoundNBT serializeNBT() {
        final CompoundNBT nbt = new CompoundNBT();
        return nbt;
    }

    public void deserializeNBT(final CompoundNBT nbt) {
    }

    public static <T extends LivingEntity> TeleportRandomly<T> fromNBT(final T entity, final CompoundNBT nbt) {
        final TeleportRandomly<T> tp = new TeleportRandomly<T>(entity);
        tp.deserializeNBT(nbt);
        return tp;
    }

    @FunctionalInterface
    public interface Condition<T extends LivingEntity> {
        double getChance(final T p0, final DamageSource p1, final double p2);
    }
}
