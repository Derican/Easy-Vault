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
    private final List<Level> levels;

    public VaultSizeConfig() {
        this.levels = new ArrayList<Level>();
    }

    @Override
    public String getName() {
        return "vault_size";
    }

    @Override
    protected void reset() {
        this.raffleLayout = new SizeLayout(11, 6.0f, DiamondRoomLayout.ID);
        this.defaultLayout = new SizeLayout(11, 6.0f, DiamondRoomLayout.ID);
        this.levels.clear();
        final SizeLayout l1 = new SizeLayout(7, 3.0f, DiamondRoomLayout.ID);
        this.levels.add(new Level(0, new WeightedList<SizeLayout>().add(l1, 1)));
        final SizeLayout l2 = new SizeLayout(9, 4.5f, DiamondRoomLayout.ID);
        this.levels.add(new Level(25, new WeightedList<SizeLayout>().add(l2, 1)));
        final SizeLayout l3 = new SizeLayout(11, 6.0f, DiamondRoomLayout.ID);
        this.levels.add(new Level(50, new WeightedList<SizeLayout>().add(l3, 1)));
        final SizeLayout l4 = new SizeLayout(13, 9.0f, DiamondRoomLayout.ID);
        final SizeLayout l5 = new SizeLayout(13, 9.0f, SquareRoomLayout.ID);
        this.levels.add(new Level(75, new WeightedList<SizeLayout>().add(l4, 2).add(l5, 1)));
        final SizeLayout l6 = new SizeLayout(15, 10.0f, DiamondRoomLayout.ID);
        final SizeLayout l7 = new SizeLayout(15, 10.0f, SquareRoomLayout.ID);
        this.levels.add(new Level(100, new WeightedList<SizeLayout>().add(l6, 2).add(l7, 1)));
        final SizeLayout l8 = new SizeLayout(17, 10.0f, DiamondRoomLayout.ID);
        final SizeLayout l9 = new SizeLayout(15, 12.0f, SquareRoomLayout.ID);
        this.levels.add(new Level(125, new WeightedList<SizeLayout>().add(l8, 2).add(l9, 1)));
        final SizeLayout l10 = new SizeLayout(19, 12.0f, DiamondRoomLayout.ID);
        final SizeLayout l11 = new SizeLayout(17, 14.5f, SquareRoomLayout.ID);
        this.levels.add(new Level(150, new WeightedList<SizeLayout>().add(l10, 2).add(l11, 1)));
    }

    @Nonnull
    public SizeLayout getLayout(final int vaultLevel, final boolean isRaffle) {
        if (isRaffle) {
            return this.raffleLayout;
        }
        final Level levelConfig = this.getForLevel(this.levels, vaultLevel);
        if (levelConfig == null) {
            return this.defaultLayout;
        }
        final SizeLayout layout = levelConfig.outcomes.getRandom(VaultSizeConfig.rand);
        if (layout == null) {
            return this.defaultLayout;
        }
        return layout;
    }

    @Nullable
    public Level getForLevel(final List<Level> levels, final int level) {
        int i = 0;
        while (i < levels.size()) {
            if (level < levels.get(i).level) {
                if (i == 0) {
                    break;
                }
                return levels.get(i - 1);
            } else {
                if (i == levels.size() - 1) {
                    return levels.get(i);
                }
                ++i;
            }
        }
        return null;
    }

    public static class Level {
        @Expose
        private final int level;
        @Expose
        private final WeightedList<SizeLayout> outcomes;

        public Level(final int level, final WeightedList<SizeLayout> outcomes) {
            this.level = level;
            this.outcomes = outcomes;
        }
    }

    public static class SizeLayout {
        @Expose
        private final int size;
        @Expose
        private final float objectiveRoomRatio;
        @Expose
        private final String layout;

        public SizeLayout(final int size, final float objectiveRoomRatio, final ResourceLocation layout) {
            this(size, objectiveRoomRatio, layout.toString());
        }

        public SizeLayout(final int size, final float objectiveRoomRatio, final String layout) {
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
