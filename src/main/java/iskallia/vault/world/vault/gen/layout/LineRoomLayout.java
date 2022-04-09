// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.gen.layout;

import iskallia.vault.Vault;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3i;

public class LineRoomLayout extends VaultRoomLayoutGenerator
{
    public static final ResourceLocation ID;
    
    public LineRoomLayout() {
        super(LineRoomLayout.ID);
    }
    
    @Override
    public void setSize(final int size) {
    }
    
    @Override
    public Layout generateLayout() {
        final Layout layout = new Layout();
        layout.putRoom(new Vector3i(0, 0, 0));
        layout.putRoom(new Vector3i(1, 0, 0));
        layout.putRoom(new Vector3i(2, 0, 0));
        layout.putRoom(new Vector3i(3, 0, 0));
        layout.addTunnel(new Tunnel(layout.getRoom(new Vector3i(0, 0, 0)), layout.getRoom(new Vector3i(1, 0, 0))));
        return layout;
    }
    
    static {
        ID = Vault.id("line");
    }
}
