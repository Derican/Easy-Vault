package iskallia.vault.item.gear.applicable;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class VaultRepairCoreItem
        extends TieredVaultItem {
    public VaultRepairCoreItem(ResourceLocation id, Item.Properties properties, int vaultGearTier, ITextComponent... components) {
        super(id, properties, vaultGearTier, components);
    }

    public VaultRepairCoreItem(ResourceLocation id, Item.Properties properties, int vaultGearTier, List<ITextComponent> components) {
        super(id, properties, vaultGearTier, components);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\gear\applicable\VaultRepairCoreItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */