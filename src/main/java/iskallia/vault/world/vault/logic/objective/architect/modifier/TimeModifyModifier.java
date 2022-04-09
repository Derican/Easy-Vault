// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.logic.objective.architect.modifier;

import com.google.gson.annotations.Expose;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.architect.ArchitectObjective;
import iskallia.vault.world.vault.time.extension.RoomGenerationExtension;
import net.minecraft.world.server.ServerWorld;

public class TimeModifyModifier extends VoteModifier
{
    @Expose
    private final int timeChange;
    
    public TimeModifyModifier(final String name, final String description, final int voteLockDurationChangeSeconds, final int timeChange) {
        super(name, description, voteLockDurationChangeSeconds);
        this.timeChange = timeChange;
    }
    
    @Override
    public void onApply(final ArchitectObjective objective, final VaultRaid vault, final ServerWorld world) {
        super.onApply(objective, vault, world);
        vault.getPlayers().forEach(vPlayer -> vPlayer.getTimer().addTime(new RoomGenerationExtension(this.timeChange), 0));
    }
}
