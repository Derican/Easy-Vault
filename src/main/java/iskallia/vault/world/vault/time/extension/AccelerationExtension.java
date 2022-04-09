// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.time.extension;

import iskallia.vault.Vault;
import net.minecraft.util.ResourceLocation;

public class AccelerationExtension extends TimeExtension
{
    public static final ResourceLocation ID;
    
    public AccelerationExtension() {
    }
    
    public AccelerationExtension(final int value) {
        super(AccelerationExtension.ID, value);
    }
    
    static {
        ID = Vault.id("acceleration");
    }
}
