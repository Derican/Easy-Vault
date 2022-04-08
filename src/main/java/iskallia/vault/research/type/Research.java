package iskallia.vault.research.type;

import com.google.gson.annotations.Expose;
import iskallia.vault.research.Restrictions;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;

public abstract class Research {
    @Expose
    protected String name;
    @Expose
    protected int cost;

    public Research(String name, int cost) {
        this.name = name;
        this.cost = cost;
    }

    @Expose
    protected boolean usesKnowledge;
    @Expose
    protected String gatedBy;

    public String getName() {
        return this.name;
    }

    public int getCost() {
        return this.cost;
    }

    public boolean isGated() {
        return (this.gatedBy != null);
    }

    public String gatedBy() {
        return this.gatedBy;
    }

    public boolean usesKnowledge() {
        return this.usesKnowledge;
    }

    public abstract boolean restricts(Item paramItem, Restrictions.Type paramType);

    public abstract boolean restricts(Block paramBlock, Restrictions.Type paramType);

    public abstract boolean restricts(EntityType<?> paramEntityType, Restrictions.Type paramType);
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\research\type\Research.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */