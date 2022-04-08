package iskallia.vault.client.particles;

import iskallia.vault.block.entity.StabilizerTileEntity;
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

public class StabilizerCubeParticle
        extends BaseFloatingCubeParticle {
    private final BlockPos originPos;

    private StabilizerCubeParticle(ClientWorld world, IAnimatedSprite spriteSet, double x, double y, double z) {
        super(world, spriteSet, x, y, z);
        this.originPos = new BlockPos(x, y, z);
    }


    protected boolean isValid() {
        return (getTileRef() != null);
    }


    protected boolean isActive() {
        StabilizerTileEntity tile = getTileRef();
        return (tile != null && tile.isActive());
    }

    @Nullable
    private StabilizerTileEntity getTileRef() {
        BlockState at = this.level.getBlockState(this.originPos);
        if (!(at.getBlock() instanceof iskallia.vault.block.StabilizerBlock)) {
            return null;
        }
        TileEntity tile = this.level.getBlockEntity(this.originPos);
        if (tile instanceof StabilizerTileEntity) {
            return (StabilizerTileEntity) tile;
        }
        return null;
    }


    protected int getActiveColor() {
        return 301982;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory
            implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite spriteSet;

        public Factory(IAnimatedSprite spriteSet) {
            this.spriteSet = spriteSet;
        }


        @Nullable
        public Particle makeParticle(BasicParticleType type, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new StabilizerCubeParticle(worldIn, this.spriteSet, x, y, z);
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\particles\StabilizerCubeParticle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */