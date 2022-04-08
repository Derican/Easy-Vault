package iskallia.vault.block.entity;

import iskallia.vault.init.ModBlocks;
import iskallia.vault.util.SkinProfile;

public class AdvancedVendingTileEntity
        extends VendingMachineTileEntity {
    public AdvancedVendingTileEntity() {
        super(ModBlocks.ADVANCED_VENDING_MACHINE_TILE_ENTITY);
        this.skin = new SkinProfile();
    }

    public void updateSkin(String name) {
        this.skin.updateSkin(name);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\entity\AdvancedVendingTileEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */