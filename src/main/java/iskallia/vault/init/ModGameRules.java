package iskallia.vault.init;

import iskallia.vault.mixin.MixinBooleanValue;
import net.minecraft.world.GameRules;

public class ModGameRules {
    public static GameRules.RuleKey<GameRules.BooleanValue> FINAL_VAULT_ALLOW_PARTY;

    public static void initialize() {
        ModGameRules.FINAL_VAULT_ALLOW_PARTY = register("finalVaultAllowParty", GameRules.Category.MISC, booleanRule(false));
    }

    public static <T extends GameRules.RuleValue<T>> GameRules.RuleKey<T> register(final String name, final GameRules.Category category, final GameRules.RuleType<T> type) {
        return (GameRules.RuleKey<T>) GameRules.register(name, category, (GameRules.RuleType) type);
    }

    public static GameRules.RuleType<GameRules.BooleanValue> booleanRule(final boolean defaultValue) {
        return MixinBooleanValue.create(defaultValue);
    }
}
