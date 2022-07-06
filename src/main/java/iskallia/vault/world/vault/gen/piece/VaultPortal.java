package iskallia.vault.world.vault.gen.piece;

import iskallia.vault.Vault;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.ResourceLocation;

public class VaultPortal extends VaultPiece
{
    public static final ResourceLocation ID;

    public VaultPortal() {
        super(VaultPortal.ID);
    }

    public VaultPortal(final ResourceLocation template, final MutableBoundingBox boundingBox, final Rotation rotation) {
        super(VaultPortal.ID, template, boundingBox, rotation);
    }

    @Override
    public void tick(final ServerWorld world, final VaultRaid vault) {
    }

    static {
        ID = Vault.id("portal");
    }
}
