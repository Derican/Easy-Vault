package iskallia.vault.item.catalyst;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.util.function.Function;

public enum ModifierRollType {
    ADD_SPECIFIC_MODIFIER(Function.identity()),
    ADD_RANDOM_MODIFIER(cmp -> new StringTextComponent("A random ").append(cmp).append(" Modifier"));

    private final Function<ITextComponent, ITextComponent> formatter;

    private ModifierRollType(final Function<ITextComponent, ITextComponent> formatter) {
        this.formatter = formatter;
    }

    public ITextComponent getDescription(final ITextComponent name) {
        return this.formatter.apply(name);
    }
}
