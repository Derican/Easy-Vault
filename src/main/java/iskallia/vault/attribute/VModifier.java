package iskallia.vault.attribute;

import net.minecraft.util.ResourceLocation;

import java.util.function.Supplier;

public abstract class VModifier<T, I extends VAttribute.Instance<T>>
        extends VAttribute<T, I> {
    public VModifier(ResourceLocation id, Supplier<I> instance) {
        super(id, instance);
    }


    protected String getTagKey() {
        return "Modifiers";
    }

    public abstract T apply(VAttribute.Instance<T> paramInstance, T paramT);
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\attribute\VModifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */