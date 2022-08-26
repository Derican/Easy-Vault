package iskallia.vault.skill.talent.type;

import com.google.gson.annotations.Expose;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Random;

public abstract class PlayerTalent {
    protected static final Random rand;
    @Expose
    private final int cost;
    @Expose
    private int levelRequirement;

    public PlayerTalent(final int cost) {
        this(cost, 0);
    }

    public PlayerTalent(final int cost, final int levelRequirement) {
        this.levelRequirement = 0;
        this.cost = cost;
        this.levelRequirement = levelRequirement;
    }

    public int getCost() {
        return this.cost;
    }

    public int getLevelRequirement() {
        return this.levelRequirement;
    }

    public void onAdded(final PlayerEntity player) {
    }

    public void tick(final PlayerEntity player) {
    }

    public void onRemoved(final PlayerEntity player) {
    }

    static {
        rand = new Random();
    }
}
