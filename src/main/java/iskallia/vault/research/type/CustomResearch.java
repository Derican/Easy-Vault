// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.research.type;

import com.google.gson.annotations.Expose;
import iskallia.vault.research.Restrictions;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class CustomResearch extends Research
{
    @Expose
    protected Map<String, Restrictions> itemRestrictions;
    @Expose
    protected Map<String, Restrictions> blockRestrictions;
    @Expose
    protected Map<String, Restrictions> entityRestrictions;
    
    public CustomResearch(final String name, final int cost) {
        super(name, cost);
        this.itemRestrictions = new HashMap<String, Restrictions>();
        this.blockRestrictions = new HashMap<String, Restrictions>();
        this.entityRestrictions = new HashMap<String, Restrictions>();
    }
    
    public Map<String, Restrictions> getItemRestrictions() {
        return this.itemRestrictions;
    }
    
    public Map<String, Restrictions> getBlockRestrictions() {
        return this.blockRestrictions;
    }
    
    public Map<String, Restrictions> getEntityRestrictions() {
        return this.entityRestrictions;
    }
    
    @Override
    public boolean restricts(final Item item, final Restrictions.Type restrictionType) {
        final ResourceLocation registryName = item.getRegistryName();
        if (registryName == null) {
            return false;
        }
        final String sid = registryName.getNamespace() + ":" + registryName.getPath();
        final Restrictions restrictions = this.itemRestrictions.get(sid);
        return restrictions != null && restrictions.restricts(restrictionType);
    }
    
    @Override
    public boolean restricts(final Block block, final Restrictions.Type restrictionType) {
        final ResourceLocation registryName = block.getRegistryName();
        if (registryName == null) {
            return false;
        }
        final String sid = registryName.getNamespace() + ":" + registryName.getPath();
        final Restrictions restrictions = this.blockRestrictions.get(sid);
        return restrictions != null && restrictions.restricts(restrictionType);
    }
    
    @Override
    public boolean restricts(final EntityType<?> entityType, final Restrictions.Type restrictionType) {
        final ResourceLocation registryName = entityType.getRegistryName();
        if (registryName == null) {
            return false;
        }
        final String sid = registryName.getNamespace() + ":" + registryName.getPath();
        final Restrictions restrictions = this.entityRestrictions.get(sid);
        return restrictions != null && restrictions.restricts(restrictionType);
    }
}
