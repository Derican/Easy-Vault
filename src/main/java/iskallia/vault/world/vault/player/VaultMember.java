// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.player;

import iskallia.vault.Vault;
import iskallia.vault.world.vault.time.VaultTimer;
import net.minecraft.world.server.ServerWorld;
import iskallia.vault.world.vault.VaultRaid;
import java.util.UUID;
import net.minecraft.util.ResourceLocation;

public class VaultMember extends VaultPlayer
{
    public static final ResourceLocation ID;
    
    public VaultMember() {
    }
    
    public VaultMember(final UUID playerId) {
        this(VaultMember.ID, playerId);
    }
    
    public VaultMember(final ResourceLocation id, final UUID playerId) {
        super(id, playerId);
    }
    
    @Override
    public void tickTimer(final VaultRaid vault, final ServerWorld world, final VaultTimer timer) {
    }
    
    @Override
    public void tickObjectiveUpdates(final VaultRaid vault, final ServerWorld world) {
    }
    
    static {
        ID = Vault.id("member");
    }
}
