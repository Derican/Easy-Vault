package iskallia.vault.world.vault.gen.piece;

import iskallia.vault.block.VaultDoorBlock;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.state.Property;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class VaultTreasure extends VaultPiece {
    public static final ResourceLocation ID = Vault.id("treasure");

    public VaultTreasure() {
        super(ID);
    }

    public VaultTreasure(ResourceLocation template, MutableBoundingBox boundingBox, Rotation rotation) {
        super(ID, template, boundingBox, rotation);
    }

    public boolean isDoorOpen(World world) {
        return BlockPos.betweenClosedStream(getBoundingBox())
                .map(world::getBlockState)
                .filter(state -> state.getBlock() instanceof VaultDoorBlock)
                .anyMatch(state -> ((Boolean) state.getValue((Property) VaultDoorBlock.OPEN)).booleanValue());
    }


    public void tick(ServerWorld world, VaultRaid vault) {
        AxisAlignedBB blind = AxisAlignedBB.of(this.boundingBox).inflate(-2.0D, -2.0D, -2.0D);
        AxisAlignedBB inner = blind.inflate(-0.5D, -0.5D, -0.5D);

        vault.getPlayers().forEach(vaultPlayer -> vaultPlayer.runIfPresent(world.getServer(), ()));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\gen\piece\VaultTreasure.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */