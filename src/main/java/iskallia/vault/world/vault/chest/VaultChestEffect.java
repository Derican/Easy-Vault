package iskallia.vault.world.vault.chest;

import com.google.gson.annotations.Expose;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.world.server.ServerWorld;

public abstract class VaultChestEffect {
    @Expose
    private final String name;

    public VaultChestEffect(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public boolean isTrapEffect() {
        return true;
    }

    public abstract void apply(VaultRaid paramVaultRaid, VaultPlayer paramVaultPlayer, ServerWorld paramServerWorld);
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\chest\VaultChestEffect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */