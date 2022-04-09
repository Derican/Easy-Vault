// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.modifier;

import com.google.gson.annotations.Expose;
import net.minecraft.util.ResourceLocation;

public class LevelModifier extends TexturedVaultModifier
{
    @Expose
    private final int levelAddend;
    
    public LevelModifier(final String name, final ResourceLocation icon, final int levelAddend) {
        super(name, icon);
        this.levelAddend = levelAddend;
        if (this.levelAddend > 0) {
            this.format(this.getColor(), "Pushes the vault " + this.levelAddend + ((this.levelAddend == 1) ? " level higher." : " levels higher."));
        }
        else if (this.levelAddend < 0) {
            this.format(this.getColor(), "Pushes the vault " + -this.levelAddend + ((-this.levelAddend == 1) ? " level lower." : " levels lower."));
        }
        else {
            this.format(this.getColor(), "Does nothing at all. A bit of a waste of a modifier...");
        }
    }
    
    public int getLevelAddend() {
        return this.levelAddend;
    }
}
