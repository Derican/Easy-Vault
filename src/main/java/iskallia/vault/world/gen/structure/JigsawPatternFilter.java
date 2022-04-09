// 
// Decompiled by Procyon v0.6.0
// 

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

public class JigsawPatternFilter
{
    private Predicate<ResourceLocation> roomPieceFilter;
    private WeightedList<JigsawPiece> filteredPieceCache;
    
    public JigsawPatternFilter() {
        this.roomPieceFilter = (key -> true);
        this.filteredPieceCache = null;
    }
    
    public JigsawPatternFilter andMatches(final Predicate<ResourceLocation> filter) {
        this.roomPieceFilter = this.roomPieceFilter.and(filter);
        return this;
    }
    
    public JigsawPiece getRandomPiece(final JigsawPattern pattern, final Random random) {
        if (this.filteredPieceCache != null) {
            return this.filteredPieceCache.getRandom(random);
        }
        this.filteredPieceCache = new WeightedList<JigsawPiece>();
        pattern.rawTemplates.forEach(weightedPiece -> {
            if (this.isApplicable((JigsawPiece)weightedPiece.getFirst())) {
                this.filteredPieceCache.add((JigsawPiece)weightedPiece.getFirst(), (int)weightedPiece.getSecond());
            }
            return;
        });
        return this.getRandomPiece(pattern, random);
    }
    
    private boolean isApplicable(final JigsawPiece piece) {
        if (piece instanceof PalettedListPoolElement) {
            final List<JigsawPiece> elements = ((PalettedListPoolElement)piece).getElements();
            for (final JigsawPiece elementPiece : elements) {
                if (!this.isApplicable(elementPiece)) {
                    return false;
                }
            }
            return !elements.isEmpty();
        }
        if (piece instanceof PalettedSinglePoolElement) {
            final ResourceLocation key = ((PalettedSinglePoolElement)piece).getTemplate().left().orElse(null);
            if (key != null) {
                return this.roomPieceFilter.test(key);
            }
        }
        return false;
    }
}
