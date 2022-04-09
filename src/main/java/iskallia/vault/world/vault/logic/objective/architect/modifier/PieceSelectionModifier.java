// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.logic.objective.architect.modifier;

import com.google.gson.annotations.Expose;
import iskallia.vault.util.data.WeightedList;
import iskallia.vault.world.gen.structure.VaultJigsawHelper;
import iskallia.vault.world.gen.structure.pool.PalettedListPoolElement;
import iskallia.vault.world.gen.structure.pool.PalettedSinglePoolElement;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.architect.ArchitectObjective;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;

import javax.annotation.Nullable;
import java.util.List;

public class PieceSelectionModifier extends VoteModifier
{
    @Expose
    private final float filterChance;
    @Expose
    private final List<String> selectedRoomPrefixes;
    private WeightedList<JigsawPiece> filteredPieces;
    
    public PieceSelectionModifier(final String name, final String description, final int voteLockDurationChangeSeconds, final float filterChance, final List<String> selectedRoomPrefixes) {
        super(name, description, voteLockDurationChangeSeconds);
        this.filteredPieces = null;
        this.filterChance = filterChance;
        this.selectedRoomPrefixes = selectedRoomPrefixes;
    }
    
    @Nullable
    @Override
    public JigsawPiece getSpecialRoom(final ArchitectObjective objective, final VaultRaid vault) {
        if (PieceSelectionModifier.rand.nextFloat() >= this.filterChance) {
            return super.getSpecialRoom(objective, vault);
        }
        if (this.filteredPieces != null) {
            return this.filteredPieces.getRandom(PieceSelectionModifier.rand);
        }
        final int vaultLevel = vault.getProperties().getBase(VaultRaid.LEVEL).orElse(0);
        this.filteredPieces = VaultJigsawHelper.getVaultRoomList(vaultLevel).copyFiltered(this::isApplicable);
        return this.filteredPieces.getRandom(PieceSelectionModifier.rand);
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
                final String keyStr = key.toString();
                for (final String prefix : this.selectedRoomPrefixes) {
                    if (keyStr.startsWith(prefix)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
