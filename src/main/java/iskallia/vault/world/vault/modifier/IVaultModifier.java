package iskallia.vault.world.vault.modifier;

import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public interface IVaultModifier {
    void apply(VaultRaid paramVaultRaid, VaultPlayer paramVaultPlayer, ServerWorld paramServerWorld, Random paramRandom);

    void remove(VaultRaid paramVaultRaid, VaultPlayer paramVaultPlayer, ServerWorld paramServerWorld, Random paramRandom);

    void tick(VaultRaid paramVaultRaid, VaultPlayer paramVaultPlayer, ServerWorld paramServerWorld);
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\modifier\IVaultModifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */