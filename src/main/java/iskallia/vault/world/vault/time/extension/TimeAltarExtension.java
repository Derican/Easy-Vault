package iskallia.vault.world.vault.time.extension;

import iskallia.vault.Vault;
import net.minecraft.util.ResourceLocation;

public class TimeAltarExtension
        extends TimeExtension {
    public static final ResourceLocation ID = Vault.id("time_altar");

    public TimeAltarExtension() {
    }

    public TimeAltarExtension(int value) {
        super(ID, value);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\time\extension\TimeAltarExtension.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */