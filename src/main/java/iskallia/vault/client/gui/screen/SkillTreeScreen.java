package iskallia.vault.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import iskallia.vault.client.gui.component.AbilityDialog;
import iskallia.vault.client.gui.component.ComponentDialog;
import iskallia.vault.client.gui.component.PlayerStatisticsDialog;
import iskallia.vault.client.gui.component.TalentDialog;
import iskallia.vault.client.gui.helper.FontHelper;
import iskallia.vault.client.gui.overlay.VaultBarOverlay;
import iskallia.vault.client.gui.tab.SkillTab;
import iskallia.vault.container.SkillTreeContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class SkillTreeScreen extends ContainerScreen<SkillTreeContainer> {
    public static final ResourceLocation HUD_RESOURCE;
    public static final ResourceLocation UI_RESOURCE;
    public static final ResourceLocation BACKGROUNDS_RESOURCE;
    public static final int TAB_WIDTH = 28;
    public static final int GAP = 3;
    private final List<ComponentDialog> dialogs;
    protected Tuple<SkillTab, ComponentDialog> activeTabTpl;

    public SkillTreeScreen(final SkillTreeContainer container, final PlayerInventory inventory, final ITextComponent title) {
        super(container, inventory, (ITextComponent) new StringTextComponent("Ability Tree Screen"));
        this.dialogs = new ArrayList<ComponentDialog>();
        final PlayerStatisticsDialog statisticsDialog = new PlayerStatisticsDialog(this);
        this.dialogs.add(statisticsDialog);
        this.dialogs.add(new AbilityDialog(((SkillTreeContainer) this.getMenu()).getAbilityTree(), this));
        this.dialogs.add(new TalentDialog(((SkillTreeContainer) this.getMenu()).getTalentTree(), this));
        this.selectDialog(statisticsDialog);
        this.imageWidth = 270;
        this.imageHeight = 200;
    }

    private void selectDialog(final ComponentDialog dialog) {
        this.activeTabTpl = (Tuple<SkillTab, ComponentDialog>) new Tuple(dialog.createTab(), dialog);
        this.refreshWidgets();
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
        return new Rectangle(30, 60, (int) (this.width * 0.55f) - 30, this.height - 30 - 60);
    }

    public Rectangle getTabBounds(final int index, final boolean active) {
        final Rectangle containerBounds = this.getContainerBounds();
        return new Rectangle(containerBounds.x + 5 + index * 31, containerBounds.y - 25 - (active ? 21 : 17), 28, active ? 32 : 25);
    }

    public boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
        final Rectangle containerBounds = this.getContainerBounds();
        if (containerBounds.contains(mouseX, mouseY)) {
            ((SkillTab) this.activeTabTpl.getA()).mouseClicked(mouseX, mouseY, button);
        } else {
            boolean updatedTab = false;
            final ComponentDialog activeDialog = (ComponentDialog) this.activeTabTpl.getB();
            for (int i = 0; i < this.dialogs.size(); ++i) {
                final ComponentDialog thisDialog = this.dialogs.get(i);
                final Rectangle tabBounds = this.getTabBounds(i, activeDialog.equals(thisDialog));
                if (tabBounds.contains(mouseX, mouseY)) {
                    final SkillTab activeTab = (SkillTab) this.activeTabTpl.getA();
                    activeTab.removed();
                    this.selectDialog(thisDialog);
                    updatedTab = true;
                }
            }
            if (!updatedTab) {
                activeDialog.mouseClicked(mouseX, mouseY, button);
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public boolean mouseReleased(final double mouseX, final double mouseY, final int button) {
        ((SkillTab) this.activeTabTpl.getA()).mouseReleased(mouseX, mouseY, button);
        return super.mouseReleased(mouseX, mouseY, button);
    }

    public void mouseMoved(final double mouseX, final double mouseY) {
        ((SkillTab) this.activeTabTpl.getA()).mouseMoved(mouseX, mouseY);
        ((ComponentDialog) this.activeTabTpl.getB()).mouseMoved((int) mouseX, (int) mouseY);
    }

    public boolean mouseScrolled(final double mouseX, final double mouseY, final double delta) {
        if (this.getContainerBounds().contains((int) mouseX, (int) mouseY)) {
            ((SkillTab) this.activeTabTpl.getA()).mouseScrolled(mouseX, mouseY, delta);
        } else {
            ((ComponentDialog) this.activeTabTpl.getB()).mouseScrolled(mouseX, mouseY, delta);
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    public void removed() {
        ((SkillTab) this.activeTabTpl.getA()).removed();
    }

    protected void renderBg(final MatrixStack matrixStack, final float partialTicks, final int x, final int y) {
        this.renderBackground(matrixStack);
    }

    protected void renderLabels(final MatrixStack matrixStack, final int x, final int y) {
    }

    public void render(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        final Rectangle containerBounds = this.getContainerBounds();
        final List<Runnable> postRender = ((SkillTab) this.activeTabTpl.getA()).renderTab(containerBounds, matrixStack, mouseX, mouseY, partialTicks);
        this.renderSkillPointOverlay(matrixStack);
        this.renderKnowledgePointOverlay(matrixStack);
        this.renderContainerBorders(matrixStack);
        this.renderContainerTabs(matrixStack);
        this.renderVaultLevelBar(matrixStack);
        final int x = containerBounds.x + containerBounds.width + 15;
        final int y = containerBounds.y - 18;
        final Rectangle dialogBounds = new Rectangle(x, y, this.width - 21 - x, this.height - 21 - y);
        ((ComponentDialog) this.activeTabTpl.getB()).setBounds(dialogBounds);
        ((ComponentDialog) this.activeTabTpl.getB()).render(matrixStack, mouseX, mouseY, partialTicks);
        postRender.forEach(Runnable::run);
    }

    private void renderSkillPointOverlay(final MatrixStack matrixStack) {
        if (VaultBarOverlay.unspentSkillPoints > 0) {
            final Minecraft mc = Minecraft.getInstance();
            final IReorderingProcessor bidiTxt = new StringTextComponent("").append((ITextComponent) new StringTextComponent(String.valueOf(VaultBarOverlay.unspentSkillPoints)).withStyle(TextFormatting.YELLOW)).append(" unspent skill point" + ((VaultBarOverlay.unspentSkillPoints == 1) ? "" : "s")).getVisualOrderText();
            final int unspentWidth = mc.font.width(bidiTxt) + 5;
            mc.font.drawShadow(matrixStack, bidiTxt, (float) (mc.getWindow().getGuiScaledWidth() - unspentWidth), 18.0f, -1);
        }
    }

    private void renderKnowledgePointOverlay(final MatrixStack matrixStack) {
        if (VaultBarOverlay.unspentKnowledgePoints > 0) {
            final Minecraft mc = Minecraft.getInstance();
            final IReorderingProcessor bidiTxt = new StringTextComponent("").append((ITextComponent) new StringTextComponent(String.valueOf(VaultBarOverlay.unspentKnowledgePoints)).withStyle(TextFormatting.AQUA)).append(" unspent knowledge point" + ((VaultBarOverlay.unspentKnowledgePoints == 1) ? "" : "s")).getVisualOrderText();
            final int unspentWidth = mc.font.width(bidiTxt) + 5;
            matrixStack.pushPose();
            if (VaultBarOverlay.unspentSkillPoints > 0) {
                matrixStack.translate(0.0, 12.0, 0.0);
            }
            mc.font.drawShadow(matrixStack, bidiTxt, (float) (mc.getWindow().getGuiScaledWidth() - unspentWidth), 18.0f, -1);
            matrixStack.popPose();
        }
    }

    private void renderVaultLevelBar(final MatrixStack matrixStack) {
        final Rectangle containerBounds = this.getContainerBounds();
        final Minecraft minecraft = this.getMinecraft();
        minecraft.textureManager.bind(VaultBarOverlay.VAULT_HUD_SPRITE);
        final String text = String.valueOf(VaultBarOverlay.vaultLevel);
        final int textWidth = minecraft.font.width(text);
        final int barWidth = 85;
        final float expPercentage = VaultBarOverlay.vaultExp / (float) VaultBarOverlay.tnl;
        final int barX = containerBounds.x + containerBounds.width - barWidth - 5;
        final int barY = containerBounds.y - 10;
        minecraft.gui.blit(matrixStack, barX, barY, 1, 1, barWidth, 5);
        minecraft.gui.blit(matrixStack, barX, barY, 1, 7, (int) (barWidth * expPercentage), 5);
        FontHelper.drawStringWithBorder(matrixStack, text, (float) (barX - textWidth - 1), (float) (barY - 1), -6601, -12698050);
    }

    private void renderContainerTabs(final MatrixStack matrixStack) {
        final Rectangle containerBounds = this.getContainerBounds();
        final ComponentDialog activeDialog = (ComponentDialog) this.activeTabTpl.getB();
        for (int i = 0; i < this.dialogs.size(); ++i) {
            final ComponentDialog thisDialog = this.dialogs.get(i);
            final Point uv = thisDialog.getIconUV();
            final boolean active = activeDialog.equals(thisDialog);
            final Rectangle tabBounds = this.getTabBounds(i, active);
            this.blit(matrixStack, tabBounds.x, tabBounds.y, 63, active ? 28 : 0, tabBounds.width, tabBounds.height);
            this.blit(matrixStack, tabBounds.x + 6, containerBounds.y - 25 - 11, uv.x, uv.y, 16, 16);
        }
        this.getMinecraft().font.draw(matrixStack, ((SkillTab) this.activeTabTpl.getA()).getTabName(), (float) containerBounds.x, (float) (containerBounds.y - 12), -12632257);
    }

    private void renderContainerBorders(final MatrixStack matrixStack) {
        assert this.minecraft != null;
        this.minecraft.getTextureManager().bind(SkillTreeScreen.UI_RESOURCE);
        final Rectangle ctBox = this.getContainerBounds();
        RenderSystem.enableBlend();
        this.blit(matrixStack, ctBox.x - 9, ctBox.y - 18, 0, 0, 15, 24);
        this.blit(matrixStack, ctBox.x + ctBox.width - 7, ctBox.y - 18, 18, 0, 15, 24);
        this.blit(matrixStack, ctBox.x - 9, ctBox.y + ctBox.height - 7, 0, 27, 15, 16);
        this.blit(matrixStack, ctBox.x + ctBox.width - 7, ctBox.y + ctBox.height - 7, 18, 27, 15, 16);
        matrixStack.pushPose();
        matrixStack.translate((double) (ctBox.x + 6), (double) (ctBox.y - 18), 0.0);
        matrixStack.scale((float) (ctBox.width - 13), 1.0f, 1.0f);
        this.blit(matrixStack, 0, 0, 16, 0, 1, 24);
        matrixStack.translate(0.0, (double) (ctBox.height + 11), 0.0);
        this.blit(matrixStack, 0, 0, 16, 27, 1, 16);
        matrixStack.popPose();
        matrixStack.pushPose();
        matrixStack.translate((double) (ctBox.x - 9), (double) (ctBox.y + 6), 0.0);
        matrixStack.scale(1.0f, (float) (ctBox.height - 13), 1.0f);
        this.blit(matrixStack, 0, 0, 0, 25, 15, 1);
        matrixStack.translate((double) (ctBox.width + 2), 0.0, 0.0);
        this.blit(matrixStack, 0, 0, 18, 25, 15, 1);
        matrixStack.popPose();
    }

    static {
        HUD_RESOURCE = new ResourceLocation("the_vault", "textures/gui/vault-hud.png");
        UI_RESOURCE = new ResourceLocation("the_vault", "textures/gui/ability-tree.png");
        BACKGROUNDS_RESOURCE = new ResourceLocation("the_vault", "textures/gui/ability-tree-bgs.png");
    }
}
