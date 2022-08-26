package iskallia.vault.world.vault.influence;

import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class VaultInfluenceRegistry {
    private static final Map<ResourceLocation, Supplier<VaultInfluence>> influences;

    public static void init() {
        VaultInfluenceRegistry.influences.clear();
        register(TimeInfluence.ID, TimeInfluence::new);
        register(EffectInfluence.ID, EffectInfluence::new);
        register(MobAttributeInfluence.ID, MobAttributeInfluence::new);
        register(MobsInfluence.ID, MobsInfluence::new);
        register(DamageInfluence.ID, DamageInfluence::new);
        register(DamageTakenInfluence.ID, DamageTakenInfluence::new);
        Arrays.stream(VaultAttributeInfluence.Type.values()).forEach(type -> register(VaultAttributeInfluence.newInstance(type)));
    }

    public static Optional<VaultInfluence> getInfluence(final ResourceLocation key) {
        return Optional.ofNullable(VaultInfluenceRegistry.influences.get(key)).map((Function<? super Supplier<VaultInfluence>, ? extends VaultInfluence>) Supplier::get);
    }

    private static void register(final Supplier<VaultInfluence> defaultSupplier) {
        VaultInfluenceRegistry.influences.put(defaultSupplier.get().getKey(), defaultSupplier);
    }

    private static void register(final ResourceLocation key, final Supplier<VaultInfluence> defaultSupplier) {
        VaultInfluenceRegistry.influences.put(key, defaultSupplier);
    }

    static {
        influences = new HashMap<ResourceLocation, Supplier<VaultInfluence>>();
    }
}
