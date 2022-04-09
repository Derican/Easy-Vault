// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.util;

public interface IBiomeAccessor
{
    void setSeed(final long p0);
    
    void setLegacyBiomes(final boolean p0);
    
    void setLargeBiomes(final boolean p0);
    
    long getSeed();
    
    boolean getLegacyBiomes();
    
    boolean getLargeBiomes();
}
