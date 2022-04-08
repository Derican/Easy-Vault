package iskallia.vault.research;

import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.Map;


public class Restrictions {
    @Expose
    protected Map<Type, Boolean> restricts = new HashMap<>();


    public Restrictions set(Type type, boolean restricts) {
        this.restricts.put(type, Boolean.valueOf(restricts));
        return this;
    }

    public boolean restricts(Type type) {
        return ((Boolean) this.restricts.getOrDefault(type, Boolean.valueOf(false))).booleanValue();
    }

    public static Restrictions forMods() {
        Restrictions restrictions = new Restrictions();
        restrictions.restricts.put(Type.USABILITY, Boolean.valueOf(false));
        restrictions.restricts.put(Type.CRAFTABILITY, Boolean.valueOf(false));
        restrictions.restricts.put(Type.HITTABILITY, Boolean.valueOf(false));
        restrictions.restricts.put(Type.BLOCK_INTERACTABILITY, Boolean.valueOf(false));
        restrictions.restricts.put(Type.ENTITY_INTERACTABILITY, Boolean.valueOf(false));
        return restrictions;
    }

    public static Restrictions forItems(boolean restricted) {
        Restrictions restrictions = new Restrictions();
        restrictions.restricts.put(Type.USABILITY, Boolean.valueOf(restricted));
        restrictions.restricts.put(Type.CRAFTABILITY, Boolean.valueOf(restricted));
        restrictions.restricts.put(Type.HITTABILITY, Boolean.valueOf(restricted));
        return restrictions;
    }

    public static Restrictions forBlocks(boolean restricted) {
        Restrictions restrictions = new Restrictions();
        restrictions.restricts.put(Type.HITTABILITY, Boolean.valueOf(restricted));
        restrictions.restricts.put(Type.BLOCK_INTERACTABILITY, Boolean.valueOf(restricted));
        return restrictions;
    }

    public static Restrictions forEntities(boolean restricted) {
        Restrictions restrictions = new Restrictions();
        restrictions.restricts.put(Type.HITTABILITY, Boolean.valueOf(restricted));
        restrictions.restricts.put(Type.ENTITY_INTERACTABILITY, Boolean.valueOf(restricted));
        return restrictions;
    }

    public enum Type {
        USABILITY,
        CRAFTABILITY,
        HITTABILITY,
        BLOCK_INTERACTABILITY,
        ENTITY_INTERACTABILITY;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\research\Restrictions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */