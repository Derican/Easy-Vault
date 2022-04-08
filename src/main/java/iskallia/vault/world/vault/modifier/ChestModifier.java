package iskallia.vault.world.vault.modifier;

import com.google.gson.annotations.Expose;
import net.minecraft.util.ResourceLocation;

public class ChestModifier
        extends TexturedVaultModifier {
    @Expose
    private final int additionalBonusChestPasses;

    public ChestModifier(String name, ResourceLocation icon, int additionalBonusChestPasses) {
        this(name, icon, additionalBonusChestPasses, 48);
    }

    @Expose
    private final int chestGenerationAttempts;

    public ChestModifier(String name, ResourceLocation icon, int additionalBonusChestPasses, int chestGenerationAttempts) {
        super(name, icon);
        this.additionalBonusChestPasses = additionalBonusChestPasses;
        this.chestGenerationAttempts = chestGenerationAttempts;

        if (additionalBonusChestPasses == 1) {
            format(getColor(), "Adds an additional set of random chests!");
        } else if (additionalBonusChestPasses > 1) {
            format(getColor(), "Adds " + additionalBonusChestPasses + " additional sets of random chests!");
        } else {
            format(getColor(), "Does absolutely nothing. Whoever wrote this config made a mistake...");
        }
    }

    public int getAdditionalBonusChestPasses() {
        return this.additionalBonusChestPasses;
    }

    public int getChestGenerationAttempts() {
        return this.chestGenerationAttempts;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\modifier\ChestModifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */