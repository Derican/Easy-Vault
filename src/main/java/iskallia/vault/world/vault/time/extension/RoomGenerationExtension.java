// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.time.extension;

import iskallia.vault.Vault;
import net.minecraft.util.ResourceLocation;

public class RoomGenerationExtension extends TimeExtension
{
    public static final ResourceLocation ID;
    
    public RoomGenerationExtension() {
    }
    
    public RoomGenerationExtension(final int value) {
        super(RoomGenerationExtension.ID, value);
    }
    
    static {
        ID = Vault.id("room_generation");
    }
}
