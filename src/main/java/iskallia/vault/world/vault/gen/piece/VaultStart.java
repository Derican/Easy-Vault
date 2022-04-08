package iskallia.vault.world.vault.gen.piece;

import iskallia.vault.Vault;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.server.ServerWorld;

public class VaultStart
        extends VaultPiece {
    public static final ResourceLocation ID = Vault.id("start");

    public VaultStart() {
        super(ID);
    }

    public VaultStart(ResourceLocation template, MutableBoundingBox boundingBox, Rotation rotation) {
        super(ID, template, boundingBox, rotation);
    }

    public void tick(ServerWorld world, VaultRaid vault) {
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\gen\piece\VaultStart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */