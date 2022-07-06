// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.logic.objective.architect.modifier;

import iskallia.vault.world.vault.player.VaultPlayer;
import iskallia.vault.world.vault.time.extension.TimeExtension;
import iskallia.vault.world.vault.time.extension.RoomGenerationExtension;
import net.minecraft.world.server.ServerWorld;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.architect.ArchitectObjective;
import com.google.gson.annotations.Expose;

public class FinalVaultTimeModifier extends VoteModifier
{
    @Expose
    private final int timeChange;
    
    public FinalVaultTimeModifier(final String name, final String description, final int timeChange) {
        super(name, description, 0);
        this.timeChange = timeChange;
    }
    public FinalVaultTimeModifier(final String name, final String description, final String color, final int voteLockDurationChangeSeconds, final int timeChange) {
        super(name, description, color, voteLockDurationChangeSeconds);
        this.timeChange = timeChange;
    }
    @Override
    public void onApply(final ArchitectObjective objective, final VaultRaid vault, final ServerWorld world) {
        super.onApply(objective, vault, world);
        vault.getPlayers().forEach(vPlayer -> vPlayer.getTimer().addTime(new RoomGenerationExtension(this.timeChange * 20), 0));
    }
}
