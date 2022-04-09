// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.item.paxel;

import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemTier;
import net.minecraft.item.crafting.Ingredient;

public class PaxelItemTier implements IItemTier
{
    public static final PaxelItemTier INSTANCE;
    
    public int getUses() {
        return 6000;
    }
    
    public float getSpeed() {
        return ItemTier.NETHERITE.getSpeed() + 1.0f;
    }
    
    public float getAttackDamageBonus() {
        return ItemTier.NETHERITE.getAttackDamageBonus() + 1.0f;
    }
    
    public int getLevel() {
        return ItemTier.NETHERITE.getLevel();
    }
    
    public int getEnchantmentValue() {
        return ItemTier.NETHERITE.getEnchantmentValue() + 3;
    }
    
    public Ingredient getRepairIngredient() {
        return Ingredient.EMPTY;
    }
    
    static {
        INSTANCE = new PaxelItemTier();
    }
}
