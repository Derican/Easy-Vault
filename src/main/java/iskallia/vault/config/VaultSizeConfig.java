package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.util.data.WeightedList;
import iskallia.vault.world.vault.gen.layout.DiamondRoomLayout;
import iskallia.vault.world.vault.gen.layout.SquareRoomLayout;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class VaultSizeConfig extends Config {
    @Expose
    private SizeLayout defaultLayout;
    @Expose
    private SizeLayout raffleLayout;
    @Expose
    private final List<Level> levels = new ArrayList<>();


    public String getName() {
        return "vault_size";
    }


    protected void reset() {
        this.raffleLayout = new SizeLayout(11, 6.0F, DiamondRoomLayout.ID);
        this.defaultLayout = new SizeLayout(11, 6.0F, DiamondRoomLayout.ID);

        this.levels.clear();

        SizeLayout l1 = new SizeLayout(7, 3.0F, DiamondRoomLayout.ID);
        this.levels.add(new Level(0, (new WeightedList()).add(l1, 1)));

        SizeLayout l2 = new SizeLayout(9, 4.5F, DiamondRoomLayout.ID);
        this.levels.add(new Level(25, (new WeightedList()).add(l2, 1)));

        SizeLayout l3 = new SizeLayout(11, 6.0F, DiamondRoomLayout.ID);
        this.levels.add(new Level(50, (new WeightedList()).add(l3, 1)));

        SizeLayout l41 = new SizeLayout(13, 9.0F, DiamondRoomLayout.ID);
        SizeLayout l42 = new SizeLayout(13, 9.0F, SquareRoomLayout.ID);
        this.levels.add(new Level(75, (new WeightedList())
                .add(l41, 2)
                .add(l42, 1)));


        SizeLayout l51 = new SizeLayout(15, 10.0F, DiamondRoomLayout.ID);
        SizeLayout l52 = new SizeLayout(15, 10.0F, SquareRoomLayout.ID);
        this.levels.add(new Level(100, (new WeightedList())
                .add(l51, 2)
                .add(l52, 1)));


        SizeLayout l61 = new SizeLayout(17, 10.0F, DiamondRoomLayout.ID);
        SizeLayout l62 = new SizeLayout(15, 12.0F, SquareRoomLayout.ID);
        this.levels.add(new Level(125, (new WeightedList())
                .add(l61, 2)
                .add(l62, 1)));


        SizeLayout l71 = new SizeLayout(19, 12.0F, DiamondRoomLayout.ID);
        SizeLayout l72 = new SizeLayout(17, 14.5F, SquareRoomLayout.ID);
        this.levels.add(new Level(150, (new WeightedList())
                .add(l71, 2)
                .add(l72, 1)));
    }


    @Nonnull
    public SizeLayout getLayout(int vaultLevel, boolean isRaffle) {
        if (isRaffle) {
            return this.raffleLayout;
        }
        Level levelConfig = getForLevel(this.levels, vaultLevel);
        if (levelConfig == null) {
            return this.defaultLayout;
        }
        SizeLayout layout = (SizeLayout) levelConfig.outcomes.getRandom(rand);
        if (layout == null) {
            return this.defaultLayout;
        }
        return layout;
    }

    @Nullable
    public Level getForLevel(List<Level> levels, int level) {
        for (int i = 0; i < levels.size(); i++) {
            if (level < ((Level) levels.get(i)).level) {
                if (i == 0) {
                    break;
                }
                return levels.get(i - 1);
            }
            if (i == levels.size() - 1) {
                return levels.get(i);
            }
        }
        return null;
    }

    public static class Level {
        @Expose
        private final int level;
        @Expose
        private final WeightedList<VaultSizeConfig.SizeLayout> outcomes;

        public Level(int level, WeightedList<VaultSizeConfig.SizeLayout> outcomes) {
            this.level = level;
            this.outcomes = outcomes;
        }
    }


    public static class SizeLayout {
        @Expose
        private final int size;

        public SizeLayout(int size, float objectiveRoomRatio, ResourceLocation layout) {
            this(size, objectiveRoomRatio, layout.toString());
        }

        @Expose
        private final float objectiveRoomRatio;
        @Expose
        private final String layout;

        public SizeLayout(int size, float objectiveRoomRatio, String layout) {
            this.size = size;
            this.objectiveRoomRatio = objectiveRoomRatio;
            this.layout = layout;
        }

        public int getSize() {
            return this.size;
        }

        public float getObjectiveRoomRatio() {
            return this.objectiveRoomRatio;
        }

        public ResourceLocation getLayout() {
            return new ResourceLocation(this.layout);
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\VaultSizeConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */