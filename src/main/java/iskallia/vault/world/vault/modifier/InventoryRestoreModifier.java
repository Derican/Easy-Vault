package iskallia.vault.world.vault.modifier;

import com.google.gson.annotations.Expose;
import iskallia.vault.world.data.PhoenixModifierSnapshotData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class InventoryRestoreModifier
        extends TexturedVaultModifier {
    public InventoryRestoreModifier(String name, ResourceLocation icon, boolean preventsArtifact) {
        super(name, icon);
        this.preventsArtifact = preventsArtifact;
    }

    @Expose
    private final boolean preventsArtifact;

    public boolean preventsArtifact() {
        return this.preventsArtifact;
    }


    public void apply(VaultRaid vault, VaultPlayer player, ServerWorld world, Random random) {
        player.runIfPresent(world.getServer(), sPlayer -> {
            PhoenixModifierSnapshotData snapshotData = PhoenixModifierSnapshotData.get(world);
            if (snapshotData.hasSnapshot((PlayerEntity) sPlayer)) {
                snapshotData.removeSnapshot((PlayerEntity) sPlayer);
            }
            snapshotData.createSnapshot((PlayerEntity) sPlayer);
        });
    }


    public void remove(VaultRaid vault, VaultPlayer player, ServerWorld world, Random random) {
        PhoenixModifierSnapshotData snapshotData = PhoenixModifierSnapshotData.get(world);
        if (snapshotData.hasSnapshot(player.getPlayerId()))
            snapshotData.removeSnapshot(player.getPlayerId());
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\modifier\InventoryRestoreModifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */