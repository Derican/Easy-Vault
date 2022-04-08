package iskallia.vault.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.client.gui.component.*;
import iskallia.vault.client.gui.helper.FontHelper;
import iskallia.vault.client.gui.overlay.VaultBarOverlay;
import iskallia.vault.client.gui.tab.SkillTab;
import iskallia.vault.container.SkillTreeContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class SkillTreeScreen extends ContainerScreen<SkillTreeContainer> {
    public static final ResourceLocation HUD_RESOURCE = new ResourceLocation("the_vault", "textures/gui/vault-hud.png");
    public static final ResourceLocation UI_RESOURCE = new ResourceLocation("the_vault", "textures/gui/ability-tree.png");
    public static final ResourceLocation BACKGROUNDS_RESOURCE = new ResourceLocation("the_vault", "textures/gui/ability-tree-bgs.png");

    public static final int TAB_WIDTH = 28;

    public static final int GAP = 3;
    private final List<ComponentDialog> dialogs = new ArrayList<>();
    protected Tuple<SkillTab, ComponentDialog> activeTabTpl;

    public SkillTreeScreen(SkillTreeContainer container, PlayerInventory inventory, ITextComponent title) {
        super((Container) container, inventory, (ITextComponent) new StringTextComponent("Ability Tree Screen"));

        PlayerStatisticsDialog statisticsDialog = new PlayerStatisticsDialog(this);
        this.dialogs.add(statisticsDialog);
        this.dialogs.add(new AbilityDialog(((SkillTreeContainer) getMenu()).getAbilityTree(), this));
        this.dialogs.add(new TalentDialog(((SkillTreeContainer) getMenu()).getTalentTree(), this));
        this.dialogs.add(new ResearchDialog(((SkillTreeContainer) getMenu()).getResearchTree(), this));

        selectDialog((ComponentDialog) statisticsDialog);

        this.imageWidth = 270;
        this.imageHeight = 200;
    }

    private void selectDialog(ComponentDialog dialog) {
        this.activeTabTpl = new Tuple(dialog.createTab(), dialog);
        refreshWidgets();
    }


    protected void init() {
        this.imageWidth = this.width;
        super.init();
    }

    public void refreshWidgets() {
        ((SkillTab) this.activeTabTpl.getA()).refresh();
        this.dialogs.forEach(ComponentDialog::refreshWidgets);
    }

    public Rectangle getContainerBounds() {
        return new Rectangle(30, 60, (int) (this.width * 0.55F) - 30, this.height - 30 - 60);
    }

    public Rectangle getTabBounds(int index, boolean active) {
        Rectangle containerBounds = getContainerBounds();
        return new Rectangle(containerBounds.x + 5 + index * 31, containerBounds.y - 25 - (active ? 21 : 17), 28, active ? 32 : 25);
    }


    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Rectangle containerBounds = getContainerBounds();

        if (containerBounds.contains(mouseX, mouseY)) {
            ((SkillTab) this.activeTabTpl.getA()).mouseClicked(mouseX, mouseY, button);
        } else {
            boolean updatedTab = false;
            ComponentDialog activeDialog = (ComponentDialog) this.activeTabTpl.getB();
            for (int i = 0; i < this.dialogs.size(); i++) {
                ComponentDialog thisDialog = this.dialogs.get(i);
                Rectangle tabBounds = getTabBounds(i, activeDialog.equals(thisDialog));

                if (tabBounds.contains(mouseX, mouseY)) {
                    SkillTab activeTab = (SkillTab) this.activeTabTpl.getA();
                    activeTab.removed();
                    selectDialog(thisDialog);

                    updatedTab = true;
                }
            }
            if (!updatedTab) {
                activeDialog.mouseClicked(mouseX, mouseY, button);
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }


    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        ((SkillTab) this.activeTabTpl.getA()).mouseReleased(mouseX, mouseY, button);
        return super.mouseReleased(mouseX, mouseY, button);
    }


    public void mouseMoved(double mouseX, double mouseY) {
        ((SkillTab) this.activeTabTpl.getA()).mouseMoved(mouseX, mouseY);
        ((ComponentDialog) this.activeTabTpl.getB()).mouseMoved((int) mouseX, (int) mouseY);
    }


    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (getContainerBounds().contains((int) mouseX, (int) mouseY)) {
            ((SkillTab) this.activeTabTpl.getA()).mouseScrolled(mouseX, mouseY, delta);
        } else {
            ((ComponentDialog) this.activeTabTpl.getB()).mouseScrolled(mouseX, mouseY, delta);
        }

        return super.mouseScrolled(mouseX, mouseY, delta);
    }


    public void removed() {
        ((SkillTab) this.activeTabTpl.getA()).removed();
    }


    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        renderBackground(matrixStack);
    }


    protected void renderLabels(MatrixStack matrixStack, int x, int y) {
    }


    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        Rectangle containerBounds = getContainerBounds();
        List<Runnable> postRender = ((SkillTab) this.activeTabTpl.getA()).renderTab(containerBounds, matrixStack, mouseX, mouseY, partialTicks);

        renderSkillPointOverlay(matrixStack);
        renderKnowledgePointOverlay(matrixStack);
        renderContainerBorders(matrixStack);
        renderContainerTabs(matrixStack);
        renderVaultLevelBar(matrixStack);

        int x = containerBounds.x + containerBounds.width + 15;
        int y = containerBounds.y - 18;
        Rectangle dialogBounds = new Rectangle(x, y, this.width - 21 - x, this.height - 21 - y);

        ((ComponentDialog) this.activeTabTpl.getB()).setBounds(dialogBounds);
        ((ComponentDialog) this.activeTabTpl.getB()).render(matrixStack, mouseX, mouseY, partialTicks);

        postRender.forEach(Runnable::run);
    }

    private void renderSkillPointOverlay(MatrixStack matrixStack) {
        if (VaultBarOverlay.unspentSkillPoints > 0) {
            Minecraft mc = Minecraft.getInstance();


            IReorderingProcessor bidiTxt = (new StringTextComponent("")).append((ITextComponent) (new StringTextComponent(String.valueOf(VaultBarOverlay.unspentSkillPoints))).withStyle(TextFormatting.YELLOW)).append(" unspent skill point" + ((VaultBarOverlay.unspentSkillPoints == 1) ? "" : "s")).getVisualOrderText();
            int unspentWidth = mc.font.width(bidiTxt) + 5;

            mc.font.drawShadow(matrixStack, bidiTxt, (mc
                    .getWindow().getGuiScaledWidth() - unspentWidth), 18.0F, -1);
        }
    }


    private void renderKnowledgePointOverlay(MatrixStack matrixStack) {
        if (VaultBarOverlay.unspentKnowledgePoints > 0) {
            Minecraft mc = Minecraft.getInstance();


            IReorderingProcessor bidiTxt = (new StringTextComponent("")).append((ITextComponent) (new StringTextComponent(String.valueOf(VaultBarOverlay.unspentKnowledgePoints))).withStyle(TextFormatting.AQUA)).append(" unspent knowledge point" + ((VaultBarOverlay.unspentKnowledgePoints == 1) ? "" : "s")).getVisualOrderText();
            int unspentWidth = mc.font.width(bidiTxt) + 5;

            matrixStack.pushPose();
            if (VaultBarOverlay.unspentSkillPoints > 0) {
                matrixStack.translate(0.0D, 12.0D, 0.0D);
            }
            mc.font.drawShadow(matrixStack, bidiTxt, (mc
                    .getWindow().getGuiScaledWidth() - unspentWidth), 18.0F, -1);

            matrixStack.popPose();
        }
    }

    private void renderVaultLevelBar(MatrixStack matrixStack) {
        Rectangle containerBounds = getContainerBounds();

        Minecraft minecraft = getMinecraft();
        minecraft.textureManager.bind(VaultBarOverlay.VAULT_HUD_SPRITE);

        String text = String.valueOf(VaultBarOverlay.vaultLevel);
        int textWidth = minecraft.font.width(text);
        int barWidth = 85;
        float expPercentage = VaultBarOverlay.vaultExp / VaultBarOverlay.tnl;

        int barX = containerBounds.x + containerBounds.width - barWidth - 5;
        int barY = containerBounds.y - 10;

        minecraft.gui.blit(matrixStack, barX, barY, 1, 1, barWidth, 5);


        minecraft.gui.blit(matrixStack, barX, barY, 1, 7, (int) (barWidth * expPercentage), 5);


        FontHelper.drawStringWithBorder(matrixStack, text, (barX - textWidth - 1), (barY - 1), -6601, -12698050);
    }


    private void renderContainerTabs(MatrixStack matrixStack) {
        Rectangle containerBounds = getContainerBounds();
        ComponentDialog activeDialog = (ComponentDialog) this.activeTabTpl.getB();

        for (int i = 0; i < this.dialogs.size(); i++) {
            ComponentDialog thisDialog = this.dialogs.get(i);
            Point uv = thisDialog.getIconUV();
            boolean active = activeDialog.equals(thisDialog);
            Rectangle tabBounds = getTabBounds(i, active);
            blit(matrixStack, tabBounds.x, tabBounds.y, 63, active ? 28 : 0, tabBounds.width, tabBounds.height);


            blit(matrixStack, tabBounds.x + 6, containerBounds.y - 25 - 11, uv.x, uv.y, 16, 16);
        }


        (getMinecraft()).font.draw(matrixStack, ((SkillTab) this.activeTabTpl.getA()).getTabName(), containerBounds.x, (containerBounds.y - 12), -12632257);
    }


    private void renderContainerBorders(MatrixStack matrixStack) {
        assert this.minecraft != null;
        this.minecraft.getTextureManager().bind(UI_RESOURCE);

        Rectangle ctBox = getContainerBounds();

        RenderSystem.enableBlend();

        blit(matrixStack, ctBox.x - 9, ctBox.y - 18, 0, 0, 15, 24);

        blit(matrixStack, ctBox.x + ctBox.width - 7, ctBox.y - 18, 18, 0, 15, 24);

        blit(matrixStack, ctBox.x - 9, ctBox.y + ctBox.height - 7, 0, 27, 15, 16);

        blit(matrixStack, ctBox.x + ctBox.width - 7, ctBox.y + ctBox.height - 7, 18, 27, 15, 16);


        matrixStack.pushPose();
        matrixStack.translate((ctBox.x + 6), (ctBox.y - 18), 0.0D);
        matrixStack.scale((ctBox.width - 13), 1.0F, 1.0F);
        blit(matrixStack, 0, 0, 16, 0, 1, 24);

        matrixStack.translate(0.0D, (ctBox.height + 11), 0.0D);
        blit(matrixStack, 0, 0, 16, 27, 1, 16);

        matrixStack.popPose();

        matrixStack.pushPose();
        matrixStack.translate((ctBox.x - 9), (ctBox.y + 6), 0.0D);
        matrixStack.scale(1.0F, (ctBox.height - 13), 1.0F);
        blit(matrixStack, 0, 0, 0, 25, 15, 1);

        matrixStack.translate((ctBox.width + 2), 0.0D, 0.0D);
        blit(matrixStack, 0, 0, 18, 25, 15, 1);

        matrixStack.popPose();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\screen\SkillTreeScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */