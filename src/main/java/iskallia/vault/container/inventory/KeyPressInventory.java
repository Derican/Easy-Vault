package iskallia.vault.container.inventory;

import iskallia.vault.container.base.RecipeInventory;
import iskallia.vault.init.ModConfigs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class KeyPressInventory
        extends RecipeInventory {
    public static final int KEY_SLOT = 0;
    public static final int CLUSTER_SLOT = 1;

    public KeyPressInventory() {
        super(2);
    }


    public boolean recipeFulfilled() {
        Item keyItem = getItem(0).getItem();
        Item clusterItem = getItem(1).getItem();
        return !ModConfigs.KEY_PRESS.getResultFor(keyItem, clusterItem).isEmpty();
    }


    public ItemStack resultingItemStack() {
        Item keyItem = getItem(0).getItem();
        Item clusterItem = getItem(1).getItem();
        return ModConfigs.KEY_PRESS.getResultFor(keyItem, clusterItem);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\container\inventory\KeyPressInventory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */