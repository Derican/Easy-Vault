// 
// Decompiled by Procyon v0.6.0
// 

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

public abstract class VaultModifier implements IVaultModifier
{
    protected static final Random rand;
    @Expose
    private final String name;
    @Expose
    private String color;
    @Expose
    private String description;
    
    public VaultModifier(final String name) {
        this.color = String.valueOf(65535);
        this.description = "This is a description.";
        this.name = name;
    }
    
    public VaultModifier format(final int color, final String description) {
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
        final HoverEvent hover = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent(this.getDescription()));
        return (ITextComponent)new StringTextComponent(this.getName()).setStyle(Style.EMPTY.withColor(Color.fromRgb(this.getColor())).withHoverEvent(hover));
    }
    
    public String getDescription() {
        return this.description;
    }
    
    @Override
    public void apply(final VaultRaid vault, final VaultPlayer player, final ServerWorld world, final Random random) {
    }
    
    @Override
    public void remove(final VaultRaid vault, final VaultPlayer player, final ServerWorld world, final Random random) {
    }
    
    @Override
    public void tick(final VaultRaid vault, final VaultPlayer player, final ServerWorld world) {
    }
    
    public static String migrateModifierName(final String modifier) {
        if (modifier.equalsIgnoreCase("Slow")) {
            return "Freezing";
        }
        if (modifier.equalsIgnoreCase("Poison")) {
            return "Poisonous";
        }
        if (modifier.equalsIgnoreCase("Wither")) {
            return "Withering";
        }
        if (modifier.equalsIgnoreCase("Chilling") || modifier.equalsIgnoreCase("Chaining")) {
            return "Fatiguing";
        }
        return modifier;
    }
    
    static {
        rand = new Random();
    }
}
