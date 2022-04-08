package iskallia.vault.skill.talent.type;

import com.google.gson.annotations.Expose;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Random;


public abstract class PlayerTalent {
    protected static final Random rand = new Random();

    @Expose
    private int levelRequirement = 0;
    @Expose
    private int cost;

    public PlayerTalent(int cost) {
        this(cost, 0);
    }

    public PlayerTalent(int cost, int levelRequirement) {
        this.cost = cost;
        this.levelRequirement = levelRequirement;
    }

    public int getCost() {
        return this.cost;
    }

    public int getLevelRequirement() {
        return this.levelRequirement;
    }

    public void onAdded(PlayerEntity player) {
    }

    public void tick(PlayerEntity player) {
    }

    public void onRemoved(PlayerEntity player) {
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\talent\type\PlayerTalent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */