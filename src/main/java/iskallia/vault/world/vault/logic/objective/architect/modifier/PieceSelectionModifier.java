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

public class PieceSelectionModifier
        extends VoteModifier {
    @Expose
    private final float filterChance;
    @Expose
    private final List<String> selectedRoomPrefixes;
    private WeightedList<JigsawPiece> filteredPieces = null;

    public PieceSelectionModifier(String name, String description, int voteLockDurationChangeSeconds, float filterChance, List<String> selectedRoomPrefixes) {
        super(name, description, voteLockDurationChangeSeconds);
        this.filterChance = filterChance;
        this.selectedRoomPrefixes = selectedRoomPrefixes;
    }


    @Nullable
    public JigsawPiece getSpecialRoom(ArchitectObjective objective, VaultRaid vault) {
        if (rand.nextFloat() >= this.filterChance) {
            return super.getSpecialRoom(objective, vault);
        }
        if (this.filteredPieces != null) {
            return (JigsawPiece) this.filteredPieces.getRandom(rand);
        }


        int vaultLevel = ((Integer) vault.getProperties().getBase(VaultRaid.LEVEL).orElse(Integer.valueOf(0))).intValue();
        this.filteredPieces = VaultJigsawHelper.getVaultRoomList(vaultLevel).copyFiltered(this::isApplicable);
        return (JigsawPiece) this.filteredPieces.getRandom(rand);
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
                String keyStr = key.toString();
                for (String prefix : this.selectedRoomPrefixes) {
                    if (keyStr.startsWith(prefix)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\objective\architect\modifier\PieceSelectionModifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */