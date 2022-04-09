// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import iskallia.vault.block.entity.StatueCauldronTileEntity;
import iskallia.vault.block.render.VendingMachineRenderer;
import iskallia.vault.client.gui.component.ScrollableContainer;
import iskallia.vault.client.gui.widget.StatueWidget;
import iskallia.vault.entity.model.StatuePlayerModel;
import iskallia.vault.util.SkinProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.awt.*;
import java.util.List;
import java.util.*;

public class StatueCauldronScreen extends Screen
{
    public static final ResourceLocation HUD_RESOURCE;
    public ScrollableContainer statuesContainer;
    public List<StatueWidget> statueWidgets;
    private SkinProfile selected;
    private final ClientWorld world;
    private final BlockPos pos;
    private int xSize;
    private int ySize;
    
    public StatueCauldronScreen(final ClientWorld world, final BlockPos pos) {
        super((ITextComponent)new StringTextComponent("Statue Cauldron"));
        this.world = world;
        this.pos = pos;
        this.selected = new SkinProfile();
        this.statueWidgets = new LinkedList<StatueWidget>();
        this.statuesContainer = new ScrollableContainer(this::renderNames);
        this.refreshWidgets();
        this.xSize = 220;
        this.ySize = 166;
    }
    
    public boolean isPauseScreen() {
        return false;
    }
    
    public boolean keyPressed(final int keyCode, final int scanCode, final int modifiers) {
        if (keyCode == 256 || keyCode == 69) {
            final PlayerEntity player = (PlayerEntity)Minecraft.getInstance().player;
            if (player != null) {
                player.closeContainer();
            }
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    
    public SkinProfile getSelected() {
        return this.selected;
    }
    
    private StatueCauldronTileEntity getTileEntity() {
        final TileEntity tileEntity = this.world.getBlockEntity(this.pos);
        if (!(tileEntity instanceof StatueCauldronTileEntity)) {
            return null;
        }
        return (StatueCauldronTileEntity)tileEntity;
    }
    
    public void render(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        final StatueCauldronTileEntity statue = this.getTileEntity();
        if (statue == null) {
            this.onClose();
            return;
        }
        this.refreshWidgets();
        final float midX = this.width / 2.0f;
        final float midY = this.height / 2.0f;
        this.renderBackground(matrixStack, 0);
        this.minecraft.getTextureManager().bind(StatueCauldronScreen.HUD_RESOURCE);
        blit(matrixStack, (int)(midX - this.xSize / 2), (int)(midY - this.ySize / 2), 0.0f, 0.0f, this.xSize, this.ySize, 512, 256);
        this.renderTitle(matrixStack);
        if (!this.statueWidgets.isEmpty()) {
            if (this.selected.getLatestNickname() == null || this.selected.getLatestNickname().isEmpty()) {
                this.selected.updateSkin(this.statueWidgets.get(0).getName());
            }
            drawSkin((int)midX + 46, (int)midY - 22, -45, this.selected, false);
        }
        final Rectangle boundaries = this.getViewportBoundaries();
        this.statuesContainer.setBounds(boundaries);
        this.statuesContainer.setInnerHeight(27 * this.statueWidgets.size());
        this.statuesContainer.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderProgressBar(matrixStack, statue, midX, midY);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }
    
    private void renderProgressBar(final MatrixStack matrixStack, final StatueCauldronTileEntity statue, final float midX, final float midY) {
        final float progress = statue.getStatueCount() / (float)statue.getRequiredAmount();
        final int percent = MathHelper.floor(progress * 100.0f);
        final float startX = midX + 97.0f - this.font.width(percent + "%") / 2.0f;
        final float startY = midY - 78.0f;
        this.font.draw(matrixStack, (ITextComponent)new StringTextComponent(percent + "%"), startX, startY, 4210752);
        final int barHeight = 140;
        final int textureHeight = MathHelper.floor(barHeight * progress);
        final float barX = midX + 89.0f;
        final float barY = midY - 65.0f + (barHeight - textureHeight);
        this.minecraft.getTextureManager().bind(StatueCauldronScreen.HUD_RESOURCE);
        blit(matrixStack, (int)barX, (int)barY, 314.0f, 0.0f, 16, textureHeight, 512, 256);
    }
    
    public void refreshWidgets() {
        this.statueWidgets.clear();
        final List<String> names = this.getTileEntity().getNames();
        final Set<String> uniqueNames = new HashSet<String>(names);
        HashMap<String, Integer> counts = new HashMap<String, Integer>();
        for (final String uniqueName : uniqueNames) {
            final int amount = Collections.frequency(names, uniqueName);
            counts.put(uniqueName, amount);
        }
        counts = sortByValue(counts);
        int index = 0;
        for (final String name : counts.keySet()) {
            final int count = counts.get(name);
            final int x = 0;
            final int y = index * 27;
            this.statueWidgets.add(new StatueWidget(x, y, name, count, this));
            ++index;
        }
    }
    
    public static HashMap<String, Integer> sortByValue(final HashMap<String, Integer> map) {
        final List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());
        Collections.reverse(list);
        final HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
        for (final Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }
    
    public void renderNames(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        final Rectangle boundaries = this.getViewportBoundaries();
        final int containerX = mouseX - boundaries.x;
        final int containerY = mouseY - boundaries.y + this.statuesContainer.getyOffset();
        for (final StatueWidget statueWidget : this.statueWidgets) {
            statueWidget.render(matrixStack, containerX, containerY, partialTicks);
        }
    }
    
    public Rectangle getViewportBoundaries() {
        final int midX = MathHelper.floor(this.width / 2.0f);
        final int midY = MathHelper.floor(this.height / 2.0f);
        return new Rectangle(midX - 106, midY - 66, 100, 142);
    }
    
    public static void drawSkin(final int posX, final int posY, final int yRotation, final SkinProfile skin, final boolean megahead) {
        final float scale = 8.0f;
        final float headScale = megahead ? 1.75f : 1.0f;
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
            matrixStack.scale(headScale, headScale, headScale);
            model.hat.render(matrixStack, vertexBuilder, lighting, overlay, 1.0f, 1.0f, 1.0f, 1.0f);
            model.head.render(matrixStack, vertexBuilder, lighting, overlay, 1.0f, 1.0f, 1.0f, 1.0f);
            matrixStack.popPose();
            return;
        });
        irendertypebuffer$impl.endBatch();
        entityrenderermanager.setRenderShadow(true);
        RenderSystem.popMatrix();
    }
    
    public void mouseMoved(final double mouseX, final double mouseY) {
        final Rectangle boundaries = this.getViewportBoundaries();
        final double containerX = mouseX - boundaries.x;
        final double containerY = mouseY - boundaries.y;
        for (final StatueWidget statueWidget : this.statueWidgets) {
            statueWidget.mouseMoved(containerX, containerY);
        }
        this.statuesContainer.mouseMoved(mouseX, mouseY);
    }
    
    public boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
        final Rectangle boundaries = this.getViewportBoundaries();
        final double tradeContainerX = mouseX - boundaries.x;
        final double tradeContainerY = mouseY - boundaries.y + this.statuesContainer.getyOffset();
        for (int i = 0; i < this.statueWidgets.size(); ++i) {
            final StatueWidget statueWidget = this.statueWidgets.get(i);
            final boolean isHovered = statueWidget.x <= tradeContainerX && tradeContainerX <= statueWidget.x + 88 && statueWidget.y <= tradeContainerY && tradeContainerY <= statueWidget.y + 27;
            if (isHovered) {
                this.selected.updateSkin(statueWidget.getName());
                Minecraft.getInstance().getSoundManager().play((ISound)SimpleSound.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0f));
                break;
            }
        }
        this.statuesContainer.mouseClicked(mouseX, mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    public boolean mouseReleased(final double mouseX, final double mouseY, final int button) {
        this.statuesContainer.mouseReleased(mouseX, mouseY, button);
        return super.mouseReleased(mouseX, mouseY, button);
    }
    
    public boolean mouseScrolled(final double mouseX, final double mouseY, final double delta) {
        this.statuesContainer.mouseScrolled(mouseX, mouseY, delta);
        return true;
    }
    
    private void renderTitle(final MatrixStack matrixStack) {
        final int i = MathHelper.floor(this.width / 2.0f);
        final int j = MathHelper.floor(this.height / 2.0f);
        final float startX = i - this.font.width(this.title.getString()) / 2.0f;
        final float startY = (float)(j - 78);
        this.font.draw(matrixStack, this.title, startX, startY, 4210752);
    }
    
    static {
        HUD_RESOURCE = new ResourceLocation("the_vault", "textures/gui/statue_cauldron.png");
    }
}
