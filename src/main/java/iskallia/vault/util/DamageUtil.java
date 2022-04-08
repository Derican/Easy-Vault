package iskallia.vault.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

import java.util.function.Consumer;
import java.util.function.Function;


public class DamageUtil {
    public static <T extends Entity> void shotgunAttack(T e, Consumer<T> attackFn) {
        shotgunAttackApply(e, entity -> {
            attackFn.accept(entity);
            return null;
        });
    }

    public static <T extends Entity, R> R shotgunAttackApply(T e, Function<T, R> attackFn) {
        int prevHurtTicks = ((Entity) e).invulnerableTime;
        if (e instanceof LivingEntity) {
            LivingEntity le = (LivingEntity) e;
            float prevDamage = le.lastHurt;
            ((Entity) e).invulnerableTime = 0;
            le.lastHurt = 0.0F;
            try {
                return attackFn.apply(e);
            } finally {
                ((Entity) e).invulnerableTime = prevHurtTicks;
                le.lastHurt = prevDamage;
            }
        }
        ((Entity) e).invulnerableTime = 0;
        try {
            return attackFn.apply(e);
        } finally {
            ((Entity) e).invulnerableTime = prevHurtTicks;
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\DamageUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */