// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import iskallia.vault.Vault;
import iskallia.vault.container.inventory.ShardPouchContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ShardPouchScreen extends ContainerScreen<ShardPouchContainer>
{
    private static final ResourceLocation TEXTURE;
    
    public ShardPouchScreen(final ShardPouchContainer screenContainer, final PlayerInventory inv, final ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageWidth = 176;
        this.imageHeight = 137;
        this.titleLabelX = 33;
        this.inventoryLabelY = 45;
    }
    
    protected void renderBg(final MatrixStack matrixStack, final float partialTicks, final int x, final int y) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bind(ShardPouchScreen.TEXTURE);
        final int offsetX = (this.width - this.imageWidth) / 2;
        final int offsetY = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, offsetX, offsetY, 0, 0, this.imageWidth, this.imageHeight);
    }
    
    public void render(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }
    
    public boolean isPauseScreen() {
        return false;
    }
    
    static {
        TEXTURE = Vault.id("textures/gui/shard_pouch.png");
    }
}
