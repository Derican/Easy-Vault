package iskallia.vault.client.gui.overlay.goal;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import iskallia.vault.client.gui.helper.ScreenDrawHelper;
import iskallia.vault.client.gui.helper.UIHelper;
import iskallia.vault.client.vault.goal.VaultScavengerData;
import iskallia.vault.config.ScavengerHuntConfig;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.world.vault.logic.objective.ScavengerHuntObjective;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class ScavengerBarOverlay extends BossBarOverlay {
    private final VaultScavengerData data;

    public ScavengerBarOverlay(final VaultScavengerData data) {
        this.data = data;
    }

    @Override
    public boolean shouldDisplay() {
        final List<ScavengerHuntObjective.ItemSubmission> items = this.data.getRequiredItemSubmissions();
        return !items.isEmpty();
    }

    @Override
    public int drawOverlay(final MatrixStack renderStack, final float pTicks) {
        final List<ScavengerHuntObjective.ItemSubmission> items = this.data.getRequiredItemSubmissions();
        final Minecraft mc = Minecraft.getInstance();
        final int midX = mc.getWindow().getGuiScaledWidth() / 2;
        final int gapWidth = 7;
        final int itemBoxWidth = 32;
        final int totalWidth = items.size() * itemBoxWidth + (items.size() - 1) * gapWidth;
        final int shiftX = -totalWidth / 2 + itemBoxWidth / 2;
        mc.getTextureManager().bind(PlayerContainer.BLOCK_ATLAS);
        renderStack.pushPose();
        int yOffset = 0;
        renderStack.pushPose();
        renderStack.translate((double) (midX + shiftX), (double) (itemBoxWidth * 0.75f), 0.0);
        for (final ScavengerHuntObjective.ItemSubmission itemRequirement : items) {
            final int reqYOffset = renderItemRequirement(renderStack, itemRequirement, itemBoxWidth);
            if (reqYOffset > yOffset) {
                yOffset = reqYOffset;
            }
            renderStack.translate((double) (itemBoxWidth + gapWidth), 0.0, 0.0);
        }
        renderStack.popPose();
        return yOffset;
    }

    private static int renderItemRequirement(final MatrixStack renderStack, final ScavengerHuntObjective.ItemSubmission itemRequirement, final int itemBoxWidth) {
        final Minecraft mc = Minecraft.getInstance();
        final FontRenderer fr = mc.font;
        final ItemStack requiredStack = new ItemStack((IItemProvider) itemRequirement.getRequiredItem());
        final ScavengerHuntConfig.SourceType source = ModConfigs.SCAVENGER_HUNT.getRequirementSource(requiredStack);
        renderStack.pushPose();
        renderStack.translate(0.0, (double) (-itemBoxWidth / 2.0f), 0.0);
        renderItemStack(renderStack, requiredStack);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        mc.getTextureManager().bind(source.getIconPath());
        renderStack.pushPose();
        renderStack.translate(-16.0, -2.4, 0.0);
        renderStack.scale(0.4f, 0.4f, 1.0f);
        ScreenDrawHelper.drawQuad(buf -> ScreenDrawHelper.rect((IVertexBuilder) buf, renderStack).dim(16.0f, 16.0f).draw());
        renderStack.popPose();
        RenderSystem.disableBlend();
        renderStack.translate(0.0, 10.0, 0.0);
        final String requiredText = itemRequirement.getCurrentAmount() + "/" + itemRequirement.getRequiredAmount();
        final IFormattableTextComponent cmp = new StringTextComponent(requiredText).withStyle(TextFormatting.GREEN);
        UIHelper.renderCenteredWrappedText(renderStack, (ITextComponent) cmp, 30, 0);
        renderStack.translate(0.0, 10.0, 0.0);
        renderStack.pushPose();
        renderStack.scale(0.5f, 0.5f, 1.0f);
        final ITextComponent name = requiredStack.getHoverName();
        final IFormattableTextComponent display = name.copy().withStyle(source.getRequirementColor());
        final int lines = UIHelper.renderCenteredWrappedText(renderStack, (ITextComponent) display, 60, 0);
        renderStack.popPose();
        renderStack.popPose();
        return 25 + lines * 5;
    }

    private static void renderItemStack(final MatrixStack renderStack, final ItemStack item) {
        final Minecraft mc = Minecraft.getInstance();
        final ItemRenderer ir = mc.getItemRenderer();
        FontRenderer fr = item.getItem().getFontRenderer(item);
        if (fr == null) {
            fr = mc.font;
        }
        renderStack.translate(-8.0, -8.0, 0.0);
        RenderSystem.pushMatrix();
        RenderSystem.multMatrix(renderStack.last().pose());
        ir.blitOffset = 200.0f;
        ir.renderAndDecorateItem(item, 0, 0);
        ir.renderGuiItemDecorations(fr, item, 0, 0, (String) null);
        ir.blitOffset = 0.0f;
        RenderSystem.popMatrix();
        renderStack.translate(8.0, 8.0, 0.0);
    }
}
