package iskallia.vault.world.vault.logic.objective.architect.modifier;

import iskallia.vault.world.vault.logic.objective.architect.ArchitectSummonAndKillBossesObjective;
import net.minecraft.world.server.ServerWorld;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.architect.ArchitectObjective;
import com.google.gson.annotations.Expose;

public class FinalKnowledgeModifier extends VoteModifier {
    @Expose
    private final int knowledge;

    public FinalKnowledgeModifier(final String name, final String description, final int knowledge) {
        super(name, description, 0);
        this.knowledge = knowledge;
    }

    public FinalKnowledgeModifier(final String name, final String description, final String color, final int voteLockDurationChangeSeconds, final int knowledge) {
        super(name, description, color, voteLockDurationChangeSeconds);
        this.knowledge = knowledge;
    }

    @Override
    public void onApply(final ArchitectObjective objective, final VaultRaid vault, final ServerWorld world) {
        super.onApply(objective, vault, world);
        if (objective instanceof ArchitectSummonAndKillBossesObjective) {
            ((ArchitectSummonAndKillBossesObjective) objective).addKnowledge(this.knowledge);
        }
    }
}
