package iskallia.vault.world.vault.time.extension;

import iskallia.vault.Vault;
import net.minecraft.util.ResourceLocation;

public class RoomGenerationExtension
        extends TimeExtension {
    public static final ResourceLocation ID = Vault.id("room_generation");

    public RoomGenerationExtension() {
    }

    public RoomGenerationExtension(int value) {
        super(ID, value);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\time\extension\RoomGenerationExtension.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */