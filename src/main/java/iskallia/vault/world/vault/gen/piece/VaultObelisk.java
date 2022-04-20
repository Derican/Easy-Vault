package iskallia.vault.world.vault.gen.piece;

import iskallia.vault.Vault;
import iskallia.vault.block.ObeliskBlock;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class VaultObelisk extends VaultPiece {
    public static final ResourceLocation ID;

    public VaultObelisk() {
        super(VaultObelisk.ID);
    }

    public VaultObelisk(final ResourceLocation template, final MutableBoundingBox boundingBox, final Rotation rotation) {
        super(VaultObelisk.ID, template, boundingBox, rotation);
    }

    public boolean isCompleted(final World world) {
        return BlockPos.betweenClosedStream(this.getBoundingBox()).map(world::getBlockState).filter(state -> state.getBlock() instanceof ObeliskBlock).anyMatch(blockState -> (int) blockState.getValue(ObeliskBlock.COMPLETION) == 4);
    }

    @Override
    public void tick(final ServerWorld world, final VaultRaid vault) {
    }

    static {
        ID = Vault.id("obelisk");
    }
}
