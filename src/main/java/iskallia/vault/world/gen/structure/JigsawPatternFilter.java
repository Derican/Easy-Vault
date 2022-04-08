package iskallia.vault.world.gen.structure;

import iskallia.vault.util.data.WeightedList;
import iskallia.vault.world.gen.structure.pool.PalettedListPoolElement;
import iskallia.vault.world.gen.structure.pool.PalettedSinglePoolElement;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;


public class JigsawPatternFilter {
    private Predicate<ResourceLocation> roomPieceFilter = key -> true;
    private WeightedList<JigsawPiece> filteredPieceCache = null;

    public JigsawPatternFilter andMatches(Predicate<ResourceLocation> filter) {
        this.roomPieceFilter = this.roomPieceFilter.and(filter);
        return this;
    }

    public JigsawPiece getRandomPiece(JigsawPattern pattern, Random random) {
        if (this.filteredPieceCache != null) {
            return (JigsawPiece) this.filteredPieceCache.getRandom(random);
        }
        this.filteredPieceCache = new WeightedList();
        pattern.rawTemplates.forEach(weightedPiece -> {
            if (isApplicable((JigsawPiece) weightedPiece.getFirst())) {
                this.filteredPieceCache.add(weightedPiece.getFirst(), ((Integer) weightedPiece.getSecond()).intValue());
            }
        });
        return getRandomPiece(pattern, random);
    }

    private boolean isApplicable(JigsawPiece piece) {
        if (piece instanceof PalettedListPoolElement) {
            List<JigsawPiece> elements = ((PalettedListPoolElement) piece).getElements();
            for (JigsawPiece elementPiece : elements) {
                if (!isApplicable(elementPiece)) {
                    return false;
                }
            }
            return !elements.isEmpty();
        }
        if (piece instanceof PalettedSinglePoolElement) {
            ResourceLocation key = ((PalettedSinglePoolElement) piece).getTemplate().left().orElse(null);
            if (key != null) {
                return this.roomPieceFilter.test(key);
            }
        }
        return false;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\gen\structure\JigsawPatternFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */