package iskallia.vault.util;

import iskallia.vault.init.ModConfigs;
import net.minecraft.util.text.TextFormatting;

import java.util.Random;

public enum VaultRarity {
    COMMON(TextFormatting.WHITE),
    RARE(TextFormatting.YELLOW),
    EPIC(TextFormatting.LIGHT_PURPLE),
    OMEGA(TextFormatting.GREEN);

    public final TextFormatting color;

    VaultRarity(final TextFormatting color) {
        this.color = color;
    }

    public static VaultRarity getWeightedRandom() {
        final Random rand = new Random();
        return getWeightedRarityAt(rand.nextInt(getTotalWeight()));
    }

    private static int getTotalWeight() {
        int totalWeight = 0;
        for (final VaultRarity rarity : values()) {
            totalWeight += getWeight(rarity);
        }
        return totalWeight;
    }

    private static VaultRarity getWeightedRarityAt(int index) {
        VaultRarity current = null;
        final VaultRarity[] values = values();
        for (int length = values.length, i = 0; i < length; ++i) {
            final VaultRarity rarity = current = values[i];
            index -= getWeight(rarity);
            if (index < 0) {
                break;
            }
        }
        return current;
    }

    private static int getWeight(final VaultRarity rarity) {
        switch (rarity) {
            case COMMON: {
                return ModConfigs.VAULT_CRYSTAL.NORMAL_WEIGHT;
            }
            case RARE: {
                return ModConfigs.VAULT_CRYSTAL.RARE_WEIGHT;
            }
            case EPIC: {
                return ModConfigs.VAULT_CRYSTAL.EPIC_WEIGHT;
            }
            case OMEGA: {
                return ModConfigs.VAULT_CRYSTAL.OMEGA_WEIGHT;
            }
            default: {
                return ModConfigs.VAULT_CRYSTAL.NORMAL_WEIGHT;
            }
        }
    }
}
