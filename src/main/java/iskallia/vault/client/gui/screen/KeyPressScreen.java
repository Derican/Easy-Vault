// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.Vault;
import iskallia.vault.container.KeyPressContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class KeyPressScreen extends ContainerScreen<KeyPressContainer>
{
    private static final ResourceLocation GUI_RESOURCE;
    
    public KeyPressScreen(final KeyPressContainer screenContainer, final PlayerInventory inv, final ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }
    
    protected void renderBg(final MatrixStack matrixStack, final float partialTicks, final int x, final int y) {
    }
    
    protected void renderLabels(final MatrixStack matrixStack, final int x, final int y) {
        this.font.draw(matrixStack, (ITextComponent)new StringTextComponent(""), (float)this.titleLabelX, (float)this.titleLabelY, 4210752);
    }
    
    public void render(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        this.renderBackground(matrixStack);
        final float midX = this.width / 2.0f;
        final float midY = this.height / 2.0f;
        final Minecraft minecraft = this.getMinecraft();
        final int containerWidth = 176;
        final int containerHeight = 166;
        minecraft.getTextureManager().bind(KeyPressScreen.GUI_RESOURCE);
        this.blit(matrixStack, (int)(midX - containerWidth / 2), (int)(midY - containerHeight / 2), 0, 0, containerWidth, containerHeight);
        final FontRenderer fontRenderer = minecraft.font;
        final String title = "Mold Vault Keys";
        fontRenderer.draw(matrixStack, title, midX - 35.0f, midY - 63.0f, 4144959);
        final String inventoryTitle = "Inventory";
        fontRenderer.draw(matrixStack, inventoryTitle, midX - 80.0f, midY - 11.0f, 4144959);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }
    
    static {
        GUI_RESOURCE = Vault.id("textures/gui/key-press.png");
    }
}
