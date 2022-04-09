// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.logic.objective.raid.modifier;

import com.google.gson.annotations.Expose;
import iskallia.vault.util.MiscUtils;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.raid.ActiveRaid;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;

public class BlockPlacementModifier extends RaidModifier
{
    @Expose
    private final String block;
    @Expose
    private final int blocksToSpawn;
    @Expose
    private final String blockDescription;
    
    public BlockPlacementModifier(final String name, final Block block, final int blocksToSpawn, final String blockDescription) {
        this(name, block.getRegistryName().toString(), blocksToSpawn, blockDescription);
    }
    
    public BlockPlacementModifier(final String name, final String block, final int blocksToSpawn, final String blockDescription) {
        super(false, true, name);
        this.block = block;
        this.blocksToSpawn = blocksToSpawn;
        this.blockDescription = blockDescription;
    }
    
    @Override
    public void affectRaidMob(final MobEntity mob, final float value) {
    }
    
    @Override
    public void onVaultRaidFinish(final VaultRaid vault, final ServerWorld world, final BlockPos controller, final ActiveRaid raid, final float value) {
        final BlockState placementState = Registry.BLOCK.getOptional(new ResourceLocation(this.block)).orElse(Blocks.AIR).defaultBlockState();
        final int toPlace = this.blocksToSpawn * Math.round(value);
        final AxisAlignedBB placementBox = raid.getRaidBoundingBox();
        for (int i = 0; i < toPlace; ++i) {
            BlockPos at;
            do {
                at = MiscUtils.getRandomPos(placementBox, BlockPlacementModifier.rand);
            } while (!world.isEmptyBlock(at) || !world.getBlockState(at.below()).isFaceSturdy((IBlockReader)world, at, Direction.UP));
            world.setBlock(at, placementState, 2);
        }
    }
    
    @Override
    public ITextComponent getDisplay(final float value) {
        final int sets = Math.round(value);
        final String set = (sets > 1) ? "sets" : "set";
        return (ITextComponent)new StringTextComponent("+" + sets + " " + set + " of " + this.blockDescription).withStyle(TextFormatting.GREEN);
    }
}
