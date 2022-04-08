package iskallia.vault.util;

public enum StatueType {
    GIFT_NORMAL,
    GIFT_MEGA,
    VAULT_BOSS,
    OMEGA,
    TROPHY,
    OMEGA_VARIANT;

    public float getPlayerRenderYOffset() {
        return 0.9F;
    }

    public boolean isOmega() {
        return (this == OMEGA || this == OMEGA_VARIANT);
    }

    public boolean doGrayscaleShader() {
        return (!isOmega() && this != TROPHY);
    }

    public boolean doesStatueCauldronAccept() {
        return (!isOmega() && this != TROPHY);
    }

    public boolean hasLimitedItems() {
        return (!isOmega() && this != TROPHY);
    }

    public boolean dropsItems() {
        return (this != TROPHY);
    }

    public boolean allowsRenaming() {
        return (this != TROPHY);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\StatueType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */