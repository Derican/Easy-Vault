// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.block;

import javax.annotation.Nullable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import iskallia.vault.init.ModBlocks;
import net.minecraft.client.Minecraft;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.BarrierBlock;

public class FloatingTextBlock extends BarrierBlock
{
    public FloatingTextBlock() {
        super(AbstractBlock.Properties.of(Material.BARRIER).strength(-1.0f, 3.6E8f).noDrops().noOcclusion().noCollission());
    }
    
    public boolean isPossibleToRespawnInThis() {
        return true;
    }
    
    public BlockRenderType getRenderShape(final BlockState state) {
        return BlockRenderType.INVISIBLE;
    }
    
    public boolean isPathfindable(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final PathType type) {
        return true;
    }
    
    public void animateTick(final BlockState stateIn, final World worldIn, final BlockPos pos, final Random rand) {
        final Minecraft minecraft = Minecraft.getInstance();
        final ClientPlayerEntity player = minecraft.player;
        final ClientWorld world = minecraft.level;
        if (player != null && world != null && player.getMainHandItem().getItem() == ModBlocks.FLOATING_TEXT.asItem()) {
            final int i = pos.getX();
            final int j = pos.getY();
            final int k = pos.getZ();
            world.addParticle((IParticleData)ParticleTypes.BARRIER, i + 0.5, j + 0.5, k + 0.5, 0.0, 0.0, 0.0);
        }
    }
    
    public boolean hasTileEntity(final BlockState state) {
        return true;
    }
    
    @Nullable
    public TileEntity createTileEntity(final BlockState state, final IBlockReader world) {
        return ModBlocks.FLOATING_TEXT_TILE_ENTITY.create();
    }
}
