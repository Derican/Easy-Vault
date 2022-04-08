package iskallia.vault.world.vault.gen.layout;

import iskallia.vault.Vault;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;

import java.util.Random;


public interface JigsawPoolProvider {
    public static final Random rand = new Random();

    default JigsawPattern getStartRoomPool(Registry<JigsawPattern> jigsawRegistry) {
        return (JigsawPattern) jigsawRegistry.get(Vault.id("vault/starts"));
    }

    default JigsawPattern getRoomPool(Registry<JigsawPattern> jigsawRegistry) {
        return (JigsawPattern) jigsawRegistry.get(Vault.id("vault/rooms"));
    }

    default JigsawPattern getTunnelPool(Registry<JigsawPattern> jigsawRegistry) {
        return (JigsawPattern) jigsawRegistry.get(Vault.id("vault/tunnels"));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\gen\layout\JigsawPoolProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */