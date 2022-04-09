// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.modifier;

import com.google.gson.annotations.Expose;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.player.VaultPlayer;
import iskallia.vault.world.vault.time.extension.ModifierExtension;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class TimerModifier extends TexturedVaultModifier
{
    @Expose
    private final int timerAddend;
    
    public TimerModifier(final String name, final ResourceLocation icon, final int timerAddend) {
        super(name, icon);
        this.timerAddend = timerAddend;
        if (this.timerAddend > 0) {
            this.format(this.getColor(), "Adds " + this.timerAddend / 20 + " seconds to the clock.");
        }
        else if (this.timerAddend < 0) {
            this.format(this.getColor(), "Removes " + -(this.timerAddend / 20) + " seconds from the clock.");
        }
        else {
            this.format(this.getColor(), "Does nothing at all. A bit of a waste of a modifier...");
        }
    }
    
    public int getTimerAddend() {
        return this.timerAddend;
    }
    
    @Override
    public void apply(final VaultRaid vault, final VaultPlayer player, final ServerWorld world, final Random random) {
        player.getTimer().addTime(new ModifierExtension(this.getTimerAddend()), 0);
    }
    
    @Override
    public void remove(final VaultRaid vault, final VaultPlayer player, final ServerWorld world, final Random random) {
        player.getTimer().addTime(new ModifierExtension(-this.getTimerAddend()), 0);
    }
}
