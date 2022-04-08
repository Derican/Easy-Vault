package iskallia.vault.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import iskallia.vault.block.entity.VendingMachineTileEntity;
import iskallia.vault.block.render.VendingMachineRenderer;
import iskallia.vault.client.gui.component.ScrollableContainer;
import iskallia.vault.client.gui.widget.TradeWidget;
import iskallia.vault.container.VendingMachineContainer;
import iskallia.vault.entity.model.StatuePlayerModel;
import iskallia.vault.event.InputEvents;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.VendingUIMessage;
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

public class VendingMachineScreen
        extends ContainerScreen<VendingMachineContainer> {
    public static final ResourceLocation HUD_RESOURCE = new ResourceLocation("the_vault", "textures/gui/vending-machine.png");

    public ScrollableContainer tradesContainer;
    public List<TradeWidget> tradeWidgets;

    public VendingMachineScreen(VendingMachineContainer screenContainer, PlayerInventory inv, ITextComponent title) {
        super( screenContainer, inv, (ITextComponent) new StringTextComponent("Vending Machine"));

        this.tradesContainer = new ScrollableContainer(this::renderTrades);
        this.tradeWidgets = new LinkedList<>();

        refreshWidgets();

        this.imageWidth = 394;
        this.imageHeight = 170;
    }

    public void refreshWidgets() {
        this.tradeWidgets.clear();

        List<TraderCore> cores = ((VendingMachineContainer) getMenu()).getTileEntity().getCores();

        for (int i = 0; i < cores.size(); i++) {
            TraderCore traderCore = cores.get(i);
            int x = 0;
            int y = i * 27;
            this.tradeWidgets.add(new TradeWidget(x, y, traderCore, this));
        }
    }

    public Rectangle getTradeBoundaries() {
        int midX = MathHelper.floor(this.width / 2.0F);
        int midY = MathHelper.floor(this.height / 2.0F);

        return new Rectangle(midX - 134, midY - 66, 100, 142);
    }


    protected void init() {
        super.init();
    }


    public void mouseMoved(double mouseX, double mouseY) {
        Rectangle tradeBoundaries = getTradeBoundaries();

        double tradeContainerX = mouseX - tradeBoundaries.x;
        double tradeContainerY = mouseY - tradeBoundaries.y;

        for (TradeWidget tradeWidget : this.tradeWidgets) {
            tradeWidget.mouseMoved(tradeContainerX, tradeContainerY);
        }

        this.tradesContainer.mouseMoved(mouseX, mouseY);
    }


    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Rectangle tradeBoundaries = getTradeBoundaries();

        double tradeContainerX = mouseX - tradeBoundaries.x;
        double tradeContainerY = mouseY - tradeBoundaries.y + this.tradesContainer.getyOffset();

        for (int i = 0; i < this.tradeWidgets.size(); i++) {
            TradeWidget tradeWidget = this.tradeWidgets.get(i);
            boolean isHovered = (tradeWidget.x <= tradeContainerX && tradeContainerX <= (tradeWidget.x + 88) && tradeWidget.y <= tradeContainerY && tradeContainerY <= (tradeWidget.y + 27));


            if (isHovered) {
                if (InputEvents.isShiftDown()) {
                    ((VendingMachineContainer) getMenu()).ejectCore(i);
                    refreshWidgets();
                    ModNetwork.CHANNEL.sendToServer(VendingUIMessage.ejectTrade(i));
                    Minecraft.getInstance().getSoundManager()
                            .play((ISound) SimpleSound.forUI(SoundEvents.ITEM_PICKUP, 1.0F));
                    break;
                }
                ((VendingMachineContainer) getMenu()).selectTrade(i);
                ModNetwork.CHANNEL.sendToServer(VendingUIMessage.selectTrade(i));
                Minecraft.getInstance().getSoundManager()
                        .play((ISound) SimpleSound.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));


                break;
            }
        }

        this.tradesContainer.mouseClicked(mouseX, mouseY, button);

        return super.mouseClicked(mouseX, mouseY, button);
    }


    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.tradesContainer.mouseReleased(mouseX, mouseY, button);
        return super.mouseReleased(mouseX, mouseY, button);
    }


    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        this.tradesContainer.mouseScrolled(mouseX, mouseY, delta);
        return true;
    }


    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
    }


    protected void renderLabels(MatrixStack matrixStack, int x, int y) {
        this.font.draw(matrixStack, (ITextComponent) new StringTextComponent(""), this.titleLabelX, this.titleLabelY, 4210752);
    }


    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);

        float midX = this.width / 2.0F;
        float midY = this.height / 2.0F;

        Minecraft minecraft = getMinecraft();

        int containerWidth = 276;
        int containerHeight = 166;

        minecraft.getTextureManager().bind(HUD_RESOURCE);
        blit(matrixStack, (int) (midX - (containerWidth / 2)), (int) (midY - (containerHeight / 2)), 0.0F, 0.0F, containerWidth, containerHeight, 512, 256);


        VendingMachineContainer container = (VendingMachineContainer) getMenu();
        VendingMachineTileEntity tileEntity = container.getTileEntity();
        Rectangle tradeBoundaries = getTradeBoundaries();

        this.tradesContainer.setBounds(tradeBoundaries);
        this.tradesContainer.setInnerHeight(27 * this.tradeWidgets.size());

        this.tradesContainer.render(matrixStack, mouseX, mouseY, partialTicks);
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        TraderCore lastCore = tileEntity.getLastCore();
        if (lastCore != null) {
            drawSkin((int) midX + 175, (int) midY - 10, -45, tileEntity.getSkin());
        }

        minecraft.font.draw(matrixStack, "Trades", midX - 108.0F, midY - 77.0F, -12632257);


        if (lastCore != null) {
            String name = "Vendor - " + lastCore.getName();
            int nameWidth = minecraft.font.width(name);
            minecraft.font.draw(matrixStack, name, midX + 50.0F - nameWidth / 2.0F, midY - 70.0F, -12632257);
        }


        renderTooltip(matrixStack, mouseX, mouseY);
    }


    public void renderTrades(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Rectangle tradeBoundaries = getTradeBoundaries();

        int tradeContainerX = mouseX - tradeBoundaries.x;
        int tradeContainerY = mouseY - tradeBoundaries.y + this.tradesContainer.getyOffset();

        for (TradeWidget tradeWidget : this.tradeWidgets) {
            tradeWidget.render(matrixStack, tradeContainerX, tradeContainerY, partialTicks);
        }
    }


    protected void renderTooltip(MatrixStack matrixStack, int mouseX, int mouseY) {
        Rectangle tradeBoundaries = getTradeBoundaries();

        int tradeContainerX = mouseX - tradeBoundaries.x;
        int tradeContainerY = mouseY - tradeBoundaries.y + this.tradesContainer.getyOffset();

        for (TradeWidget tradeWidget : this.tradeWidgets) {
            if (tradeWidget.isHovered(tradeContainerX, tradeContainerY)) {
                Trade trade = tradeWidget.getTraderCode().getTrade();
                if (trade.getTradesLeft() != 0) {
                    ItemStack sellStack = trade.getSell().toStack();
                    renderTooltip(matrixStack, sellStack, mouseX, mouseY);
                    continue;
                }
                StringTextComponent text = new StringTextComponent("Sold out, sorry!");
                text.setStyle(Style.EMPTY.withColor(Color.fromRgb(16711680)));
                renderTooltip(matrixStack, (ITextComponent) text, mouseX, mouseY);
            }
        }


        super.renderTooltip(matrixStack, mouseX, mouseY);
    }

    public static void drawSkin(int posX, int posY, int yRotation, SkinProfile skin) {
        float scale = 8.0F;
        RenderSystem.pushMatrix();
        RenderSystem.translatef(posX, posY, 1050.0F);
        RenderSystem.scalef(1.0F, 1.0F, -1.0F);
        MatrixStack matrixStack = new MatrixStack();
        matrixStack.translate(0.0D, 0.0D, 1000.0D);
        matrixStack.scale(scale, scale, scale);
        Quaternion quaternion = Vector3f.ZP.rotationDegrees(200.0F);
        Quaternion quaternion1 = Vector3f.XP.rotationDegrees(45.0F);
        quaternion.mul(quaternion1);

        EntityRendererManager entityrenderermanager = Minecraft.getInstance().getEntityRenderDispatcher();
        quaternion1.conj();
        entityrenderermanager.overrideCameraOrientation(quaternion1);
        entityrenderermanager.setRenderShadow(false);
        IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().renderBuffers().bufferSource();
        StatuePlayerModel<PlayerEntity> model = VendingMachineRenderer.PLAYER_MODEL;
        RenderSystem.runAsFancy(() -> {
            matrixStack.scale(scale, scale, scale);

            matrixStack.mulPose(Vector3f.XP.rotationDegrees(20.0F));

            matrixStack.mulPose(Vector3f.YN.rotationDegrees(yRotation));

            int lighting = 15728640;
            int overlay = 983040;
            RenderType renderType = model.renderType(skin.getLocationSkin());
            IVertexBuilder vertexBuilder = irendertypebuffer$impl.getBuffer(renderType);
            model.body.render(matrixStack, vertexBuilder, lighting, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
            model.leftLeg.render(matrixStack, vertexBuilder, lighting, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
            model.rightLeg.render(matrixStack, vertexBuilder, lighting, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
            model.leftArm.render(matrixStack, vertexBuilder, lighting, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
            model.rightArm.render(matrixStack, vertexBuilder, lighting, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
            model.jacket.render(matrixStack, vertexBuilder, lighting, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
            model.leftPants.render(matrixStack, vertexBuilder, lighting, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
            model.rightPants.render(matrixStack, vertexBuilder, lighting, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
            model.leftSleeve.render(matrixStack, vertexBuilder, lighting, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStack.pushPose();
            matrixStack.translate(0.0D, 0.0D, -0.6200000047683716D);
            model.rightSleeve.render(matrixStack, vertexBuilder, lighting, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStack.popPose();
            model.hat.render(matrixStack, vertexBuilder, lighting, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
            model.head.render(matrixStack, vertexBuilder, lighting, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStack.popPose();
        });
        irendertypebuffer$impl.endBatch();
        entityrenderermanager.setRenderShadow(true);
        RenderSystem.popMatrix();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\screen\VendingMachineScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */