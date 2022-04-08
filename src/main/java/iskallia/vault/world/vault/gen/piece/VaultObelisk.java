package iskallia.vault.world.vault.gen.piece;

import iskallia.vault.Vault;
import iskallia.vault.block.ObeliskBlock;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.state.Property;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class VaultObelisk extends VaultPiece {
    public static final ResourceLocation ID = Vault.id("obelisk");

    public VaultObelisk() {
        super(ID);
    }

    public VaultObelisk(ResourceLocation template, MutableBoundingBox boundingBox, Rotation rotation) {
        super(ID, template, boundingBox, rotation);
    }

    public boolean isCompleted(World world) {
        return BlockPos.betweenClosedStream(getBoundingBox())
                .map(world::getBlockState)
                .filter(state -> state.getBlock() instanceof ObeliskBlock)
                .anyMatch(blockState -> (((Integer) blockState.getValue((Property) ObeliskBlock.COMPLETION)).intValue() == 4));
    }

    public void tick(ServerWorld world, VaultRaid vault) {
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\gen\piece\VaultObelisk.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */