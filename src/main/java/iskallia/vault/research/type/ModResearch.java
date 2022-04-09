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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ModResearch extends Research
{
    @Expose
    protected Set<String> modIds;
    @Expose
    protected Restrictions restrictions;
    
    public ModResearch(final String name, final int cost, final String... modIds) {
        super(name, cost);
        this.modIds = new HashSet<String>();
        this.restrictions = Restrictions.forMods();
        Collections.addAll(this.modIds, modIds);
    }
    
    public Set<String> getModIds() {
        return this.modIds;
    }
    
    public Restrictions getRestrictions() {
        return this.restrictions;
    }
    
    public ModResearch withRestrictions(final boolean hittability, final boolean entityIntr, final boolean blockIntr, final boolean usability, final boolean craftability) {
        this.restrictions.set(Restrictions.Type.HITTABILITY, hittability);
        this.restrictions.set(Restrictions.Type.ENTITY_INTERACTABILITY, entityIntr);
        this.restrictions.set(Restrictions.Type.BLOCK_INTERACTABILITY, blockIntr);
        this.restrictions.set(Restrictions.Type.USABILITY, usability);
        this.restrictions.set(Restrictions.Type.CRAFTABILITY, craftability);
        return this;
    }
    
    @Override
    public boolean restricts(final Item item, final Restrictions.Type restrictionType) {
        if (!this.restrictions.restricts(restrictionType)) {
            return false;
        }
        final ResourceLocation registryName = item.getRegistryName();
        return registryName != null && this.modIds.contains(registryName.getNamespace());
    }
    
    @Override
    public boolean restricts(final Block block, final Restrictions.Type restrictionType) {
        if (!this.restrictions.restricts(restrictionType)) {
            return false;
        }
        final ResourceLocation registryName = block.getRegistryName();
        return registryName != null && this.modIds.contains(registryName.getNamespace());
    }
    
    @Override
    public boolean restricts(final EntityType<?> entityType, final Restrictions.Type restrictionType) {
        if (!this.restrictions.restricts(restrictionType)) {
            return false;
        }
        final ResourceLocation registryName = entityType.getRegistryName();
        return registryName != null && this.modIds.contains(registryName.getNamespace());
    }
}
