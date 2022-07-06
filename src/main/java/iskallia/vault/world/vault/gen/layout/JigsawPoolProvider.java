package iskallia.vault.world.vault.gen.layout;

import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.ResourceLocation;

import java.util.Random;

public interface JigsawPoolProvider {
    public static final Random rand = new Random();

    ResourceLocation getStartRoomId();

    ResourceLocation getRoomId();

    ResourceLocation getTunnelId();

    default JigsawPattern getStartRoomPool(final Registry<JigsawPattern> jigsawRegistry) {
        return (JigsawPattern) jigsawRegistry.get(this.getStartRoomId());
    }

    default JigsawPattern getRoomPool(final Registry<JigsawPattern> jigsawRegistry) {
        return (JigsawPattern) jigsawRegistry.get(this.getRoomId());
    }

    default JigsawPattern getTunnelPool(final Registry<JigsawPattern> jigsawRegistry) {
        return (JigsawPattern) jigsawRegistry.get(this.getTunnelId());
    }
}
