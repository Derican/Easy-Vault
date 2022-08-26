package iskallia.vault.world.vault.gen.piece;

import iskallia.vault.Vault;
import iskallia.vault.block.VaultDoorBlock;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class VaultTreasure extends VaultPiece {
    public static final ResourceLocation ID;

    public VaultTreasure() {
        super(VaultTreasure.ID);
    }

    public VaultTreasure(final ResourceLocation template, final MutableBoundingBox boundingBox, final Rotation rotation) {
        super(VaultTreasure.ID, template, boundingBox, rotation);
    }

    public boolean isDoorOpen(final World world) {
        return BlockPos.betweenClosedStream(this.getBoundingBox()).map(world::getBlockState).filter(state -> state.getBlock() instanceof VaultDoorBlock).anyMatch(state -> state.getValue(VaultDoorBlock.OPEN));
    }

    @Override
    public void tick(final ServerWorld world, final VaultRaid vault) {
        final AxisAlignedBB blind = AxisAlignedBB.of(this.boundingBox).inflate(-2.0, -2.0, -2.0);
        final AxisAlignedBB inner = blind.inflate(-0.5, -0.5, -0.5);
        vault.getPlayers().forEach(vaultPlayer -> vaultPlayer.runIfPresent(world.getServer(), playerEntity -> {
            if (blind.intersects(playerEntity.getBoundingBox()) && !this.isDoorOpen(world)) {
                playerEntity.addEffect(new EffectInstance(Effects.BLINDNESS, 40));
                if (!playerEntity.isSpectator() && inner.intersects(playerEntity.getBoundingBox())) {
                    playerEntity.hurt(DamageSource.MAGIC, 1000000.0f);
                    playerEntity.setHealth(0.0f);
                }
            }
        }));
    }

    static {
        ID = Vault.id("treasure");
    }
}
