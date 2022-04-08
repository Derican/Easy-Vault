package iskallia.vault.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class MazeBlock extends Block {
    public static final EnumProperty<MazeColor> COLOR = EnumProperty.create("color", MazeColor.class);

    public MazeBlock() {
        super(AbstractBlock.Properties.of(Material.METAL, MaterialColor.METAL)
                .strength(-1.0F, 3600000.0F)
                .sound(SoundType.METAL));

        registerDefaultState((BlockState) ((BlockState) this.stateDefinition.any())
                .setValue((Property) COLOR, MazeColor.RED));
    }


    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{(Property) COLOR});
    }


    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return super.getStateForPlacement(context);
    }


    public void stepOn(World worldIn, BlockPos pos, Entity entityIn) {
        if (worldIn.isClientSide)
            return;
        if (!(entityIn instanceof PlayerEntity))
            return;
        PlayerEntity player = (PlayerEntity) entityIn;
        Scoreboard scoreboard = worldIn.getScoreboard();

        if (scoreboard.getObjective("Color") == null) {
            scoreboard.addObjective("Color", ScoreCriteria.DUMMY, (ITextComponent) new StringTextComponent("Color"), ScoreCriteria.RenderType.INTEGER);
        }
        ScoreObjective colorObjective = scoreboard.getObjective("Color");
        assert colorObjective != null;


        Score colorScore = worldIn.getScoreboard().getOrCreatePlayerScore(player.getDisplayName().getString(), colorObjective);
        MazeColor playerColor = MazeColor.values()[colorScore.getScore()];
        MazeColor blockColor = (MazeColor) worldIn.getBlockState(pos).getValue((Property) COLOR);

        if (playerColor != blockColor) {
            BlockPos nextPosition = player.blockPosition().relative(player.getDirection(), 1);
            colorScore.setScore((playerColor == MazeColor.RED) ? MazeColor.BLUE.ordinal() : MazeColor.RED.ordinal());
            player.teleportTo(nextPosition.getX() + 0.5D, nextPosition.getY(), nextPosition.getZ() + 0.5D);
        }

        super.stepOn(worldIn, pos, entityIn);
    }

    public enum MazeColor implements IStringSerializable {
        RED("red"),
        BLUE("blue");

        private String name;

        MazeColor(String name) {
            this.name = name;
        }


        public String getSerializedName() {
            return this.name;
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\MazeBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */