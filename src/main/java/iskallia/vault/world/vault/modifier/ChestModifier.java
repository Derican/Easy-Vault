package iskallia.vault.world.vault.modifier;

import com.google.gson.annotations.Expose;
import net.minecraft.util.ResourceLocation;

public class ChestModifier extends TexturedVaultModifier {
    @Expose
    private final int additionalBonusChestPasses;
    @Expose
    private final int chestGenerationAttempts;

    public ChestModifier(final String name, final ResourceLocation icon, final int additionalBonusChestPasses) {
        this(name, icon, additionalBonusChestPasses, 48);
    }

    public ChestModifier(final String name, final ResourceLocation icon, final int additionalBonusChestPasses, final int chestGenerationAttempts) {
        super(name, icon);
        this.additionalBonusChestPasses = additionalBonusChestPasses;
        this.chestGenerationAttempts = chestGenerationAttempts;
        if (additionalBonusChestPasses == 1) {
            this.format(this.getColor(), "Adds an additional set of random chests!");
        } else if (additionalBonusChestPasses > 1) {
            this.format(this.getColor(), "Adds " + additionalBonusChestPasses + " additional sets of random chests!");
        } else {
            this.format(this.getColor(), "Does absolutely nothing. Whoever wrote this config made a mistake...");
        }
    }

    public int getAdditionalBonusChestPasses() {
        return this.additionalBonusChestPasses;
    }

    public int getChestGenerationAttempts() {
        return this.chestGenerationAttempts;
    }
}
