package iskallia.vault.world.vault.logic.objective.architect.modifier;

import iskallia.vault.world.vault.logic.objective.architect.ArchitectSummonAndKillBossesObjective;
import net.minecraft.world.server.ServerWorld;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.architect.ArchitectObjective;
import com.google.gson.annotations.Expose;

public class FinalMobHealthModifier extends VoteModifier
{
    @Expose
    private final float healthIncrease;
    
    public FinalMobHealthModifier(final String name, final String description, final float healthIncrease) {
        super(name, description, 0);
        this.healthIncrease = healthIncrease;
    }

    public FinalMobHealthModifier(final String name, final String description, final String color, final int voteLockDurationChangeSeconds, final float healthIncrease) {
        super(name, description, color, voteLockDurationChangeSeconds);
        this.healthIncrease = healthIncrease;
    }
    
    @Override
    public void onApply(final ArchitectObjective objective, final VaultRaid vault, final ServerWorld world) {
        super.onApply(objective, vault, world);
        if (objective instanceof ArchitectSummonAndKillBossesObjective) {
            ((ArchitectSummonAndKillBossesObjective)objective).addMobHealthMultiplier(this.healthIncrease);
        }
    }
}
