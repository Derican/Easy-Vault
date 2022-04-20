package iskallia.vault.entity.ai;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;

public class MobAttackGoal extends MeleeAttackGoal {
    public MobAttackGoal(final CreatureEntity creature, final double speedIn, final boolean useLongMemory) {
        super(creature, speedIn, useLongMemory);
    }

    protected double getAttackReachSqr(final LivingEntity attackTarget) {
        return 4.0f + attackTarget.getBbWidth();
    }
}
