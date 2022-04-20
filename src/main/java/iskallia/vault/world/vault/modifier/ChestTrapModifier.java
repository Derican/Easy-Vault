package iskallia.vault.world.vault.modifier;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.VaultChestConfig;
import iskallia.vault.util.data.RandomListAccess;
import iskallia.vault.util.data.WeightedDoubleList;
import iskallia.vault.world.vault.chest.VaultChestEffect;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class ChestTrapModifier extends TexturedVaultModifier {
    @Expose
    private final double chanceOfTrappedChests;

    public ChestTrapModifier(final String name, final ResourceLocation icon, final double chanceOfTrappedChests) {
        super(name, icon);
        this.chanceOfTrappedChests = chanceOfTrappedChests;
    }

    public double getChanceOfTrappedChests() {
        return this.chanceOfTrappedChests;
    }

    @Nonnull
    public RandomListAccess<String> modifyWeightedList(final VaultChestConfig config, final RandomListAccess<String> chestOutcomes) {
        final WeightedDoubleList<String> reWeightedList = new WeightedDoubleList<String>();
        chestOutcomes.forEach((entry, weight) -> {
            final VaultChestEffect effect = config.getEffectByName(entry);
            if (effect == null || !effect.isTrapEffect()) {
                reWeightedList.add(entry, weight.doubleValue());
            } else {
                reWeightedList.add(entry, weight.doubleValue() * this.getChanceOfTrappedChests());
            }
            return;
        });
        return reWeightedList;
    }
}
