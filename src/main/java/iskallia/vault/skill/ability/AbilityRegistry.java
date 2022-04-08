package iskallia.vault.skill.ability;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import iskallia.vault.skill.ability.effect.AbilityEffect;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;


public class AbilityRegistry {
    private static final BiMap<String, AbilityEffect<?>> abilityRegistry = (BiMap<String, AbilityEffect<?>>) HashBiMap.create();

    public static <E extends AbilityEffect<?>> E register(String key, E ability) {
        abilityRegistry.put(key, ability);
        MinecraftForge.EVENT_BUS.register(ability);
        return ability;
    }

    @Nullable
    public static AbilityEffect<?> getAbility(String key) {
        return (AbilityEffect) abilityRegistry.get(key);
    }

    @Nullable
    public static String getKey(AbilityEffect<?> ability) {
        return (String) abilityRegistry.inverse().get(ability);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\AbilityRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */