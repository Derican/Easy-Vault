// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.modifier;

import com.google.gson.annotations.Expose;
import iskallia.vault.world.data.PhoenixModifierSnapshotData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class InventoryRestoreModifier extends TexturedVaultModifier
{
    @Expose
    private final boolean preventsArtifact;
    
    public InventoryRestoreModifier(final String name, final ResourceLocation icon, final boolean preventsArtifact) {
        super(name, icon);
        this.preventsArtifact = preventsArtifact;
    }
    
    public boolean preventsArtifact() {
        return this.preventsArtifact;
    }
    
    @Override
    public void apply(final VaultRaid vault, final VaultPlayer player, final ServerWorld world, final Random random) {
        player.runIfPresent(world.getServer(), sPlayer -> {
            final PhoenixModifierSnapshotData snapshotData = PhoenixModifierSnapshotData.get(world);
            if (snapshotData.hasSnapshot((PlayerEntity)sPlayer)) {
                snapshotData.removeSnapshot((PlayerEntity)sPlayer);
            }
            snapshotData.createSnapshot((PlayerEntity)sPlayer);
        });
    }
    
    @Override
    public void remove(final VaultRaid vault, final VaultPlayer player, final ServerWorld world, final Random random) {
        final PhoenixModifierSnapshotData snapshotData = PhoenixModifierSnapshotData.get(world);
        if (snapshotData.hasSnapshot(player.getPlayerId())) {
            snapshotData.removeSnapshot(player.getPlayerId());
        }
    }
}
