package iskallia.vault.skill.talent.type;

import com.google.gson.annotations.Expose;

public class SoulShardTalent extends PlayerTalent {
    @Expose
    protected final float additionalSoulShardChance;

    public SoulShardTalent(int cost, float additionalSoulShardChance) {
        super(cost);
        this.additionalSoulShardChance = additionalSoulShardChance;
    }

    public float getAdditionalSoulShardChance() {
        return this.additionalSoulShardChance;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\talent\type\SoulShardTalent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */