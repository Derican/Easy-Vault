package iskallia.vault.world.vault.gen.piece;

import iskallia.vault.Vault;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.ResourceLocation;

public class FinalVaultBoss extends VaultPiece {
    public static final ResourceLocation ID;

    protected FinalVaultBoss(final ResourceLocation id) {
        super(id);
    }

    public FinalVaultBoss() {
        this(FinalVaultBoss.ID);
    }

    protected FinalVaultBoss(final ResourceLocation id, final ResourceLocation template, final MutableBoundingBox boundingBox, final Rotation rotation) {
        super(id, template, boundingBox, rotation);
    }

    public FinalVaultBoss(final ResourceLocation template, final MutableBoundingBox boundingBox, final Rotation rotation) {
        this(FinalVaultBoss.ID, template, boundingBox, rotation);
    }

    @Override
    public void tick(final ServerWorld world, final VaultRaid vault) {
    }

    static {
        ID = Vault.id("final_vault_boss");
    }
}
