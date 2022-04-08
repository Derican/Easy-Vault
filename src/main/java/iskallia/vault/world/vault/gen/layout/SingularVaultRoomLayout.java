package iskallia.vault.world.vault.gen.layout;

import iskallia.vault.Vault;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3i;

public class SingularVaultRoomLayout
        extends VaultRoomLayoutGenerator {
    public static final ResourceLocation ID = Vault.id("singular");

    public SingularVaultRoomLayout() {
        super(ID);
    }


    public void setSize(int size) {
    }


    public VaultRoomLayoutGenerator.Layout generateLayout() {
        VaultRoomLayoutGenerator.Layout layout = new VaultRoomLayoutGenerator.Layout();
        layout.putRoom(new Vector3i(0, 0, 0));
        return layout;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\gen\layout\SingularVaultRoomLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */