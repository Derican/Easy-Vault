package iskallia.vault.world.vault.time.extension;

import iskallia.vault.Vault;
import iskallia.vault.world.vault.time.VaultTimer;
import net.minecraft.util.ResourceLocation;

public class WinExtension
        extends TimeExtension {
    public static final ResourceLocation ID = Vault.id("win");


    public WinExtension() {
    }


    public WinExtension(VaultTimer timer, int target) {
        this(ID, timer, target);
    }

    public WinExtension(ResourceLocation id, VaultTimer timer, int target) {
        super(id, -(timer.getTimeLeft() - target));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\time\extension\WinExtension.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */