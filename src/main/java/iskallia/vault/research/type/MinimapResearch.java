package iskallia.vault.research.type;

import iskallia.vault.research.Restrictions;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;

public class MinimapResearch
        extends Research {
    public MinimapResearch(String name, int cost) {
        super(name, cost);
    }


    public boolean restricts(Item item, Restrictions.Type restrictionType) {
        return false;
    }


    public boolean restricts(Block block, Restrictions.Type restrictionType) {
        return false;
    }


    public boolean restricts(EntityType<?> entityType, Restrictions.Type restrictionType) {
        return false;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\research\type\MinimapResearch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */