package iskallia.vault.world.vault.gen.layout;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3i;

public abstract class ConnectedRoomGenerator extends VaultRoomLayoutGenerator {
    protected ConnectedRoomGenerator(final ResourceLocation id) {
        super(id);
    }

    public void connectRooms(final Layout layout, final int size) {
        for (int halfSize = size / 2, xx = -halfSize; xx <= halfSize; ++xx) {
            for (int zz = -halfSize; zz <= halfSize; ++zz) {
                final Room middle = layout.getRoom(new Vector3i(xx, 0, zz));
                if (middle != null) {
                    if (xx != -1 || zz != 0) {
                        final Room right = layout.getRoom(new Vector3i(xx + 1, 0, zz));
                        if (right != null) {
                            layout.addTunnel(middle, right);
                        }
                    }
                    final Room up = layout.getRoom(new Vector3i(xx, 0, zz + 1));
                    if (up != null) {
                        layout.addTunnel(middle, up);
                    }
                }
            }
        }
    }
}
