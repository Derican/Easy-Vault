package iskallia.vault.world.vault.gen;

import iskallia.vault.Vault;
import iskallia.vault.world.gen.structure.pool.PalettedListPoolElement;
import iskallia.vault.world.gen.structure.pool.PalettedSinglePoolElement;
import iskallia.vault.world.vault.gen.layout.VaultRoomLayoutGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;

import java.util.List;


public class VaultRoomLevelRestrictions {
    public static void addGenerationPreventions(VaultRoomLayoutGenerator.Layout layout, int vaultLevel) {
        if (vaultLevel < 250) {
            layout.getRooms().forEach(room -> room.andFilter(()));
        }


        if (vaultLevel < 100) {
            layout.getRooms().forEach(room -> room.andFilter(()));
        }
    }


    public static boolean canGenerate(JigsawPiece vaultPiece, int vaultLevel) {
        if (vaultLevel < 250 && isJigsawPieceOfName(vaultPiece, getVaultRoomPrefix("vendor"))) {
            return false;
        }

        if (vaultLevel < 100 && isJigsawPieceOfName(vaultPiece, getVaultRoomPrefix("contest_pixel"))) {
            return false;
        }
        return true;
    }

    private static String getVaultRoomPrefix(String roomName) {
        return Vault.sId("vault/enigma/rooms/" + roomName);
    }

    private static boolean isJigsawPieceOfName(JigsawPiece piece, String name) {
        if (piece instanceof PalettedListPoolElement) {
            List<JigsawPiece> elements = ((PalettedListPoolElement) piece).getElements();
            for (JigsawPiece elementPiece : elements) {
                if (!isJigsawPieceOfName(elementPiece, name)) {
                    return false;
                }
            }
            return !elements.isEmpty();
        }
        if (piece instanceof PalettedSinglePoolElement) {
            ResourceLocation key = ((PalettedSinglePoolElement) piece).getTemplate().left().orElse(null);
            if (key != null) {
                return key.toString().startsWith(name);
            }
        }
        return false;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\gen\VaultRoomLevelRestrictions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */