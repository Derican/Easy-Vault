// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import iskallia.vault.block.entity.AdvancedVendingTileEntity;
import iskallia.vault.block.render.VendingMachineRenderer;
import iskallia.vault.client.gui.component.ScrollableContainer;
import iskallia.vault.client.gui.widget.AdvancedTradeWidget;
import iskallia.vault.container.AdvancedVendingContainer;
import iskallia.vault.entity.model.StatuePlayerModel;
import iskallia.vault.event.InputEvents;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.AdvancedVendingUIMessage;
import iskallia.vault.util.SkinProfile;
import iskallia.vault.vending.Trade;
import iskallia.vault.vending.TraderCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class AdvancedVendingMachineScreen extends ContainerScreen<AdvancedVendingContainer>
{
    public static final ResourceLocation HUD_RESOURCE;
    public ScrollableContainer tradesContainer;
    public List<AdvancedTradeWidget> tradeWidgets;
    public SkinProfile skin;
    
    public AdvancedVendingMachineScreen(final AdvancedVendingContainer screenContainer, final PlayerInventory inv, final ITextComponent title) {
        super(screenContainer, inv, (ITextComponent)new StringTextComponent("Advanced Vending Machine"));
        this.skin = new SkinProfile();
        this.tradesContainer = new ScrollableContainer(this::renderTrades);
        this.tradeWidgets = new LinkedList<AdvancedTradeWidget>();
        this.refreshWidgets();
        this.imageWidth = 394;
        this.imageHeight = 170;
    }
    
    public void refreshWidgets() {
        this.tradeWidgets.clear();
        final List<TraderCore> cores = ((AdvancedVendingContainer)this.getMenu()).getTileEntity().getCores();
        for (int i = 0; i < cores.size(); ++i) {
            final TraderCore traderCore = cores.get(i);
            final int x = 0;
            final int y = i * 27;
            this.tradeWidgets.add(new AdvancedTradeWidget(x, y, traderCore, this));
        }
    }
    
    public Rectangle getTradeBoundaries() {
        final int midX = MathHelper.floor(this.width / 2.0f);
        final int midY = MathHelper.floor(this.height / 2.0f);
        return new Rectangle(midX - 134, midY - 66, 100, 142);
    }
    
    protected void init() {
        super.init();
    }
    
    public void mouseMoved(final double mouseX, final double mouseY) {
        final Rectangle tradeBoundaries = this.getTradeBoundaries();
        final double tradeContainerX = mouseX - tradeBoundaries.x;
        final double tradeContainerY = mouseY - tradeBoundaries.y;
        for (final AdvancedTradeWidget tradeWidget : this.tradeWidgets) {
            tradeWidget.mouseMoved(tradeContainerX, tradeContainerY);
        }
        this.tradesContainer.mouseMoved(mouseX, mouseY);
    }
    
    public boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
        final Rectangle tradeBoundaries = this.getTradeBoundaries();
        final double tradeContainerX = mouseX - tradeBoundaries.x;
        final double tradeContainerY = mouseY - tradeBoundaries.y + this.tradesContainer.getyOffset();
        int i = 0;
        while (i < this.tradeWidgets.size()) {
            final AdvancedTradeWidget tradeWidget = this.tradeWidgets.get(i);
            final boolean isHovered = tradeWidget.x <= tradeContainerX && tradeContainerX <= tradeWidget.x + 88 && tradeWidget.y <= tradeContainerY && tradeContainerY <= tradeWidget.y + 27;
            if (isHovered) {
                if (InputEvents.isShiftDown()) {
                    ((AdvancedVendingContainer)this.getMenu()).ejectCore(i);
                    this.refreshWidgets();
                    ModNetwork.CHANNEL.sendToServer(AdvancedVendingUIMessage.ejectTrade(i));
                    Minecraft.getInstance().getSoundManager().play((ISound)SimpleSound.forUI(SoundEvents.ITEM_PICKUP, 1.0f));
                    break;
                }
                ((AdvancedVendingContainer)this.getMenu()).selectTrade(i);
                ModNetwork.CHANNEL.sendToServer(AdvancedVendingUIMessage.selectTrade(i));
                Minecraft.getInstance().getSoundManager().play((ISound)SimpleSound.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0f));
                break;
            }
            else {
                ++i;
            }
        }
        this.tradesContainer.mouseClicked(mouseX, mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    public boolean mouseReleased(final double mouseX, final double mouseY, final int button) {
        this.tradesContainer.mouseReleased(mouseX, mouseY, button);
        return super.mouseReleased(mouseX, mouseY, button);
    }
    
    public boolean mouseScrolled(final double mouseX, final double mouseY, final double delta) {
        this.tradesContainer.mouseScrolled(mouseX, mouseY, delta);
        return true;
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
        final int containerWidth = 276;
        final int containerHeight = 166;
        minecraft.getTextureManager().bind(AdvancedVendingMachineScreen.HUD_RESOURCE);
        blit(matrixStack, (int)(midX - containerWidth / 2), (int)(midY - containerHeight / 2), 0.0f, 0.0f, containerWidth, containerHeight, 512, 256);
        final AdvancedVendingContainer container = (AdvancedVendingContainer)this.getMenu();
        final AdvancedVendingTileEntity tileEntity = container.getTileEntity();
        final Rectangle tradeBoundaries = this.getTradeBoundaries();
        this.tradesContainer.setBounds(tradeBoundaries);
        this.tradesContainer.setInnerHeight(27 * this.tradeWidgets.size());
        this.tradesContainer.render(matrixStack, mouseX, mouseY, partialTicks);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        final TraderCore coreToRender = container.getSelectedTrade();
        if (coreToRender != null) {
            this.skin.updateSkin(coreToRender.getName());
        }
        if (coreToRender != null) {
            drawSkin((int)midX + 175, (int)midY - 10, -45, this.skin);
        }
        minecraft.font.draw(matrixStack, "Trades", midX - 108.0f, midY - 77.0f, -12632257);
        if (coreToRender != null) {
            final String name = "Vendor - " + coreToRender.getName();
            final int nameWidth = minecraft.font.width(name);
            minecraft.font.draw(matrixStack, name, midX + 50.0f - nameWidth / 2.0f, midY - 70.0f, -12632257);
        }
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }
    
    public void renderTrades(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        final Rectangle tradeBoundaries = this.getTradeBoundaries();
        final int tradeContainerX = mouseX - tradeBoundaries.x;
        final int tradeContainerY = mouseY - tradeBoundaries.y + this.tradesContainer.getyOffset();
        for (final AdvancedTradeWidget tradeWidget : this.tradeWidgets) {
            tradeWidget.render(matrixStack, tradeContainerX, tradeContainerY, partialTicks);
        }
    }
    
    protected void renderTooltip(final MatrixStack matrixStack, final int mouseX, final int mouseY) {
        final Rectangle tradeBoundaries = this.getTradeBoundaries();
        final int tradeContainerX = mouseX - tradeBoundaries.x;
        final int tradeContainerY = mouseY - tradeBoundaries.y + this.tradesContainer.getyOffset();
        for (final AdvancedTradeWidget tradeWidget : this.tradeWidgets) {
            if (tradeWidget.isHovered(tradeContainerX, tradeContainerY)) {
                final Trade trade = tradeWidget.getTraderCode().getTrade();
                if (trade.getTradesLeft() != 0) {
                    final ItemStack sellStack = trade.getSell().toStack();
                    this.renderTooltip(matrixStack, sellStack, mouseX, mouseY);
                }
                else {
                    final StringTextComponent text = new StringTextComponent("Sold out, sorry!");
                    text.setStyle(Style.EMPTY.withColor(Color.fromRgb(16711680)));
                    this.renderTooltip(matrixStack, (ITextComponent)text, mouseX, mouseY);
                }
            }
        }
        super.renderTooltip(matrixStack, mouseX, mouseY);
    }
    
    public static void drawSkin(final int posX, final int posY, final int yRotation, final SkinProfile skin) {
        final float scale = 8.0f;
        RenderSystem.pushMatrix();
        RenderSystem.translatef((float)posX, (float)posY, 1050.0f);
        RenderSystem.scalef(1.0f, 1.0f, -1.0f);
        final MatrixStack matrixStack = new MatrixStack();
        matrixStack.translate(0.0, 0.0, 1000.0);
        matrixStack.scale(scale, scale, scale);
        final Quaternion quaternion = Vector3f.ZP.rotationDegrees(200.0f);
        final Quaternion quaternion2 = Vector3f.XP.rotationDegrees(45.0f);
        quaternion.mul(quaternion2);
        final EntityRendererManager entityrenderermanager = Minecraft.getInstance().getEntityRenderDispatcher();
        quaternion2.conj();
        entityrenderermanager.overrideCameraOrientation(quaternion2);
        entityrenderermanager.setRenderShadow(false);
        final IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().renderBuffers().bufferSource();
        final StatuePlayerModel<PlayerEntity> model = VendingMachineRenderer.PLAYER_MODEL;
        RenderSystem.runAsFancy(() -> {
            matrixStack.scale(scale, scale, scale);
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(20.0f));
            matrixStack.mulPose(Vector3f.YN.rotationDegrees((float)yRotation));
            final int lighting = 15728640;
            final int overlay = 983040;
            final RenderType renderType = model.renderType(skin.getLocationSkin());
            final IVertexBuilder vertexBuilder = irendertypebuffer$impl.getBuffer(renderType);
            model.body.render(matrixStack, vertexBuilder, lighting, overlay, 1.0f, 1.0f, 1.0f, 1.0f);
            model.leftLeg.render(matrixStack, vertexBuilder, lighting, overlay, 1.0f, 1.0f, 1.0f, 1.0f);
            model.rightLeg.render(matrixStack, vertexBuilder, lighting, overlay, 1.0f, 1.0f, 1.0f, 1.0f);
            model.leftArm.render(matrixStack, vertexBuilder, lighting, overlay, 1.0f, 1.0f, 1.0f, 1.0f);
            model.rightArm.render(matrixStack, vertexBuilder, lighting, overlay, 1.0f, 1.0f, 1.0f, 1.0f);
            model.jacket.render(matrixStack, vertexBuilder, lighting, overlay, 1.0f, 1.0f, 1.0f, 1.0f);
            model.leftPants.render(matrixStack, vertexBuilder, lighting, overlay, 1.0f, 1.0f, 1.0f, 1.0f);
            model.rightPants.render(matrixStack, vertexBuilder, lighting, overlay, 1.0f, 1.0f, 1.0f, 1.0f);
            model.leftSleeve.render(matrixStack, vertexBuilder, lighting, overlay, 1.0f, 1.0f, 1.0f, 1.0f);
            matrixStack.pushPose();
            matrixStack.translate(0.0, 0.0, -0.6200000047683716);
            model.rightSleeve.render(matrixStack, vertexBuilder, lighting, overlay, 1.0f, 1.0f, 1.0f, 1.0f);
            matrixStack.popPose();
            model.hat.render(matrixStack, vertexBuilder, lighting, overlay, 1.0f, 1.0f, 1.0f, 1.0f);
            model.head.render(matrixStack, vertexBuilder, lighting, overlay, 1.0f, 1.0f, 1.0f, 1.0f);
            matrixStack.popPose();
            return;
        });
        irendertypebuffer$impl.endBatch();
        entityrenderermanager.setRenderShadow(true);
        RenderSystem.popMatrix();
    }
    
    static {
        HUD_RESOURCE = new ResourceLocation("the_vault", "textures/gui/vending-machine.png");
    }
}
