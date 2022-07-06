package iskallia.vault.entity.ai.eyesore;

import iskallia.vault.entity.EyesoreEntity;
import net.minecraft.entity.ai.goal.Goal;

public class GiveBirthGoal extends Goal
{
    private final EyesoreEntity eyesore;
    private int birthTicks;
    
    public GiveBirthGoal(final EyesoreEntity eyesore) {
        this.eyesore = eyesore;
    }
    
    public boolean canUse() {
        return this.eyesore.getState() == EyesoreEntity.State.NORMAL;
    }
    
    public boolean canContinueToUse() {
        return this.birthTicks > 0;
    }
    
    public boolean isInterruptable() {
        return false;
    }
    
    public void start() {
        this.eyesore.setState(EyesoreEntity.State.GIVING_BIRTH);
        this.birthTicks = 200;
    }
    
    public void tick() {
        super.tick();
        if (this.birthTicks % 20 == 0) {
            System.out.println("Give birth to one random lil dude here!");
        }
        --this.birthTicks;
        if (this.birthTicks <= 0) {
            this.eyesore.setState(EyesoreEntity.State.NORMAL);
        }
    }
}
