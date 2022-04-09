// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import iskallia.vault.container.VaultCrateContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class VaultCrateScreen extends ContainerScreen<VaultCrateContainer>
{
    private static final ResourceLocation TEXTURE;
    
    public VaultCrateScreen(final VaultCrateContainer screenContainer, final PlayerInventory inv, final ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageHeight = 222;
        this.inventoryLabelY = this.imageHeight - 94;
    }
    
    public void render(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }
    
    protected void renderBg(final MatrixStack matrixStack, final float partialTicks, final int x, final int y) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bind(VaultCrateScreen.TEXTURE);
        final int i = (this.width - this.imageWidth) / 2;
        final int j = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, i, j, 0, 0, this.imageWidth, 125);
        this.blit(matrixStack, i, j + 108 + 17, 0, 126, this.imageWidth, 96);
    }
    
    static {
        TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
    }
}
