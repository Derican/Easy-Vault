// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.util;

public enum StatueType
{
    GIFT_NORMAL, 
    GIFT_MEGA, 
    VAULT_BOSS, 
    OMEGA, 
    TROPHY, 
    OMEGA_VARIANT;
    
    public float getPlayerRenderYOffset() {
        return 0.9f;
    }
    
    public boolean isOmega() {
        return this == StatueType.OMEGA || this == StatueType.OMEGA_VARIANT;
    }
    
    public boolean doGrayscaleShader() {
        return !this.isOmega() && this != StatueType.TROPHY;
    }
    
    public boolean doesStatueCauldronAccept() {
        return !this.isOmega() && this != StatueType.TROPHY;
    }
    
    public boolean hasLimitedItems() {
        return !this.isOmega() && this != StatueType.TROPHY;
    }
    
    public boolean dropsItems() {
        return this != StatueType.TROPHY;
    }
    
    public boolean allowsRenaming() {
        return this != StatueType.TROPHY;
    }
}
