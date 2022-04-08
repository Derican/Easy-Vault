package iskallia.vault.entity.ai;

import net.minecraft.entity.LivingEntity;


public class RegenAfterAWhile<T extends LivingEntity> {
    private final int startTicksUntilRegen;
    private final int ticksUntilPulse;
    private final float regenPercentage;
    public T entity;
    public int ticksUntilRegen;
    public int ticksUntilNextPulse;

    public RegenAfterAWhile(T entity) {
        this(entity, 90, 10, 0.05F);
    }

    public RegenAfterAWhile(T entity, int startTicksUntilRegen, int ticksUntilPulse, float regenPercentage) {
        this.entity = entity;
        this.startTicksUntilRegen = startTicksUntilRegen;
        this.ticksUntilPulse = ticksUntilPulse;
        this.regenPercentage = regenPercentage;
    }

    private void resetTicks() {
        this.ticksUntilRegen = this.startTicksUntilRegen;
        resetPulseTicks();
    }

    private void resetPulseTicks() {
        this.ticksUntilNextPulse = this.ticksUntilPulse;
    }

    public void onDamageTaken() {
        resetTicks();
    }

    public void tick() {
        if (this.ticksUntilRegen <= 0) {
            if (this.ticksUntilNextPulse <= 0) {
                float maxHealth = this.entity.getMaxHealth();
                float currentHealth = this.entity.getHealth();

                this.entity.setHealth(
                        Math.min(maxHealth, currentHealth + maxHealth * this.regenPercentage));


                resetPulseTicks();
            } else {
                this.ticksUntilNextPulse--;
            }
        } else {

            this.ticksUntilRegen--;
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\entity\ai\RegenAfterAWhile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */