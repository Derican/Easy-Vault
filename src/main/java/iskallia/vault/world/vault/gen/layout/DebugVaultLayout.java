package iskallia.vault.world.vault.gen.layout;

import iskallia.vault.Vault;
import iskallia.vault.util.data.WeightedList;
import iskallia.vault.world.gen.structure.VaultJigsawHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;

import java.util.Random;

public class DebugVaultLayout
        extends VaultRoomLayoutGenerator {
    public static final ResourceLocation ID = Vault.id("debug");

    public DebugVaultLayout() {
        super(ID);
    }


    public void setSize(int size) {
    }


    public VaultRoomLayoutGenerator.Layout generateLayout() {
        VaultRoomLayoutGenerator.Layout layout = new VaultRoomLayoutGenerator.Layout();

        int xx = 0;
        VaultRoomLayoutGenerator.Room previousRoom = null;
        for (WeightedList.Entry<JigsawPiece> weightedEntry : (Iterable<WeightedList.Entry<JigsawPiece>>) VaultJigsawHelper.getVaultRoomList(2147483647)) {
            final JigsawPiece piece = (JigsawPiece) weightedEntry.value;
            VaultRoomLayoutGenerator.Room room = new VaultRoomLayoutGenerator.Room(new Vector3i(xx, 0, 0)) {
                public JigsawPiece getRandomPiece(JigsawPattern pattern, Random random) {
                    return piece;
                }
            };
            xx++;

            layout.putRoom(room);
            if (previousRoom != null) {
                layout.addTunnel(new VaultRoomLayoutGenerator.Tunnel(previousRoom, room));
            }
            previousRoom = room;
        }
        return layout;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\gen\layout\DebugVaultLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */