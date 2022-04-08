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
import net.minecraft.client.entity.player.ClientPlayerEntity;
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

public class StatueCauldronScreen extends Screen {
    public static final ResourceLocation HUD_RESOURCE = new ResourceLocation("the_vault", "textures/gui/statue_cauldron.png");

    public ScrollableContainer statuesContainer;

    public List<StatueWidget> statueWidgets;

    private SkinProfile selected;

    private final ClientWorld world;
    private final BlockPos pos;
    private int xSize;
    private int ySize;

    public StatueCauldronScreen(ClientWorld world, BlockPos pos) {
        super((ITextComponent) new StringTextComponent("Statue Cauldron"));
        this.world = world;
        this.pos = pos;
        this.selected = new SkinProfile();

        this.statueWidgets = new LinkedList<>();
        this.statuesContainer = new ScrollableContainer(this::renderNames);

        refreshWidgets();

        this.xSize = 220;
        this.ySize = 166;
    }


    public boolean isPauseScreen() {
        return false;
    }


    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256 || keyCode == 69) {
            ClientPlayerEntity clientPlayerEntity = (Minecraft.getInstance()).player;
            if (clientPlayerEntity != null) {
                clientPlayerEntity.closeContainer();
            }
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public SkinProfile getSelected() {
        return this.selected;
    }

    private StatueCauldronTileEntity getTileEntity() {
        TileEntity tileEntity = this.world.getBlockEntity(this.pos);
        if (!(tileEntity instanceof StatueCauldronTileEntity)) {
            return null;
        }
        return (StatueCauldronTileEntity) tileEntity;
    }


    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        StatueCauldronTileEntity statue = getTileEntity();
        if (statue == null) {
            onClose();

            return;
        }

        refreshWidgets();

        float midX = this.width / 2.0F;
        float midY = this.height / 2.0F;

        renderBackground(matrixStack, 0);

        this.minecraft.getTextureManager().bind(HUD_RESOURCE);
        blit(matrixStack, (int) (midX - (this.xSize / 2)), (int) (midY - (this.ySize / 2)), 0.0F, 0.0F, this.xSize, this.ySize, 512, 256);


        renderTitle(matrixStack);

        if (!this.statueWidgets.isEmpty()) {
            if (this.selected.getLatestNickname() == null || this.selected.getLatestNickname().isEmpty()) {
                this.selected.updateSkin(((StatueWidget) this.statueWidgets.get(0)).getName());
            }

            drawSkin((int) midX + 46, (int) midY - 22, -45, this.selected, false);
        }

        Rectangle boundaries = getViewportBoundaries();

        this.statuesContainer.setBounds(boundaries);
        this.statuesContainer.setInnerHeight(27 * this.statueWidgets.size());

        this.statuesContainer.render(matrixStack, mouseX, mouseY, partialTicks);

        renderProgressBar(matrixStack, statue, midX, midY);

        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    private void renderProgressBar(MatrixStack matrixStack, StatueCauldronTileEntity statue, float midX, float midY) {
        float progress = statue.getStatueCount() / statue.getRequiredAmount();
        int percent = MathHelper.floor(progress * 100.0F);
        float startX = midX + 97.0F - this.font.width(percent + "%") / 2.0F;
        float startY = midY - 78.0F;
        this.font.draw(matrixStack, (ITextComponent) new StringTextComponent(percent + "%"), startX, startY, 4210752);

        int barHeight = 140;
        int textureHeight = MathHelper.floor(barHeight * progress);

        float barX = midX + 89.0F;
        float barY = midY - 65.0F + (barHeight - textureHeight);
        this.minecraft.getTextureManager().bind(HUD_RESOURCE);
        blit(matrixStack, (int) barX, (int) barY, 314.0F, 0.0F, 16, textureHeight, 512, 256);
    }


    public void refreshWidgets() {
        this.statueWidgets.clear();

        List<String> names = getTileEntity().getNames();

        Set<String> uniqueNames = new HashSet<>(names);
        HashMap<String, Integer> counts = new HashMap<>();

        for (String uniqueName : uniqueNames) {
            int amount = Collections.frequency(names, uniqueName);
            counts.put(uniqueName, Integer.valueOf(amount));
        }

        counts = sortByValue(counts);

        int index = 0;

        for (String name : counts.keySet()) {
            int count = ((Integer) counts.get(name)).intValue();
            int x = 0;
            int y = index * 27;
            this.statueWidgets.add(new StatueWidget(x, y, name, count, this));
            index++;
        }
    }


    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> map) {
        List<Map.Entry<String, Integer>> list = new LinkedList<>(map.entrySet());

        list.sort((Comparator) Map.Entry.comparingByValue());

        Collections.reverse(list);

        HashMap<String, Integer> temp = new LinkedHashMap<>();

        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }

        return temp;
    }

    public void renderNames(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Rectangle boundaries = getViewportBoundaries();

        int containerX = mouseX - boundaries.x;
        int containerY = mouseY - boundaries.y + this.statuesContainer.getyOffset();

        for (StatueWidget statueWidget : this.statueWidgets) {
            statueWidget.render(matrixStack, containerX, containerY, partialTicks);
        }
    }

    public Rectangle getViewportBoundaries() {
        int midX = MathHelper.floor(this.width / 2.0F);
        int midY = MathHelper.floor(this.height / 2.0F);

        return new Rectangle(midX - 106, midY - 66, 100, 142);
    }

    public static void drawSkin(int posX, int posY, int yRotation, SkinProfile skin, boolean megahead) {
        float scale = 8.0F;
        float headScale = megahead ? 1.75F : 1.0F;
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
            matrixStack.scale(headScale, headScale, headScale);
            model.hat.render(matrixStack, vertexBuilder, lighting, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
            model.head.render(matrixStack, vertexBuilder, lighting, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStack.popPose();
        });
        irendertypebuffer$impl.endBatch();
        entityrenderermanager.setRenderShadow(true);
        RenderSystem.popMatrix();
    }


    public void mouseMoved(double mouseX, double mouseY) {
        Rectangle boundaries = getViewportBoundaries();

        double containerX = mouseX - boundaries.x;
        double containerY = mouseY - boundaries.y;

        for (StatueWidget statueWidget : this.statueWidgets) {
            statueWidget.mouseMoved(containerX, containerY);
        }

        this.statuesContainer.mouseMoved(mouseX, mouseY);
    }


    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Rectangle boundaries = getViewportBoundaries();

        double tradeContainerX = mouseX - boundaries.x;
        double tradeContainerY = mouseY - boundaries.y + this.statuesContainer.getyOffset();

        for (int i = 0; i < this.statueWidgets.size(); i++) {
            StatueWidget statueWidget = this.statueWidgets.get(i);
            boolean isHovered = (statueWidget.x <= tradeContainerX && tradeContainerX <= (statueWidget.x + 88) && statueWidget.y <= tradeContainerY && tradeContainerY <= (statueWidget.y + 27));


            if (isHovered) {
                this.selected.updateSkin(statueWidget.getName());
                Minecraft.getInstance().getSoundManager()
                        .play((ISound) SimpleSound.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));

                break;
            }
        }

        this.statuesContainer.mouseClicked(mouseX, mouseY, button);

        return super.mouseClicked(mouseX, mouseY, button);
    }


    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.statuesContainer.mouseReleased(mouseX, mouseY, button);
        return super.mouseReleased(mouseX, mouseY, button);
    }


    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        this.statuesContainer.mouseScrolled(mouseX, mouseY, delta);
        return true;
    }

    private void renderTitle(MatrixStack matrixStack) {
        int i = MathHelper.floor(this.width / 2.0F);
        int j = MathHelper.floor(this.height / 2.0F);
        float startX = i - this.font.width(this.title.getString()) / 2.0F;
        float startY = (j - 78);
        this.font.draw(matrixStack, this.title, startX, startY, 4210752);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\screen\StatueCauldronScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */