package iskallia.vault.world.vault.time.extension;

import iskallia.vault.Vault;
import net.minecraft.util.ResourceLocation;

public class FavourExtension extends TimeExtension {
    public static final ResourceLocation ID;

    public FavourExtension() {
    }

    public FavourExtension(final long extraTime) {
        super(FavourExtension.ID, extraTime);
    }

    static {
        ID = Vault.id("relic_set");
    }
}
