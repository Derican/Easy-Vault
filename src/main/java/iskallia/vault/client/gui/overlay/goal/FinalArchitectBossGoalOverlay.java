// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.client.gui.overlay.goal;

import java.util.Iterator;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.math.MathHelper;
import iskallia.vault.world.vault.logic.objective.architect.modifier.VoteModifier;
import iskallia.vault.world.vault.logic.objective.architect.DirectionChoice;
import java.util.List;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.client.gui.FontRenderer;
import com.mojang.blaze3d.systems.RenderSystem;
import iskallia.vault.client.gui.helper.LightmapHelper;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import iskallia.vault.world.vault.logic.objective.architect.VotingSession;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.client.vault.goal.FinalArchitectGoalData;

public class FinalArchitectBossGoalOverlay extends BossBarOverlay
{
    private final FinalArchitectGoalData data;
    
    public FinalArchitectBossGoalOverlay(final FinalArchitectGoalData data) {
        this.data = data;
    }
    
    @Override
    public boolean shouldDisplay() {
        return true;
    }
    
    @Override
    public int drawOverlay(final MatrixStack renderStack, final float pTicks) {
        final VotingSession activeSession = this.data.getActiveSession();
        final Minecraft mc = Minecraft.getInstance();
        int offsetY = 5;
        if (activeSession != null && !activeSession.getDirections().isEmpty()) {
            offsetY = this.drawVotingSession(activeSession, renderStack, offsetY);
        }
        mc.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
        return offsetY;
    }
    
    private int drawVotingSession(final VotingSession activeSession, final MatrixStack renderStack, int offsetY) {
        final IRenderTypeBuffer.Impl buffer = IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuilder());
        final Minecraft mc = Minecraft.getInstance();
        final FontRenderer fr = mc.font;
        final int midX = mc.getWindow().getGuiScaledWidth() / 2;
        final int segmentWidth = 8;
        final int barSegments = 22;
        final int startEndWith = 12;
        final int barWidth = segmentWidth * barSegments;
        final int totalWidth = barWidth + startEndWith * 2;
        final int offsetX = midX - totalWidth / 2;
        final ITextComponent title = (ITextComponent)new StringTextComponent("Vote!").withStyle(TextFormatting.BOLD);
        final float shiftTitleX = fr.width((ITextProperties)title) * 1.25f;
        renderStack.pushPose();
        renderStack.translate((double)(midX - shiftTitleX / 2.0f), (double)offsetY, 0.0);
        renderStack.scale(1.25f, 1.25f, 1.0f);
        fr.drawInBatch(title, 0.0f, 0.0f, -1, false, renderStack.last().pose(), (IRenderTypeBuffer)buffer, true, 0, LightmapHelper.getPackedFullbrightCoords());
        buffer.endBatch();
        renderStack.popPose();
        offsetY += (int)12.5f;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        offsetY += this.drawVoteChoices(renderStack, offsetX, offsetY, totalWidth, activeSession.getDirections());
        return offsetY;
    }
    
    private int drawVoteChoices(final MatrixStack renderStack, final int offsetX, final int offsetY, final int totalWidth, final List<DirectionChoice> choices) {
        final IRenderTypeBuffer.Impl buffer = IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuilder());
        final Minecraft mc = Minecraft.getInstance();
        final FontRenderer fr = mc.font;
        int maxHeight = 0;
        for (int i = 0; i < choices.size(); ++i) {
            final DirectionChoice choice = choices.get(i);
            final float offsetPart = (i + 0.5f) / choices.size();
            final float barMid = offsetX + offsetPart * totalWidth;
            int yShift = 0;
            final IReorderingProcessor bidiDir = choice.getDirectionDisplay().getVisualOrderText();
            final int dirLength = fr.width(bidiDir);
            fr.drawInBatch(bidiDir, barMid - dirLength / 2.0f, (float)(yShift + offsetY), -1, false, renderStack.last().pose(), (IRenderTypeBuffer)buffer, true, 0, LightmapHelper.getPackedFullbrightCoords());
            yShift += 9;
            float scaledShift = 0.0f;
            final float modifierScale = 0.75f;
            renderStack.pushPose();
            renderStack.translate((double)barMid, (double)(offsetY + yShift), 0.0);
            renderStack.scale(modifierScale, modifierScale, 1.0f);
            for (final VoteModifier modifier : choice.getFinalArchitectModifiers()) {
                final IReorderingProcessor bidiDesc = modifier.getDescription().getVisualOrderText();
                final int descLength = fr.width(bidiDesc);
                fr.drawInBatch(bidiDesc, -descLength / 2.0f, 0.0f, -1, false, renderStack.last().pose(), (IRenderTypeBuffer)buffer, true, 0, LightmapHelper.getPackedFullbrightCoords());
                renderStack.translate(0.0, 9.0, 0.0);
                scaledShift += 9.0f;
            }
            renderStack.popPose();
            yShift += MathHelper.ceil(scaledShift * modifierScale);
            if (yShift > maxHeight) {
                maxHeight = yShift;
            }
        }
        buffer.endBatch();
        return maxHeight;
    }
}
