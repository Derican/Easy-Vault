package iskallia.vault.client.gui.overlay.goal;

import com.mojang.blaze3d.matrix.MatrixStack;

public abstract class BossBarOverlay {
    public abstract boolean shouldDisplay();

    public abstract int drawOverlay(MatrixStack paramMatrixStack, float paramFloat);
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\overlay\goal\BossBarOverlay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */