package iskallia.vault.item.paxel;

import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemTier;
import net.minecraft.item.crafting.Ingredient;

public class PaxelItemTier
        implements IItemTier {
    public static final PaxelItemTier INSTANCE = new PaxelItemTier();


    public int getUses() {
        return 6000;
    }


    public float getSpeed() {
        return ItemTier.NETHERITE.getSpeed() + 1.0F;
    }


    public float getAttackDamageBonus() {
        return ItemTier.NETHERITE.getAttackDamageBonus() + 1.0F;
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
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\paxel\PaxelItemTier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */