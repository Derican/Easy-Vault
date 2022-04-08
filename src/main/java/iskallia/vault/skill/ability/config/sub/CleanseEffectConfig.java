package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.AbilityConfig;
import iskallia.vault.skill.ability.config.CleanseConfig;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CleanseEffectConfig extends CleanseConfig {
    @Expose
    private final List<String> possibleEffects = new ArrayList<>();


    public CleanseEffectConfig(int learningCost, AbilityConfig.Behavior behavior, int cooldown, int effectAmplifier, List<String> possibleEffects) {
        super(learningCost, behavior, cooldown);
        this.effectAmplifier = effectAmplifier;
        this.possibleEffects.addAll(possibleEffects);
    }

    @Expose
    private final int effectAmplifier;

    public static CleanseEffectConfig ofEffectNames(int learningCost, AbilityConfig.Behavior behavior, int cooldown, int effectAmplifier, List<ResourceLocation> possibleEffects) {
        return new CleanseEffectConfig(learningCost, behavior, cooldown, effectAmplifier, (List<String>) possibleEffects
                .stream().map(ResourceLocation::toString).collect(Collectors.toList()));
    }

    public static CleanseEffectConfig ofEffects(int learningCost, AbilityConfig.Behavior behavior, int cooldown, int effectAmplifier, List<Effect> possibleEffects) {
        return new CleanseEffectConfig(learningCost, behavior, cooldown, effectAmplifier, (List<String>) possibleEffects
                .stream().map(effect -> effect.getRegistryName().toString()).collect(Collectors.toList()));
    }

    public List<String> getPossibleEffects() {
        return this.possibleEffects;
    }

    public int getEffectAmplifier() {
        return this.effectAmplifier;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\config\sub\CleanseEffectConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */