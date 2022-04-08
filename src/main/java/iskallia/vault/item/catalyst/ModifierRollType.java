package iskallia.vault.item.catalyst;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.util.function.Function;

public enum ModifierRollType {
    ADD_RANDOM_MODIFIER,
    ADD_SPECIFIC_MODIFIER(Function.identity());

    static {
        ADD_RANDOM_MODIFIER = new ModifierRollType("ADD_RANDOM_MODIFIER", 1, cmp -> (new StringTextComponent("A random ")).append(cmp).append(" Modifier"));
    }

    private final Function<ITextComponent, ITextComponent> formatter;

    ModifierRollType(Function<ITextComponent, ITextComponent> formatter) {
        this.formatter = formatter;
    }

    public ITextComponent getDescription(ITextComponent name) {
        return this.formatter.apply(name);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\catalyst\ModifierRollType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */