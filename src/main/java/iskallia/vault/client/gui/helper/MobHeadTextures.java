// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.client.gui.helper;

import iskallia.vault.init.ModEntities;
import java.util.HashMap;
import java.util.Optional;
import javax.annotation.Nullable;
import iskallia.vault.Vault;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import java.util.Map;

public class MobHeadTextures
{
    private static final Map<String, ResourceLocation> REGISTRY;
    
    private static void register(final EntityType<?> entityType) {
        final ResourceLocation registryName = entityType.getRegistryName();
        if (registryName == null) {
            throw new InternalError();
        }
        register(registryName);
    }
    
    private static void register(final ResourceLocation id) {
        final String namespace = id.getNamespace();
        final String path = id.getPath();
        MobHeadTextures.REGISTRY.put(id.toString(), Vault.id("textures/gui/mob_heads/" + namespace + "/" + path + ".png"));
    }
    
    public static Optional<ResourceLocation> get(@Nullable final ResourceLocation mobId) {
        if (mobId == null) {
            return Optional.empty();
        }
        return get(mobId.toString());
    }
    
    public static Optional<ResourceLocation> get(final String mobId) {
        return Optional.ofNullable(MobHeadTextures.REGISTRY.get(mobId));
    }
    
    static {
        REGISTRY = new HashMap<String, ResourceLocation>();
        register((EntityType<?>)EntityType.CAVE_SPIDER);
        register((EntityType<?>)EntityType.CREEPER);
        register((EntityType<?>)EntityType.DROWNED);
        register((EntityType<?>)EntityType.HUSK);
        register((EntityType<?>)EntityType.PIGLIN);
        register((EntityType<?>)EntityType.RAVAGER);
        register((EntityType<?>)EntityType.SILVERFISH);
        register((EntityType<?>)EntityType.SKELETON);
        register((EntityType<?>)EntityType.SPIDER);
        register((EntityType<?>)EntityType.STRAY);
        register((EntityType<?>)EntityType.VEX);
        register((EntityType<?>)EntityType.VINDICATOR);
        register((EntityType<?>)EntityType.WITCH);
        register((EntityType<?>)EntityType.WITHER_SKELETON);
        register((EntityType<?>)EntityType.ZOMBIE);
        register(ModEntities.BOOGIEMAN);
        register(ModEntities.BLUE_BLAZE);
        register(ModEntities.TREASURE_GOBLIN);
        register(ModEntities.ROBOT);
    }
}
