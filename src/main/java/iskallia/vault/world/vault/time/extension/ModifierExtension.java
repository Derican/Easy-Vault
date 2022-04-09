// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.time.extension;

import iskallia.vault.Vault;
import iskallia.vault.world.vault.modifier.TimerModifier;
import net.minecraft.util.ResourceLocation;

public class ModifierExtension extends TimeExtension
{
    public static final ResourceLocation ID;
    
    public ModifierExtension() {
    }
    
    public ModifierExtension(final int addedTime) {
        super(ModifierExtension.ID, addedTime);
    }
    
    public ModifierExtension(final TimerModifier modifier) {
        this(modifier.getTimerAddend());
    }
    
    public ModifierExtension(final ResourceLocation id, final TimerModifier modifier) {
        super(id, modifier.getTimerAddend());
    }
    
    static {
        ID = Vault.id("modifier");
    }
}
