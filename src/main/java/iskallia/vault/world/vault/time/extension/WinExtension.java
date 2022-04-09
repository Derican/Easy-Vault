// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.time.extension;

import iskallia.vault.Vault;
import iskallia.vault.world.vault.time.VaultTimer;
import net.minecraft.util.ResourceLocation;

public class WinExtension extends TimeExtension
{
    public static final ResourceLocation ID;
    
    public WinExtension() {
    }
    
    public WinExtension(final VaultTimer timer, final int target) {
        this(WinExtension.ID, timer, target);
    }
    
    public WinExtension(final ResourceLocation id, final VaultTimer timer, final int target) {
        super(id, -(timer.getTimeLeft() - target));
    }
    
    static {
        ID = Vault.id("win");
    }
}
