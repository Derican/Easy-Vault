// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.block.property;

import net.minecraft.state.IntegerProperty;

public class HiddenIntegerProperty extends IntegerProperty
{
    protected HiddenIntegerProperty(final String name, final int min, final int max) {
        super(name, min, max);
    }
    
    public static IntegerProperty create(final String name, final int min, final int max) {
        return new HiddenIntegerProperty(name, min, max);
    }
}
