// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.ability;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import iskallia.vault.skill.ability.effect.AbilityEffect;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;

public class AbilityRegistry
{
    private static final BiMap<String, AbilityEffect<?>> abilityRegistry;
    
    public static <E extends AbilityEffect<?>> E register(final String key, final E ability) {
        AbilityRegistry.abilityRegistry.put(key, ability);
        MinecraftForge.EVENT_BUS.register(ability);
        return ability;
    }
    
    @Nullable
    public static AbilityEffect<?> getAbility(final String key) {
        return (AbilityEffect)AbilityRegistry.abilityRegistry.get(key);
    }
    
    @Nullable
    public static String getKey(final AbilityEffect<?> ability) {
        return (String)AbilityRegistry.abilityRegistry.inverse().get(ability);
    }
    
    static {
        abilityRegistry = (BiMap)HashBiMap.create();
    }
}
