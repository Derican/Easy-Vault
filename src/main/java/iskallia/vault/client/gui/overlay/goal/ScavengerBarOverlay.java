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

public class ScavengerBarOverlay
        extends BossBarOverlay {
    public ScavengerBarOverlay(VaultScavengerData data) {
        this.data = data;
    }

    private final VaultScavengerData data;

    public boolean shouldDisplay() {
        List<ScavengerHuntObjective.ItemSubmission> items = this.data.getRequiredItemSubmissions();
        return !items.isEmpty();
    }


    public int drawOverlay(MatrixStack renderStack, float pTicks) {
        List<ScavengerHuntObjective.ItemSubmission> items = this.data.getRequiredItemSubmissions();
        Minecraft mc = Minecraft.getInstance();

        int midX = mc.getWindow().getGuiScaledWidth() / 2;

        int gapWidth = 7;
        int itemBoxWidth = 32;

        int totalWidth = items.size() * itemBoxWidth + (items.size() - 1) * gapWidth;
        int shiftX = -totalWidth / 2 + itemBoxWidth / 2;

        mc.getTextureManager().bind(PlayerContainer.BLOCK_ATLAS);
        renderStack.pushPose();

        int yOffset = 0;
        renderStack.pushPose();
        renderStack.translate((midX + shiftX), (itemBoxWidth * 0.75F), 0.0D);
        for (ScavengerHuntObjective.ItemSubmission itemRequirement : items) {
            int reqYOffset = renderItemRequirement(renderStack, itemRequirement, itemBoxWidth);
            if (reqYOffset > yOffset) {
                yOffset = reqYOffset;
            }
            renderStack.translate((itemBoxWidth + gapWidth), 0.0D, 0.0D);
        }
        renderStack.popPose();
        return yOffset;
    }

    private static int renderItemRequirement(MatrixStack renderStack, ScavengerHuntObjective.ItemSubmission itemRequirement, int itemBoxWidth) {
        Minecraft mc = Minecraft.getInstance();
        FontRenderer fr = mc.font;
        ItemStack requiredStack = new ItemStack((IItemProvider) itemRequirement.getRequiredItem());
        ScavengerHuntConfig.SourceType source = ModConfigs.SCAVENGER_HUNT.getRequirementSource(requiredStack);

        renderStack.pushPose();
        renderStack.translate(0.0D, (-itemBoxWidth / 2.0F), 0.0D);
        renderItemStack(renderStack, requiredStack);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        mc.getTextureManager().bind(source.getIconPath());
        renderStack.pushPose();
        renderStack.translate(-16.0D, -2.4D, 0.0D);
        renderStack.scale(0.4F, 0.4F, 1.0F);
        ScreenDrawHelper.drawQuad(buf -> ScreenDrawHelper.rect((IVertexBuilder) buf, renderStack).dim(16.0F, 16.0F).draw());


        renderStack.popPose();
        RenderSystem.disableBlend();

        renderStack.translate(0.0D, 10.0D, 0.0D);
        String requiredText = itemRequirement.getCurrentAmount() + "/" + itemRequirement.getRequiredAmount();
        IFormattableTextComponent cmp = (new StringTextComponent(requiredText)).withStyle(TextFormatting.GREEN);

        UIHelper.renderCenteredWrappedText(renderStack, (ITextComponent) cmp, 30, 0);

        renderStack.translate(0.0D, 10.0D, 0.0D);

        renderStack.pushPose();
        renderStack.scale(0.5F, 0.5F, 1.0F);
        ITextComponent name = requiredStack.getHoverName();
        IFormattableTextComponent display = name.copy().withStyle(source.getRequirementColor());
        int lines = UIHelper.renderCenteredWrappedText(renderStack, (ITextComponent) display, 60, 0);
        renderStack.popPose();

        renderStack.popPose();
        return 25 + lines * 5;
    }

    private static void renderItemStack(MatrixStack renderStack, ItemStack item) {
        Minecraft mc = Minecraft.getInstance();
        ItemRenderer ir = mc.getItemRenderer();
        FontRenderer fr = item.getItem().getFontRenderer(item);
        if (fr == null) {
            fr = mc.font;
        }

        renderStack.translate(-8.0D, -8.0D, 0.0D);
        RenderSystem.pushMatrix();
        RenderSystem.multMatrix(renderStack.last().pose());
        ir.blitOffset = 200.0F;
        ir.renderAndDecorateItem(item, 0, 0);
        ir.renderGuiItemDecorations(fr, item, 0, 0, null);
        ir.blitOffset = 0.0F;
        RenderSystem.popMatrix();
        renderStack.translate(8.0D, 8.0D, 0.0D);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\overlay\goal\ScavengerBarOverlay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */