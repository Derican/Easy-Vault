package iskallia.vault.block.property;

import net.minecraft.state.IntegerProperty;

public class HiddenIntegerProperty
        extends IntegerProperty {
    protected HiddenIntegerProperty(String name, int min, int max) {
        super(name, min, max);
    }

    public static IntegerProperty create(String name, int min, int max) {
        return new HiddenIntegerProperty(name, min, max);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\property\HiddenIntegerProperty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */