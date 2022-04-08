package iskallia.vault.world.vault.modifier;

import com.google.gson.annotations.Expose;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.VaultSpawner;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class MaxMobsModifier
        extends TexturedVaultModifier {
    public MaxMobsModifier(String name, ResourceLocation icon, int maxMobsAddend) {
        super(name, icon);
        this.maxMobsAddend = maxMobsAddend;

        if (this.maxMobsAddend > 0) {
            format(getColor(), "Spawns " + this.maxMobsAddend + ((this.maxMobsAddend == 1) ? " more mob." : " more mobs."));
        } else if (this.maxMobsAddend < 0) {
            format(getColor(), "Spawns " + -this.maxMobsAddend + ((-this.maxMobsAddend == 1) ? " less mob." : " less mobs."));
        } else {
            format(getColor(), "Does nothing at all. A bit of a waste of a modifier...");
        }
    }

    @Expose
    private final int maxMobsAddend;

    public void apply(VaultRaid vault, VaultPlayer player, ServerWorld world, Random random) {
        player.getProperties().get(VaultRaid.SPAWNER).ifPresent(spawner -> {
            ((VaultSpawner) spawner.getBaseValue()).addMaxMobs(this.maxMobsAddend);
            spawner.updateNBT();
        });
    }


    public void remove(VaultRaid vault, VaultPlayer player, ServerWorld world, Random random) {
        player.getProperties().get(VaultRaid.SPAWNER).ifPresent(spawner -> {
            ((VaultSpawner) spawner.getBaseValue()).addMaxMobs(-this.maxMobsAddend);
            spawner.updateNBT();
        });
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\modifier\MaxMobsModifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */