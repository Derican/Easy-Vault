package iskallia.vault.world.vault.modifier;

import com.google.gson.annotations.Expose;
import net.minecraft.util.ResourceLocation;


public class LevelModifier
        extends TexturedVaultModifier {
    @Expose
    private final int levelAddend;

    public LevelModifier(String name, ResourceLocation icon, int levelAddend) {
        super(name, icon);
        this.levelAddend = levelAddend;

        if (this.levelAddend > 0) {
            format(getColor(), "Pushes the vault " + this.levelAddend + ((this.levelAddend == 1) ? " level higher." : " levels higher."));
        } else if (this.levelAddend < 0) {
            format(getColor(), "Pushes the vault " + -this.levelAddend + ((-this.levelAddend == 1) ? " level lower." : " levels lower."));
        } else {
            format(getColor(), "Does nothing at all. A bit of a waste of a modifier...");
        }
    }

    public int getLevelAddend() {
        return this.levelAddend;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\modifier\LevelModifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */