package iskallia.vault.world.vault.chest;

import com.google.gson.annotations.Expose;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.VaultSpawner;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.world.server.ServerWorld;

public class MobTrapEffect
        extends VaultChestEffect {
    @Expose
    private final int attempts;

    public MobTrapEffect(String name, int attempts, VaultSpawner.Config appliedConfig) {
        super(name);
        this.attempts = attempts;
        this.appliedConfig = appliedConfig;
    }

    @Expose
    private final VaultSpawner.Config appliedConfig;

    public int getAttempts() {
        return this.attempts;
    }

    public VaultSpawner.Config getAppliedConfig() {
        return this.appliedConfig;
    }


    public void apply(VaultRaid vault, VaultPlayer player, ServerWorld world) {
        player.getProperties().getBase(VaultRaid.SPAWNER).ifPresent(spawner -> {
            VaultSpawner.Config oldConfig = spawner.getConfig();
            spawner.configure(getAppliedConfig());
            for (int i = 0; i < getAttempts(); i++)
                spawner.execute(vault, player, world);
            spawner.configure(oldConfig);
        });
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\chest\MobTrapEffect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */