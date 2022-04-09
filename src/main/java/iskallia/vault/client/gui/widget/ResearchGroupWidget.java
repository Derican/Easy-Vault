// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import iskallia.vault.client.gui.helper.LightmapHelper;
import iskallia.vault.client.gui.helper.ScreenDrawHelper;
import iskallia.vault.client.gui.helper.SkillFrame;
import iskallia.vault.client.gui.screen.SkillTreeScreen;
import iskallia.vault.config.entry.ResearchGroupStyle;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.research.ResearchTree;
import iskallia.vault.research.group.ResearchGroup;
import iskallia.vault.util.ResourceBoundary;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.LanguageMap;
import net.minecraft.util.text.StringTextComponent;

import java.util.function.Supplier;

public class ResearchGroupWidget extends Widget
{
    private static final int CONTENT_SPACER = 5;
    private static final int CONTAINER_CORNER_WH = 3;
    private static final int CONTAINER_HEADER_WIDTH = 3;
    private static final int CONTAINER_HEADER_HEIGHT = 20;
    private static final int CONTAINER_HEADER_ICON_FRAME_WH = 30;
    private static final int CONTAINER_HEADER_ICON_WH = 16;
    private final ResearchGroupStyle groupStyle;
    private final ResearchTree researchTree;
    private final Supplier<ResearchWidget> selectedResearchSupplier;
    
    public ResearchGroupWidget(final ResearchGroupStyle style, final ResearchTree researchTree, final Supplier<ResearchWidget> selectedResearchSupplier) {
        super(style.getX(), style.getY(), getGroupWidth(style), getGroupHeight(style), (ITextComponent)new StringTextComponent("the_vault.widgets.research_group"));
        this.groupStyle = style;
        this.researchTree = researchTree;
        this.selectedResearchSupplier = selectedResearchSupplier;
    }
    
    private static int getGroupWidth(final ResearchGroupStyle style) {
        final FontRenderer fr = Minecraft.getInstance().font;
        final ResearchGroup group = ModConfigs.RESEARCH_GROUPS.getResearchGroupById(style.getGroup());
        final int minWidth = 5;
        final int iconWidth = (style.getIcon() == null) ? 0 : 35;
        final int titleWidth = (group == null) ? 0 : (fr.width(group.getTitle()) + 5);
        final int costWidth = fr.width("XXXXXXXX") + 15 + 5;
        return Math.max(minWidth + iconWidth + titleWidth + costWidth, style.getBoxWidth());
    }
    
    private static int getGroupHeight(final ResearchGroupStyle style) {
        final int minHeight = 24;
        return Math.max(minHeight, style.getBoxHeight());
    }
    
    public void render(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        Minecraft.getInstance().getTextureManager().bind(SkillTreeScreen.UI_RESOURCE);
        matrixStack.pushPose();
        matrixStack.translate((double)this.groupStyle.getX(), (double)this.groupStyle.getY(), 0.0);
        this.renderContainerBox(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHeaderBox(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHeaderIcon(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHeaderInformation(matrixStack, mouseX, mouseY, partialTicks);
        matrixStack.popPose();
    }
    
    private void renderHeaderInformation(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        final FontRenderer fr = Minecraft.getInstance().font;
        int titleOffset = 5;
        if (this.groupStyle.getIcon() != null) {
            titleOffset += 35;
        }
        int costRightOffset = this.width - 5;
        final ResearchGroup group = ModConfigs.RESEARCH_GROUPS.getResearchGroupById(this.groupStyle.getGroup());
        if (group != null) {
            String title = group.getTitle();
            title = ((title == null) ? "" : title);
            final IReorderingProcessor bidiTitle = LanguageMap.getInstance().getVisualOrder((ITextProperties)new StringTextComponent(title));
            final IRenderTypeBuffer.Impl renderBuf = IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuilder());
            fr.drawInBatch(bidiTitle, (float)titleOffset, 6.0f, this.groupStyle.getHeaderTextColor(), true, matrixStack.last().pose(), (IRenderTypeBuffer)renderBuf, false, 0, LightmapHelper.getPackedFullbrightCoords());
            renderBuf.endBatch();
            RenderSystem.enableDepthTest();
            RenderSystem.enableBlend();
        }
        float currentAdditional = 0.0f;
        for (final String research : this.researchTree.getResearchesDone()) {
            final ResearchGroup resGroup = ModConfigs.RESEARCH_GROUPS.getResearchGroup(research);
            if (resGroup != null) {
                currentAdditional += resGroup.getGroupIncreasedResearchCost(this.groupStyle.getGroup());
            }
        }
        final int currentAdditionalDisplay = Math.round(currentAdditional);
        boolean displayAdditional = false;
        float selectedAdditional = 0.0f;
        final ResearchWidget selectedWidget = this.selectedResearchSupplier.get();
        if (selectedWidget != null) {
            final String selectedResearch = selectedWidget.getResearchName();
            if (selectedResearch != null && !this.researchTree.isResearched(selectedResearch)) {
                displayAdditional = (currentAdditionalDisplay != 0);
                final ResearchGroup selectedGroup = ModConfigs.RESEARCH_GROUPS.getResearchGroup(selectedResearch);
                if (selectedGroup != null) {
                    selectedAdditional += selectedGroup.getGroupIncreasedResearchCost(this.groupStyle.getGroup());
                    displayAdditional |= (selectedAdditional != 0.0f);
                }
            }
        }
        final int selectedAdditionalDisplay = Math.round(currentAdditional + selectedAdditional) - currentAdditionalDisplay;
        if (currentAdditionalDisplay == 0 && !displayAdditional) {
            return;
        }
        final String displayStr = (currentAdditionalDisplay >= 0) ? ("+" + currentAdditionalDisplay) : String.valueOf(currentAdditionalDisplay);
        final StringTextComponent costDisplay = new StringTextComponent(displayStr);
        if (displayAdditional) {
            final String selectedStr = (selectedAdditionalDisplay >= 0) ? ("+" + selectedAdditionalDisplay) : String.valueOf(selectedAdditionalDisplay);
            costDisplay.append(String.format(" (%s)", selectedStr));
        }
        final IReorderingProcessor bidiCost = LanguageMap.getInstance().getVisualOrder((ITextProperties)costDisplay);
        costRightOffset -= fr.width(bidiCost);
        final IRenderTypeBuffer.Impl renderBuf2 = IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuilder());
        fr.drawInBatch(bidiCost, (float)costRightOffset, 6.0f, this.groupStyle.getHeaderTextColor(), true, matrixStack.last().pose(), (IRenderTypeBuffer)renderBuf2, false, 0, LightmapHelper.getPackedFullbrightCoords());
        renderBuf2.endBatch();
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
    }
    
    private void renderHeaderIcon(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        if (this.groupStyle.getIcon() == null) {
            return;
        }
        final ResourceBoundary iconFrame = SkillFrame.RECTANGULAR.getResourceBoundary();
        Minecraft.getInstance().getTextureManager().bind(iconFrame.getResource());
        final float iconFrameX = 5.0f;
        final float iconFrameY = -5.0f;
        ScreenDrawHelper.draw(7, DefaultVertexFormats.POSITION_COLOR_TEX, (BufferBuilder buf) -> ScreenDrawHelper.rect((IVertexBuilder)buf, matrixStack, iconFrameX, iconFrameY, 0.0f, 30.0f, 30.0f).texVanilla((float)iconFrame.getU(), (float)iconFrame.getV(), (float)iconFrame.getW(), (float)iconFrame.getH()).draw());
        final ResearchGroupStyle.Icon icon = this.groupStyle.getIcon();
        Minecraft.getInstance().getTextureManager().bind(ResearchWidget.RESEARCHES_RESOURCE);
        final float iconX = 12.0f;
        final float iconY = 2.0f;
        ScreenDrawHelper.draw(7, DefaultVertexFormats.POSITION_COLOR_TEX, (BufferBuilder buf) -> ScreenDrawHelper.rect((IVertexBuilder)buf, matrixStack, iconX, iconY, 0.0f, 16.0f, 16.0f).texVanilla((float)icon.getU(), (float)icon.getV(), 16.0f, 16.0f).draw());
    }
    
    private void renderHeaderBox(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        final int headerColor = this.groupStyle.getHeaderColor();
        ScreenDrawHelper.draw(7, DefaultVertexFormats.POSITION_COLOR_TEX, buf -> {
            ScreenDrawHelper.rect((IVertexBuilder)buf, matrixStack, 3.0f, 20.0f).texVanilla(166.0f, 0.0f, 3.0f, 20.0f).color(headerColor).draw();
            ScreenDrawHelper.rect((IVertexBuilder)buf, matrixStack, (float)(this.width - 6), 20.0f).at(3.0f, 0.0f).texVanilla(169.0f, 0.0f, 1.0f, 20.0f).color(headerColor).draw();
            ScreenDrawHelper.rect((IVertexBuilder)buf, matrixStack, 3.0f, 20.0f).at((float)(this.width - 3), 0.0f).texVanilla(170.0f, 0.0f, 3.0f, 20.0f).color(headerColor).draw();
        });
    }
    
    private void renderContainerBox(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        ScreenDrawHelper.draw(7, DefaultVertexFormats.POSITION_COLOR_TEX, buf -> {
            ScreenDrawHelper.rect((IVertexBuilder)buf, matrixStack, 3.0f, 3.0f).texVanilla(166.0f, 20.0f, 3.0f, 3.0f).draw();
            ScreenDrawHelper.rect((IVertexBuilder)buf, matrixStack, (float)(this.width - 6), 3.0f).at(3.0f, 0.0f).texVanilla(169.0f, 20.0f, 1.0f, 3.0f).draw();
            ScreenDrawHelper.rect((IVertexBuilder)buf, matrixStack, 3.0f, 3.0f).at((float)(this.width - 3), 0.0f).texVanilla(170.0f, 20.0f, 3.0f, 3.0f).draw();
            ScreenDrawHelper.rect((IVertexBuilder)buf, matrixStack, 3.0f, (float)(this.height - 6)).at(0.0f, 3.0f).texVanilla(166.0f, 23.0f, 3.0f, 1.0f).draw();
            ScreenDrawHelper.rect((IVertexBuilder)buf, matrixStack, 3.0f, (float)(this.height - 6)).at((float)(this.width - 3), 3.0f).texVanilla(170.0f, 23.0f, 3.0f, 1.0f).draw();
            ScreenDrawHelper.rect((IVertexBuilder)buf, matrixStack, 3.0f, 3.0f).at(0.0f, (float)(this.height - 3)).texVanilla(166.0f, 24.0f, 3.0f, 3.0f).draw();
            ScreenDrawHelper.rect((IVertexBuilder)buf, matrixStack, (float)(this.width - 6), 3.0f).at(3.0f, (float)(this.height - 3)).texVanilla(169.0f, 24.0f, 1.0f, 3.0f).draw();
            ScreenDrawHelper.rect((IVertexBuilder)buf, matrixStack, 3.0f, 3.0f).at((float)(this.width - 3), (float)(this.height - 3)).texVanilla(170.0f, 24.0f, 3.0f, 3.0f).draw();
            ScreenDrawHelper.rect((IVertexBuilder)buf, matrixStack, (float)(this.width - 6), (float)(this.height - 6)).at(3.0f, 3.0f).texVanilla(169.0f, 23.0f, 1.0f, 1.0f).draw();
        });
    }
}
