package iskallia.vault.world.vault.gen.piece;

import iskallia.vault.Vault;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.state.Property;
import iskallia.vault.block.GodEyeBlock;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.ResourceLocation;

public class VaultGodEye extends VaultPiece
{
    public static final ResourceLocation ID;

    public VaultGodEye() {
        super(VaultGodEye.ID);
    }

    public VaultGodEye(final ResourceLocation template, final MutableBoundingBox boundingBox, final Rotation rotation) {
        super(VaultGodEye.ID, template, boundingBox, rotation);
    }

    public boolean isLit(final ServerWorld world) {
        return world.getBlockState(this.getMin()).getValue((Property)GodEyeBlock.LIT) == Boolean.TRUE;
    }

    @Override
    public void tick(final ServerWorld world, final VaultRaid vault) {
    }

    static {
        ID = Vault.id("god_eye");
    }
}
