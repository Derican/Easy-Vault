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

public class ModResearch extends Research {
    @Expose
    protected Set<String> modIds;
    @Expose
    protected Restrictions restrictions;

    public ModResearch(String name, int cost, String... modIds) {
        super(name, cost);
        this.modIds = new HashSet<>();
        this.restrictions = Restrictions.forMods();

        Collections.addAll(this.modIds, modIds);
    }

    public Set<String> getModIds() {
        return this.modIds;
    }

    public Restrictions getRestrictions() {
        return this.restrictions;
    }


    public ModResearch withRestrictions(boolean hittability, boolean entityIntr, boolean blockIntr, boolean usability, boolean craftability) {
        this.restrictions.set(Restrictions.Type.HITTABILITY, hittability);
        this.restrictions.set(Restrictions.Type.ENTITY_INTERACTABILITY, entityIntr);
        this.restrictions.set(Restrictions.Type.BLOCK_INTERACTABILITY, blockIntr);
        this.restrictions.set(Restrictions.Type.USABILITY, usability);
        this.restrictions.set(Restrictions.Type.CRAFTABILITY, craftability);
        return this;
    }


    public boolean restricts(Item item, Restrictions.Type restrictionType) {
        if (!this.restrictions.restricts(restrictionType)) return false;
        ResourceLocation registryName = item.getRegistryName();
        if (registryName == null) return false;
        return this.modIds.contains(registryName.getNamespace());
    }


    public boolean restricts(Block block, Restrictions.Type restrictionType) {
        if (!this.restrictions.restricts(restrictionType)) return false;
        ResourceLocation registryName = block.getRegistryName();
        if (registryName == null) return false;
        return this.modIds.contains(registryName.getNamespace());
    }


    public boolean restricts(EntityType<?> entityType, Restrictions.Type restrictionType) {
        if (!this.restrictions.restricts(restrictionType)) return false;
        ResourceLocation registryName = entityType.getRegistryName();
        if (registryName == null) return false;
        return this.modIds.contains(registryName.getNamespace());
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\research\type\ModResearch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */