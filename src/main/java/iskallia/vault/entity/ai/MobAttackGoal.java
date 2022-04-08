package iskallia.vault.entity.ai;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;

public class MobAttackGoal
        extends MeleeAttackGoal {
    public MobAttackGoal(CreatureEntity creature, double speedIn, boolean useLongMemory) {
        super(creature, speedIn, useLongMemory);
    }

    protected double getAttackReachSqr(LivingEntity attackTarget) {
        return (4.0F + attackTarget.getBbWidth());
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\entity\ai\MobAttackGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */