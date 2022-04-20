package iskallia.vault.world.vault.gen.layout;

import iskallia.vault.Vault;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3i;

public class SingularVaultRoomLayout extends VaultRoomLayoutGenerator {
    public static final ResourceLocation ID;

    public SingularVaultRoomLayout() {
        super(SingularVaultRoomLayout.ID);
    }

    @Override
    public void setSize(final int size) {
    }

    @Override
    public Layout generateLayout() {
        final Layout layout = new Layout();
        layout.putRoom(new Vector3i(0, 0, 0));
        return layout;
    }

    static {
        ID = Vault.id("singular");
    }
}
