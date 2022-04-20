package iskallia.vault.world.vault.gen.layout;

import iskallia.vault.Vault;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;

import java.util.Random;

public interface JigsawPoolProvider {
    public static final Random rand = new Random();

    default JigsawPattern getStartRoomPool(final Registry<JigsawPattern> jigsawRegistry) {
        return (JigsawPattern) jigsawRegistry.get(Vault.id("vault/starts"));
    }

    default JigsawPattern getRoomPool(final Registry<JigsawPattern> jigsawRegistry) {
        return (JigsawPattern) jigsawRegistry.get(Vault.id("vault/rooms"));
    }

    default JigsawPattern getTunnelPool(final Registry<JigsawPattern> jigsawRegistry) {
        return (JigsawPattern) jigsawRegistry.get(Vault.id("vault/tunnels"));
    }
}
