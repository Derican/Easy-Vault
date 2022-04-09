// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.util;

import net.minecraft.util.ResourceLocation;

public class ResourceBoundary
{
    ResourceLocation resource;
    int u;
    int v;
    int w;
    int h;
    
    public ResourceBoundary(final ResourceLocation resource, final int u, final int v, final int w, final int h) {
        this.resource = resource;
        this.u = u;
        this.v = v;
        this.w = w;
        this.h = h;
    }
    
    public ResourceLocation getResource() {
        return this.resource;
    }
    
    public int getU() {
        return this.u;
    }
    
    public int getV() {
        return this.v;
    }
    
    public int getW() {
        return this.w;
    }
    
    public int getH() {
        return this.h;
    }
}
