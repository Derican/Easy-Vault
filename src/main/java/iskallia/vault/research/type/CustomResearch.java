package iskallia.vault.research.type;

import com.google.gson.annotations.Expose;
import iskallia.vault.research.Restrictions;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;


public class CustomResearch
        extends Research {
    @Expose
    protected Map<String, Restrictions> itemRestrictions;

    public CustomResearch(String name, int cost) {
        super(name, cost);
        this.itemRestrictions = new HashMap<>();
        this.blockRestrictions = new HashMap<>();
        this.entityRestrictions = new HashMap<>();
    }

    @Expose
    protected Map<String, Restrictions> blockRestrictions;
    @Expose
    protected Map<String, Restrictions> entityRestrictions;

    public Map<String, Restrictions> getItemRestrictions() {
        return this.itemRestrictions;
    }

    public Map<String, Restrictions> getBlockRestrictions() {
        return this.blockRestrictions;
    }

    public Map<String, Restrictions> getEntityRestrictions() {
        return this.entityRestrictions;
    }


    public boolean restricts(Item item, Restrictions.Type restrictionType) {
        ResourceLocation registryName = item.getRegistryName();
        if (registryName == null) return false;
        String sid = registryName.getNamespace() + ":" + registryName.getPath();
        Restrictions restrictions = this.itemRestrictions.get(sid);
        if (restrictions == null) return false;
        return restrictions.restricts(restrictionType);
    }


    public boolean restricts(Block block, Restrictions.Type restrictionType) {
        ResourceLocation registryName = block.getRegistryName();
        if (registryName == null) return false;
        String sid = registryName.getNamespace() + ":" + registryName.getPath();
        Restrictions restrictions = this.blockRestrictions.get(sid);
        if (restrictions == null) return false;
        return restrictions.restricts(restrictionType);
    }


    public boolean restricts(EntityType<?> entityType, Restrictions.Type restrictionType) {
        ResourceLocation registryName = entityType.getRegistryName();
        if (registryName == null) return false;
        String sid = registryName.getNamespace() + ":" + registryName.getPath();
        Restrictions restrictions = this.entityRestrictions.get(sid);
        if (restrictions == null) return false;
        return restrictions.restricts(restrictionType);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\research\type\CustomResearch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */