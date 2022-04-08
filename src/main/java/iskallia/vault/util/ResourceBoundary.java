package iskallia.vault.util;

import net.minecraft.util.ResourceLocation;


public class ResourceBoundary {
    ResourceLocation resource;
    int u;

    public ResourceBoundary(ResourceLocation resource, int u, int v, int w, int h) {
        this.resource = resource;
        this.u = u;
        this.v = v;
        this.w = w;
        this.h = h;
    }

    int v;
    int w;
    int h;

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


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\ResourceBoundary.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */