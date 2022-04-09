// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.block.entity;

import iskallia.vault.init.ModBlocks;
import iskallia.vault.util.SkinProfile;

public class AdvancedVendingTileEntity extends VendingMachineTileEntity
{
    public AdvancedVendingTileEntity() {
        super(ModBlocks.ADVANCED_VENDING_MACHINE_TILE_ENTITY);
        this.skin = new SkinProfile();
    }
    
    public void updateSkin(final String name) {
        this.skin.updateSkin(name);
    }
}
