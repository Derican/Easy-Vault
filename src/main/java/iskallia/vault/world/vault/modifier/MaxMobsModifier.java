package iskallia.vault.world.vault.modifier;

import com.google.gson.annotations.Expose;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.VaultSpawner;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class MaxMobsModifier extends TexturedVaultModifier {
    @Expose
    private final int maxMobsAddend;

    public MaxMobsModifier(final String name, final ResourceLocation icon, final int maxMobsAddend) {
        super(name, icon);
        this.maxMobsAddend = maxMobsAddend;
        if (this.maxMobsAddend > 0) {
            this.format(this.getColor(), "Spawns " + this.maxMobsAddend + ((this.maxMobsAddend == 1) ? " more mob." : " more mobs."));
        } else if (this.maxMobsAddend < 0) {
            this.format(this.getColor(), "Spawns " + -this.maxMobsAddend + ((-this.maxMobsAddend == 1) ? " less mob." : " less mobs."));
        } else {
            this.format(this.getColor(), "Does nothing at all. A bit of a waste of a modifier...");
        }
    }

    @Override
    public void apply(final VaultRaid vault, final VaultPlayer player, final ServerWorld world, final Random random) {
        player.getProperties().get(VaultRaid.SPAWNER).ifPresent(spawner -> {
            ((VaultSpawner) spawner.getBaseValue()).addMaxMobs(this.maxMobsAddend);
            spawner.updateNBT();
        });
    }

    @Override
    public void remove(final VaultRaid vault, final VaultPlayer player, final ServerWorld world, final Random random) {
        player.getProperties().get(VaultRaid.SPAWNER).ifPresent(spawner -> {
            ((VaultSpawner) spawner.getBaseValue()).addMaxMobs(-this.maxMobsAddend);
            spawner.updateNBT();
        });
    }
}
