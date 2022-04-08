package iskallia.vault.world.vault.modifier;

import com.google.gson.annotations.Expose;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public abstract class VaultModifier
        implements IVaultModifier {
    @Expose
    private final String name;
    protected static final Random rand = new Random();

    @Expose
    private String color = String.valueOf(65535);
    @Expose
    private String description = "This is a description.";

    public VaultModifier(String name) {
        this.name = name;
    }

    public VaultModifier format(int color, String description) {
        this.color = String.valueOf(color);
        this.description = description;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public int getColor() {
        return Integer.parseInt(this.color);
    }

    public ITextComponent getNameComponent() {
        HoverEvent hover = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent(getDescription()));
        return (ITextComponent) (new StringTextComponent(getName()))
                .setStyle(Style.EMPTY.withColor(Color.fromRgb(getColor())).withHoverEvent(hover));
    }

    public String getDescription() {
        return this.description;
    }


    public void apply(VaultRaid vault, VaultPlayer player, ServerWorld world, Random random) {
    }


    public void remove(VaultRaid vault, VaultPlayer player, ServerWorld world, Random random) {
    }


    public void tick(VaultRaid vault, VaultPlayer player, ServerWorld world) {
    }

    public static String migrateModifierName(String modifier) {
        if (modifier.equalsIgnoreCase("Slow"))
            return "Freezing";
        if (modifier.equalsIgnoreCase("Poison"))
            return "Poisonous";
        if (modifier.equalsIgnoreCase("Wither"))
            return "Withering";
        if (modifier.equalsIgnoreCase("Chilling") || modifier.equalsIgnoreCase("Chaining")) {
            return "Fatiguing";
        }
        return modifier;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\modifier\VaultModifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */