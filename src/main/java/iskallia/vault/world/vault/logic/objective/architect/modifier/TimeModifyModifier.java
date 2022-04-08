package iskallia.vault.world.vault.logic.objective.architect.modifier;

import com.google.gson.annotations.Expose;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.architect.ArchitectObjective;
import iskallia.vault.world.vault.time.extension.RoomGenerationExtension;
import iskallia.vault.world.vault.time.extension.TimeExtension;
import net.minecraft.world.server.ServerWorld;

public class TimeModifyModifier
        extends VoteModifier {
    public TimeModifyModifier(String name, String description, int voteLockDurationChangeSeconds, int timeChange) {
        super(name, description, voteLockDurationChangeSeconds);
        this.timeChange = timeChange;
    }

    @Expose
    private final int timeChange;

    public void onApply(ArchitectObjective objective, VaultRaid vault, ServerWorld world) {
        super.onApply(objective, vault, world);

        vault.getPlayers().forEach(vPlayer -> vPlayer.getTimer().addTime((TimeExtension) new RoomGenerationExtension(this.timeChange), 0));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\objective\architect\modifier\TimeModifyModifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */