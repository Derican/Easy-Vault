// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.item.gear.applicable;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class VaultRepairCoreItem extends TieredVaultItem
{
    public VaultRepairCoreItem(final ResourceLocation id, final Item.Properties properties, final int vaultGearTier, final ITextComponent... components) {
        super(id, properties, vaultGearTier, components);
    }
    
    public VaultRepairCoreItem(final ResourceLocation id, final Item.Properties properties, final int vaultGearTier, final List<ITextComponent> components) {
        super(id, properties, vaultGearTier, components);
    }
}
