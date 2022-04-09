// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.client.gui.overlay.goal;

import com.mojang.blaze3d.matrix.MatrixStack;

public abstract class BossBarOverlay
{
    public abstract boolean shouldDisplay();
    
    public abstract int drawOverlay(final MatrixStack p0, final float p1);
}
