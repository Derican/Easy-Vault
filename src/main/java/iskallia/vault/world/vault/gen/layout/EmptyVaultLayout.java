// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.gen.layout;

import iskallia.vault.Vault;
import net.minecraft.util.ResourceLocation;

public class EmptyVaultLayout extends VaultRoomLayoutGenerator
{
    public static final ResourceLocation ID;
    
    public EmptyVaultLayout() {
        super(EmptyVaultLayout.ID);
    }
    
    @Override
    public void setSize(final int size) {
    }
    
    @Override
    public Layout generateLayout() {
        return new Layout();
    }
    
    static {
        ID = Vault.id("empty");
    }
}
