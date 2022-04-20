package iskallia.vault.client.particles;

import iskallia.vault.block.VaultRaidControllerBlock;
import iskallia.vault.block.entity.VaultRaidControllerTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class RaidCubeParticle extends BaseFloatingCubeParticle {
    private final BlockPos originPos;

    private RaidCubeParticle(final ClientWorld world, final IAnimatedSprite spriteSet, final double x, final double y, final double z) {
        super(world, spriteSet, x, y, z);
        this.originPos = new BlockPos(x, y, z);
    }

    @Override
    protected boolean isValid() {
        return this.getTileRef() != null;
    }

    @Override
    protected boolean isActive() {
        final VaultRaidControllerTileEntity tile = this.getTileRef();
        return tile != null && tile.isActive();
    }

    @Nullable
    private VaultRaidControllerTileEntity getTileRef() {
        final BlockState at = this.level.getBlockState(this.originPos);
        if (!(at.getBlock() instanceof VaultRaidControllerBlock)) {
            return null;
        }
        final TileEntity tile = this.level.getBlockEntity(this.originPos);
        if (tile instanceof VaultRaidControllerTileEntity) {
            return (VaultRaidControllerTileEntity) tile;
        }
        return null;
    }

    @Override
    protected int getActiveColor() {
        return 11932948;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite spriteSet;

        public Factory(final IAnimatedSprite spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        public Particle createParticle(final BasicParticleType type, final ClientWorld worldIn, final double x, final double y, final double z, final double xSpeed, final double ySpeed, final double zSpeed) {
            return new RaidCubeParticle(worldIn, this.spriteSet, x, y, z);
        }
    }
}
