package iskallia.vault.world.vault.logic.objective.architect.modifier;

import com.google.gson.annotations.Expose;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.architect.ArchitectObjective;
import iskallia.vault.world.vault.logic.objective.architect.processor.VaultPieceProcessor;
import iskallia.vault.world.vault.logic.objective.architect.processor.VaultSpawnerSpawningPostProcessor;

import javax.annotation.Nullable;

public class MobSpawnModifier
        extends VoteModifier {
    @Expose
    private final int blocksPerSpawn;

    public MobSpawnModifier(String name, String description, int voteLockDurationChangeSeconds, int blocksPerSpawn) {
        super(name, description, voteLockDurationChangeSeconds);
        this.blocksPerSpawn = blocksPerSpawn;
    }


    @Nullable
    public VaultPieceProcessor getPostProcessor(ArchitectObjective objective, VaultRaid vault) {
        return (VaultPieceProcessor) new VaultSpawnerSpawningPostProcessor(this.blocksPerSpawn);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\objective\architect\modifier\MobSpawnModifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */