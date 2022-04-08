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

public class ResearchGroupWidget extends Widget {
    private static final int CONTENT_SPACER = 5;
    private static final int CONTAINER_CORNER_WH = 3;
    private static final int CONTAINER_HEADER_WIDTH = 3;
    private static final int CONTAINER_HEADER_HEIGHT = 20;
    private static final int CONTAINER_HEADER_ICON_FRAME_WH = 30;
    private static final int CONTAINER_HEADER_ICON_WH = 16;
    private final ResearchGroupStyle groupStyle;
    private final ResearchTree researchTree;
    private final Supplier<ResearchWidget> selectedResearchSupplier;

    public ResearchGroupWidget(ResearchGroupStyle style, ResearchTree researchTree, Supplier<ResearchWidget> selectedResearchSupplier) {
        super(style.getX(), style.getY(),
                getGroupWidth(style), getGroupHeight(style), (ITextComponent) new StringTextComponent("the_vault.widgets.research_group"));

        this.groupStyle = style;
        this.researchTree = researchTree;
        this.selectedResearchSupplier = selectedResearchSupplier;
    }

    private static int getGroupWidth(ResearchGroupStyle style) {
        FontRenderer fr = (Minecraft.getInstance()).font;
        ResearchGroup group = ModConfigs.RESEARCH_GROUPS.getResearchGroupById(style.getGroup());
        int minWidth = 5;
        int iconWidth = (style.getIcon() == null) ? 0 : 35;
        int titleWidth = (group == null) ? 0 : (fr.width(group.getTitle()) + 5);
        int costWidth = fr.width("XXXXXXXX") + 15 + 5;

        return Math.max(minWidth + iconWidth + titleWidth + costWidth, style.getBoxWidth());
    }

    private static int getGroupHeight(ResearchGroupStyle style) {
        int minHeight = 24;
        return Math.max(minHeight, style.getBoxHeight());
    }


    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft.getInstance().getTextureManager().bind(SkillTreeScreen.UI_RESOURCE);

        matrixStack.pushPose();
        matrixStack.translate(this.groupStyle.getX(), this.groupStyle.getY(), 0.0D);

        renderContainerBox(matrixStack, mouseX, mouseY, partialTicks);
        renderHeaderBox(matrixStack, mouseX, mouseY, partialTicks);
        renderHeaderIcon(matrixStack, mouseX, mouseY, partialTicks);
        renderHeaderInformation(matrixStack, mouseX, mouseY, partialTicks);

        matrixStack.popPose();
    }

    private void renderHeaderInformation(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        int i = 0;
        FontRenderer fr = (Minecraft.getInstance()).font;

        int titleOffset = 5;
        if (this.groupStyle.getIcon() != null) {
            titleOffset += 35;
        }
        int costRightOffset = this.width - 5;

        ResearchGroup group = ModConfigs.RESEARCH_GROUPS.getResearchGroupById(this.groupStyle.getGroup());
        if (group != null) {
            String title = group.getTitle();
            title = (title == null) ? "" : title;
            IReorderingProcessor bidiTitle = LanguageMap.getInstance().getVisualOrder((ITextProperties) new StringTextComponent(title));

            IRenderTypeBuffer.Impl impl = IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuilder());
            fr.drawInBatch(bidiTitle, titleOffset, 6.0F, this.groupStyle.getHeaderTextColor(), true, matrixStack
                            .last().pose(), (IRenderTypeBuffer) impl, false, 0,
                    LightmapHelper.getPackedFullbrightCoords());
            impl.endBatch();

            RenderSystem.enableDepthTest();
            RenderSystem.enableBlend();
        }

        float currentAdditional = 0.0F;
        for (String research : this.researchTree.getResearchesDone()) {
            ResearchGroup resGroup = ModConfigs.RESEARCH_GROUPS.getResearchGroup(research);
            if (resGroup != null) {
                currentAdditional += resGroup.getGroupIncreasedResearchCost(this.groupStyle.getGroup());
            }
        }
        int currentAdditionalDisplay = Math.round(currentAdditional);

        boolean displayAdditional = false;
        float selectedAdditional = 0.0F;
        ResearchWidget selectedWidget = this.selectedResearchSupplier.get();
        if (selectedWidget != null) {
            String selectedResearch = selectedWidget.getResearchName();
            if (selectedResearch != null && !this.researchTree.isResearched(selectedResearch)) {
                displayAdditional = (currentAdditionalDisplay != 0);

                ResearchGroup selectedGroup = ModConfigs.RESEARCH_GROUPS.getResearchGroup(selectedResearch);
                if (selectedGroup != null) {
                    selectedAdditional += selectedGroup.getGroupIncreasedResearchCost(this.groupStyle.getGroup());
                    i = (displayAdditional ? 1 : 0) | ((selectedAdditional != 0.0F) ? 1 : 0);
                }
            }
        }
        int selectedAdditionalDisplay = Math.round(currentAdditional + selectedAdditional) - currentAdditionalDisplay;

        if (currentAdditionalDisplay == 0 && i == 0) {
            return;
        }
        String displayStr = (currentAdditionalDisplay >= 0) ? ("+" + currentAdditionalDisplay) : String.valueOf(currentAdditionalDisplay);
        StringTextComponent costDisplay = new StringTextComponent(displayStr);
        if (i != 0) {
            String selectedStr = (selectedAdditionalDisplay >= 0) ? ("+" + selectedAdditionalDisplay) : String.valueOf(selectedAdditionalDisplay);
            costDisplay.append(String.format(" (%s)", new Object[]{selectedStr}));
        }

        IReorderingProcessor bidiCost = LanguageMap.getInstance().getVisualOrder((ITextProperties) costDisplay);
        costRightOffset -= fr.width(bidiCost);

        IRenderTypeBuffer.Impl renderBuf = IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuilder());
        fr.drawInBatch(bidiCost, costRightOffset, 6.0F, this.groupStyle.getHeaderTextColor(), true, matrixStack
                        .last().pose(), (IRenderTypeBuffer) renderBuf, false, 0,
                LightmapHelper.getPackedFullbrightCoords());
        renderBuf.endBatch();

        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
    }

    private void renderHeaderIcon(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (this.groupStyle.getIcon() == null) {
            return;
        }
        ResourceBoundary iconFrame = SkillFrame.RECTANGULAR.getResourceBoundary();
        Minecraft.getInstance().getTextureManager().bind(iconFrame.getResource());
        float iconFrameX = 5.0F;
        float iconFrameY = -5.0F;

        ScreenDrawHelper.draw(7, DefaultVertexFormats.POSITION_COLOR_TEX, (BufferBuilder buf) -> ScreenDrawHelper.rect((IVertexBuilder) buf, matrixStack, iconFrameX, iconFrameY, 0.0F, 30.0F, 30.0F).texVanilla(iconFrame.getU(), iconFrame.getV(), iconFrame.getW(), iconFrame.getH()).draw());


        ResearchGroupStyle.Icon icon = this.groupStyle.getIcon();
        Minecraft.getInstance().getTextureManager().bind(ResearchWidget.RESEARCHES_RESOURCE);
        float iconX = 12.0F;
        float iconY = 2.0F;

        ScreenDrawHelper.draw(7, DefaultVertexFormats.POSITION_COLOR_TEX, (BufferBuilder buf) -> ScreenDrawHelper.rect((IVertexBuilder) buf, matrixStack, iconX, iconY, 0.0F, 16.0F, 16.0F).texVanilla(icon.getU(), icon.getV(), 16.0F, 16.0F).draw());
    }


    private void renderHeaderBox(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        int headerColor = this.groupStyle.getHeaderColor();
        ScreenDrawHelper.draw(7, DefaultVertexFormats.POSITION_COLOR_TEX, buf -> {
            ScreenDrawHelper.rect((IVertexBuilder) buf, matrixStack, 3.0F, 20.0F).texVanilla(166.0F, 0.0F, 3.0F, 20.0F).color(headerColor).draw();
            ScreenDrawHelper.rect((IVertexBuilder) buf, matrixStack, (this.width - 6), 20.0F).at(3.0F, 0.0F).texVanilla(169.0F, 0.0F, 1.0F, 20.0F).color(headerColor).draw();
            ScreenDrawHelper.rect((IVertexBuilder) buf, matrixStack, 3.0F, 20.0F).at((this.width - 3), 0.0F).texVanilla(170.0F, 0.0F, 3.0F, 20.0F).color(headerColor).draw();
        });
    }


    private void renderContainerBox(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        ScreenDrawHelper.draw(7, DefaultVertexFormats.POSITION_COLOR_TEX, buf -> {
            ScreenDrawHelper.rect((IVertexBuilder) buf, matrixStack, 3.0F, 3.0F).texVanilla(166.0F, 20.0F, 3.0F, 3.0F).draw();
            ScreenDrawHelper.rect((IVertexBuilder) buf, matrixStack, (this.width - 6), 3.0F).at(3.0F, 0.0F).texVanilla(169.0F, 20.0F, 1.0F, 3.0F).draw();
            ScreenDrawHelper.rect((IVertexBuilder) buf, matrixStack, 3.0F, 3.0F).at((this.width - 3), 0.0F).texVanilla(170.0F, 20.0F, 3.0F, 3.0F).draw();
            ScreenDrawHelper.rect((IVertexBuilder) buf, matrixStack, 3.0F, (this.height - 6)).at(0.0F, 3.0F).texVanilla(166.0F, 23.0F, 3.0F, 1.0F).draw();
            ScreenDrawHelper.rect((IVertexBuilder) buf, matrixStack, 3.0F, (this.height - 6)).at((this.width - 3), 3.0F).texVanilla(170.0F, 23.0F, 3.0F, 1.0F).draw();
            ScreenDrawHelper.rect((IVertexBuilder) buf, matrixStack, 3.0F, 3.0F).at(0.0F, (this.height - 3)).texVanilla(166.0F, 24.0F, 3.0F, 3.0F).draw();
            ScreenDrawHelper.rect((IVertexBuilder) buf, matrixStack, (this.width - 6), 3.0F).at(3.0F, (this.height - 3)).texVanilla(169.0F, 24.0F, 1.0F, 3.0F).draw();
            ScreenDrawHelper.rect((IVertexBuilder) buf, matrixStack, 3.0F, 3.0F).at((this.width - 3), (this.height - 3)).texVanilla(170.0F, 24.0F, 3.0F, 3.0F).draw();
            ScreenDrawHelper.rect((IVertexBuilder) buf, matrixStack, (this.width - 6), (this.height - 6)).at(3.0F, 3.0F).texVanilla(169.0F, 23.0F, 1.0F, 1.0F).draw();
        });
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\widget\ResearchGroupWidget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */