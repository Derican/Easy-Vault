// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.attribute;

import net.minecraft.util.ResourceLocation;

import java.util.function.Supplier;

public abstract class VModifier<T, I extends VAttribute.Instance<T>> extends VAttribute<T, I>
{
    public VModifier(final ResourceLocation id, final Supplier<I> instance) {
        super(id, instance);
    }
    
    @Override
    protected String getTagKey() {
        return "Modifiers";
    }
    
    public abstract T apply(final Instance<T> p0, final T p1);
}
