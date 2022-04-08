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
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;

public class BlockPlacementModifier extends RaidModifier {
    @Expose
    private final String block;
    @Expose
    private final int blocksToSpawn;
    @Expose
    private final String blockDescription;

    public BlockPlacementModifier(String name, Block block, int blocksToSpawn, String blockDescription) {
        this(name, block.getRegistryName().toString(), blocksToSpawn, blockDescription);
    }

    public BlockPlacementModifier(String name, String block, int blocksToSpawn, String blockDescription) {
        super(false, true, name);
        this.block = block;
        this.blocksToSpawn = blocksToSpawn;
        this.blockDescription = blockDescription;
    }


    public void affectRaidMob(MobEntity mob, float value) {
    }


    public void onVaultRaidFinish(VaultRaid vault, ServerWorld world, BlockPos controller, ActiveRaid raid, float value) {
        BlockState placementState = ((Block) Registry.BLOCK.getOptional(new ResourceLocation(this.block)).orElse(Blocks.AIR)).defaultBlockState();
        int toPlace = this.blocksToSpawn * Math.round(value);
        AxisAlignedBB placementBox = raid.getRaidBoundingBox();

        for (int i = 0; i < toPlace; ) {

            while (true) {
                BlockPos at = MiscUtils.getRandomPos(placementBox, rand);
                if (world.isEmptyBlock(at) && world.getBlockState(at.below()).isFaceSturdy((IBlockReader) world, at, Direction.UP)) {
                    world.setBlock(at, placementState, 2);
                    break;
                }
            }
            i++;
        }
    }

    public ITextComponent getDisplay(float value) {
        int sets = Math.round(value);
        String set = (sets > 1) ? "sets" : "set";
        return (ITextComponent) (new StringTextComponent("+" + sets + " " + set + " of " + this.blockDescription))
                .withStyle(TextFormatting.GREEN);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\objective\raid\modifier\BlockPlacementModifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */