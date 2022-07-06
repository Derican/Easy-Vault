package iskallia.vault.entity.ai.eyesore;

import java.util.Random;
import iskallia.vault.util.data.WeightedList;
import iskallia.vault.entity.EyesoreEntity;

public class EyesoreBrain
{
    private final EyesoreEntity boss;
    public CreepyIdleTask creepyIdle;
    public WeightedList<EyesoreTask<EyesoreEntity>> tasks;
    public EyesoreTask<EyesoreEntity> activeTask;
    
    public EyesoreBrain(final EyesoreEntity boss) {
        this.tasks = new WeightedList<EyesoreTask<EyesoreEntity>>();
        this.boss = boss;
        this.creepyIdle = new CreepyIdleTask(boss);
        this.tasks.add(new BasicAttackTask<EyesoreEntity>(this.boss), 3);
        this.tasks.add(new LaserAttackTask(this.boss), 1);
    }
    
    public void tick() {
        if (!this.creepyIdle.isFinished()) {
            this.creepyIdle.tick();
            return;
        }
        if (this.activeTask == null || this.activeTask.isFinished()) {
            (this.activeTask = this.tasks.getRandom(new Random())).reset();
        }
        this.activeTask.tick();
    }
}
