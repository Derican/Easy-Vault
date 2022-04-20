package iskallia.vault.world.vault.logic.objective.architect.modifier;

import com.google.gson.annotations.Expose;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.architect.ArchitectObjective;
import iskallia.vault.world.vault.logic.objective.architect.processor.VaultPieceProcessor;
import iskallia.vault.world.vault.logic.objective.architect.processor.VaultSpawnerSpawningPostProcessor;

import javax.annotation.Nullable;

public class MobSpawnModifier extends VoteModifier {
    @Expose
    private final int blocksPerSpawn;

    public MobSpawnModifier(final String name, final String description, final int voteLockDurationChangeSeconds, final int blocksPerSpawn) {
        super(name, description, voteLockDurationChangeSeconds);
        this.blocksPerSpawn = blocksPerSpawn;
    }

    @Nullable
    @Override
    public VaultPieceProcessor getPostProcessor(final ArchitectObjective objective, final VaultRaid vault) {
        return new VaultSpawnerSpawningPostProcessor(this.blocksPerSpawn);
    }
}
