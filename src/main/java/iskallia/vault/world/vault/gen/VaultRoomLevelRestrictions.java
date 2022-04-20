package iskallia.vault.world.vault.gen;

import iskallia.vault.Vault;
import iskallia.vault.world.gen.structure.pool.PalettedListPoolElement;
import iskallia.vault.world.gen.structure.pool.PalettedSinglePoolElement;
import iskallia.vault.world.vault.gen.layout.VaultRoomLayoutGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;

import java.util.List;

public class VaultRoomLevelRestrictions {
    public static void addGenerationPreventions(final VaultRoomLayoutGenerator.Layout layout, final int vaultLevel) {
        if (vaultLevel < 250) {
            layout.getRooms().forEach(room -> room.andFilter(key -> !key.toString().startsWith(getVaultRoomPrefix("vendor"))));
        }
        if (vaultLevel < 100) {
            layout.getRooms().forEach(room -> room.andFilter(key -> !key.toString().startsWith(getVaultRoomPrefix("contest_pixel"))));
        }
    }

    public static boolean canGenerate(final JigsawPiece vaultPiece, final int vaultLevel) {
        return (vaultLevel >= 250 || !isJigsawPieceOfName(vaultPiece, getVaultRoomPrefix("vendor"))) && (vaultLevel >= 100 || !isJigsawPieceOfName(vaultPiece, getVaultRoomPrefix("contest_pixel")));
    }

    private static String getVaultRoomPrefix(final String roomName) {
        return Vault.sId("vault/enigma/rooms/" + roomName);
    }

    private static boolean isJigsawPieceOfName(final JigsawPiece piece, final String name) {
        if (piece instanceof PalettedListPoolElement) {
            final List<JigsawPiece> elements = ((PalettedListPoolElement) piece).getElements();
            for (final JigsawPiece elementPiece : elements) {
                if (!isJigsawPieceOfName(elementPiece, name)) {
                    return false;
                }
            }
            return !elements.isEmpty();
        }
        if (piece instanceof PalettedSinglePoolElement) {
            final ResourceLocation key = ((PalettedSinglePoolElement) piece).getTemplate().left().orElse(null);
            if (key != null) {
                return key.toString().startsWith(name);
            }
        }
        return false;
    }
}
