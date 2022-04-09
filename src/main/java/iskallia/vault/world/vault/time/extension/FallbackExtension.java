// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.time.extension;

import iskallia.vault.Vault;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

public class FallbackExtension extends TimeExtension
{
    public static final ResourceLocation ID;
    protected CompoundNBT fallback;
    
    public FallbackExtension() {
    }
    
    public FallbackExtension(final CompoundNBT fallback) {
        super(FallbackExtension.ID, 0L);
        this.deserializeNBT(fallback);
    }
    
    public CompoundNBT getFallback() {
        return this.fallback;
    }
    
    @Override
    public CompoundNBT serializeNBT() {
        return this.fallback;
    }
    
    @Override
    public void deserializeNBT(final CompoundNBT nbt) {
        this.fallback = nbt;
        this.extraTime = this.getFallback().getLong("ExtraTime");
        this.executionTime = this.getFallback().getLong("ExecutionTime");
    }
    
    static {
        ID = Vault.id("fallback");
    }
}
