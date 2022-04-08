package iskallia.vault.world.vault.modifier;

import com.google.gson.annotations.Expose;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.player.VaultPlayer;
import iskallia.vault.world.vault.time.extension.ModifierExtension;
import iskallia.vault.world.vault.time.extension.TimeExtension;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class TimerModifier extends TexturedVaultModifier {
    @Expose
    private final int timerAddend;

    public TimerModifier(String name, ResourceLocation icon, int timerAddend) {
        super(name, icon);
        this.timerAddend = timerAddend;

        if (this.timerAddend > 0) {
            format(getColor(), "Adds " + (this.timerAddend / 20) + " seconds to the clock.");
        } else if (this.timerAddend < 0) {
            format(getColor(), "Removes " + -(this.timerAddend / 20) + " seconds from the clock.");
        } else {
            format(getColor(), "Does nothing at all. A bit of a waste of a modifier...");
        }
    }

    public int getTimerAddend() {
        return this.timerAddend;
    }


    public void apply(VaultRaid vault, VaultPlayer player, ServerWorld world, Random random) {
        player.getTimer().addTime((TimeExtension) new ModifierExtension(getTimerAddend()), 0);
    }


    public void remove(VaultRaid vault, VaultPlayer player, ServerWorld world, Random random) {
        player.getTimer().addTime((TimeExtension) new ModifierExtension(-getTimerAddend()), 0);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\modifier\TimerModifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */