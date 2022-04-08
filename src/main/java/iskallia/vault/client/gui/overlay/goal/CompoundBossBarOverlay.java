package iskallia.vault.client.gui.overlay.goal;

import com.mojang.blaze3d.matrix.MatrixStack;

public class CompoundBossBarOverlay
        extends BossBarOverlay {
    private final BossBarOverlay first;
    private final BossBarOverlay second;

    public CompoundBossBarOverlay(BossBarOverlay first, BossBarOverlay second) {
        this.first = first;
        this.second = second;
    }


    public boolean shouldDisplay() {
        return ((this.first != null && this.first.shouldDisplay()) || (this.second != null && this.second
                .shouldDisplay()));
    }


    public int drawOverlay(MatrixStack renderStack, float pTicks) {
        int height = 0;
        if (this.first != null && this.first.shouldDisplay()) {
            height += this.first.drawOverlay(renderStack, pTicks);
        }
        if (this.second != null && this.second.shouldDisplay()) {
            height += this.second.drawOverlay(renderStack, pTicks);
        }
        return height;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\overlay\goal\CompoundBossBarOverlay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */