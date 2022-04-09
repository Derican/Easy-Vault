// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.research;

import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.Map;

public class Restrictions
{
    @Expose
    protected Map<Type, Boolean> restricts;
    
    private Restrictions() {
        this.restricts = new HashMap<Type, Boolean>();
    }
    
    public Restrictions set(final Type type, final boolean restricts) {
        this.restricts.put(type, restricts);
        return this;
    }
    
    public boolean restricts(final Type type) {
        return this.restricts.getOrDefault(type, false);
    }
    
    public static Restrictions forMods() {
        final Restrictions restrictions = new Restrictions();
        restrictions.restricts.put(Type.USABILITY, false);
        restrictions.restricts.put(Type.CRAFTABILITY, false);
        restrictions.restricts.put(Type.HITTABILITY, false);
        restrictions.restricts.put(Type.BLOCK_INTERACTABILITY, false);
        restrictions.restricts.put(Type.ENTITY_INTERACTABILITY, false);
        return restrictions;
    }
    
    public static Restrictions forItems(final boolean restricted) {
        final Restrictions restrictions = new Restrictions();
        restrictions.restricts.put(Type.USABILITY, restricted);
        restrictions.restricts.put(Type.CRAFTABILITY, restricted);
        restrictions.restricts.put(Type.HITTABILITY, restricted);
        return restrictions;
    }
    
    public static Restrictions forBlocks(final boolean restricted) {
        final Restrictions restrictions = new Restrictions();
        restrictions.restricts.put(Type.HITTABILITY, restricted);
        restrictions.restricts.put(Type.BLOCK_INTERACTABILITY, restricted);
        return restrictions;
    }
    
    public static Restrictions forEntities(final boolean restricted) {
        final Restrictions restrictions = new Restrictions();
        restrictions.restricts.put(Type.HITTABILITY, restricted);
        restrictions.restricts.put(Type.ENTITY_INTERACTABILITY, restricted);
        return restrictions;
    }
    
    public enum Type
    {
        USABILITY, 
        CRAFTABILITY, 
        HITTABILITY, 
        BLOCK_INTERACTABILITY, 
        ENTITY_INTERACTABILITY;
    }
}
