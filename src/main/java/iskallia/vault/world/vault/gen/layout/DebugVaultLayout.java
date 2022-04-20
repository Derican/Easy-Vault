package iskallia.vault.world.vault.gen.layout;

import iskallia.vault.Vault;
import iskallia.vault.util.data.WeightedList;
import iskallia.vault.world.gen.structure.VaultJigsawHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;

import java.util.Random;

public class DebugVaultLayout extends VaultRoomLayoutGenerator {
    public static final ResourceLocation ID;

    public DebugVaultLayout() {
        super(DebugVaultLayout.ID);
    }

    @Override
    public void setSize(final int size) {
    }

    @Override
    public Layout generateLayout() {
        final Layout layout = new Layout();
        int xx = 0;
        Room previousRoom = null;
        for (final WeightedList.Entry<JigsawPiece> weightedEntry : VaultJigsawHelper.getVaultRoomList(Integer.MAX_VALUE)) {
            final JigsawPiece piece = weightedEntry.value;
            final Room room = new Room(new Vector3i(xx, 0, 0)) {
                @Override
                public JigsawPiece getRandomPiece(final JigsawPattern pattern, final Random random) {
                    return piece;
                }
            };
            ++xx;
            layout.putRoom(room);
            if (previousRoom != null) {
                layout.addTunnel(new Tunnel(previousRoom, room));
            }
            previousRoom = room;
        }
        return layout;
    }

    static {
        ID = Vault.id("debug");
    }
}
