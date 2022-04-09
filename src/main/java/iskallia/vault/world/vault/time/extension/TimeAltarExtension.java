// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.time.extension;

import iskallia.vault.Vault;
import net.minecraft.util.ResourceLocation;

public class TimeAltarExtension extends TimeExtension
{
    public static final ResourceLocation ID;
    
    public TimeAltarExtension() {
    }
    
    public TimeAltarExtension(final int value) {
        super(TimeAltarExtension.ID, value);
    }
    
    static {
        ID = Vault.id("time_altar");
    }
}
