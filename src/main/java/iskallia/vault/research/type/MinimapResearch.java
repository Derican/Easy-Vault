// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.research.type;

import iskallia.vault.research.Restrictions;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;

public class MinimapResearch extends Research
{
    public MinimapResearch(final String name, final int cost) {
        super(name, cost);
    }
    
    @Override
    public boolean restricts(final Item item, final Restrictions.Type restrictionType) {
        return false;
    }
    
    @Override
    public boolean restricts(final Block block, final Restrictions.Type restrictionType) {
        return false;
    }
    
    @Override
    public boolean restricts(final EntityType<?> entityType, final Restrictions.Type restrictionType) {
        return false;
    }
}
