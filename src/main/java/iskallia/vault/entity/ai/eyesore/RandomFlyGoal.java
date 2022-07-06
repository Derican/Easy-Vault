// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.entity.ai.eyesore;

import java.util.Random;
import net.minecraft.entity.ai.controller.MovementController;
import java.util.EnumSet;
import iskallia.vault.entity.EyesoreEntity;
import net.minecraft.entity.ai.goal.Goal;

public class RandomFlyGoal extends Goal
{
    private final EyesoreEntity eyesore;
    
    public RandomFlyGoal(final EyesoreEntity eyesore) {
        this.eyesore = eyesore;
        this.setFlags((EnumSet)EnumSet.of(Goal.Flag.MOVE));
    }
    
    public boolean canUse() {
        final MovementController movementcontroller = this.eyesore.getMoveControl();
        if (!movementcontroller.hasWanted()) {
            return true;
        }
        final double d0 = movementcontroller.getWantedX() - this.eyesore.getX();
        final double d2 = movementcontroller.getWantedY() - this.eyesore.getY();
        final double d3 = movementcontroller.getWantedZ() - this.eyesore.getZ();
        final double d4 = d0 * d0 + d2 * d2 + d3 * d3;
        return d4 < 1.0 || d4 > 3600.0;
    }
    
    public boolean canContinueToUse() {
        return false;
    }
    
    public void start() {
        final Random random = this.eyesore.getRandom();
        final double d0 = this.eyesore.getX() + (random.nextFloat() * 2.0f - 1.0f) * 16.0f;
        final double d2 = this.eyesore.getY() + (random.nextFloat() * 2.0f - 1.0f) * 16.0f;
        final double d3 = this.eyesore.getZ() + (random.nextFloat() * 2.0f - 1.0f) * 16.0f;
        this.eyesore.getMoveControl().setWantedPosition(d0, d2, d3, 1.0);
    }
}
