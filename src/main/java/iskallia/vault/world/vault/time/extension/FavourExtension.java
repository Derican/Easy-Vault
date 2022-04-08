package iskallia.vault.world.vault.time.extension;

import iskallia.vault.Vault;
import net.minecraft.util.ResourceLocation;

public class FavourExtension
        extends TimeExtension {
    public static final ResourceLocation ID = Vault.id("relic_set");

    public FavourExtension() {
    }

    public FavourExtension(long extraTime) {
        super(ID, extraTime);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\time\extension\FavourExtension.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */