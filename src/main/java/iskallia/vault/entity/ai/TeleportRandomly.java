package iskallia.vault.entity.ai;

import iskallia.vault.init.ModSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.util.INBTSerializable;

public class TeleportRandomly<T extends LivingEntity> implements INBTSerializable<CompoundNBT> {
    protected T entity;
    private final Condition<T>[] conditions;

    public TeleportRandomly(T entity) {
        this(entity, (Condition<T>[]) new Condition[0]);
    }

    public TeleportRandomly(T entity, Condition<T>... conditions) {
        this.entity = entity;
        this.conditions = conditions;
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        for (Condition<T> condition : this.conditions) {
            double chance = condition.getChance(this.entity, source, amount);

            if (((LivingEntity) this.entity).level.random.nextDouble() < chance) {
                for (int i = 0; i < 64; i++) {
                    if (teleportRandomly()) {

                        ((LivingEntity) this.entity).level.playSound(null, ((LivingEntity) this.entity).xo, ((LivingEntity) this.entity).yo, ((LivingEntity) this.entity).zo, ModSounds.BOSS_TP_SFX, this.entity
                                .getSoundSource(), 1.0F, 1.0F);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean teleportRandomly() {
        if (!((LivingEntity) this.entity).level.isClientSide() && this.entity.isAlive()) {
            double d0 = this.entity.getX() + (((LivingEntity) this.entity).level.random.nextDouble() - 0.5D) * 64.0D;
            double d1 = this.entity.getY() + (((LivingEntity) this.entity).level.random.nextInt(64) - 32);
            double d2 = this.entity.getZ() + (((LivingEntity) this.entity).level.random.nextDouble() - 0.5D) * 64.0D;
            return this.entity.randomTeleport(d0, d1, d2, true);
        }

        return false;
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        return nbt;
    }


    public void deserializeNBT(CompoundNBT nbt) {
    }


    public static <T extends LivingEntity> TeleportRandomly<T> fromNBT(T entity, CompoundNBT nbt) {
        TeleportRandomly<T> tp = new TeleportRandomly<>(entity);
        tp.deserializeNBT(nbt);
        return tp;
    }

    @FunctionalInterface
    public static interface Condition<T extends LivingEntity> {
        double getChance(T param1T, DamageSource param1DamageSource, double param1Double);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\entity\ai\TeleportRandomly.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */