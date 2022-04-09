// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.client.gui.overlay.goal;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import iskallia.vault.client.ClientVaultRaidData;
import iskallia.vault.client.gui.helper.LightmapHelper;
import iskallia.vault.client.gui.helper.ScreenDrawHelper;
import iskallia.vault.client.gui.helper.UIHelper;
import iskallia.vault.client.vault.goal.ArchitectGoalData;
import iskallia.vault.client.vault.goal.VaultGoalData;
import iskallia.vault.network.message.VaultOverlayMessage;
import iskallia.vault.world.vault.logic.objective.architect.DirectionChoice;
import iskallia.vault.world.vault.logic.objective.architect.VotingSession;
import iskallia.vault.world.vault.logic.objective.architect.modifier.VoteModifier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(value = { Dist.CLIENT }, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ArchitectGoalVoteOverlay extends BossBarOverlay
{
    private static final ResourceLocation ARCHITECT_HUD;
    private final ArchitectGoalData data;
    
    public ArchitectGoalVoteOverlay(final ArchitectGoalData data) {
        this.data = data;
    }
    
    @SubscribeEvent
    public static void onArchitectBuild(final RenderGameOverlayEvent.Post event) {
        final VaultOverlayMessage.OverlayType type = ClientVaultRaidData.getOverlayType();
        if (event.getType() != RenderGameOverlayEvent.ElementType.HOTBAR || type != VaultOverlayMessage.OverlayType.VAULT) {
            return;
        }
        final Minecraft mc = Minecraft.getInstance();
        final VaultGoalData data = VaultGoalData.CURRENT_DATA;
        if (data instanceof ArchitectGoalData) {
            final ArchitectGoalData displayData = (ArchitectGoalData)data;
            final MatrixStack renderStack = event.getMatrixStack();
            final IRenderTypeBuffer.Impl buffer = IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuilder());
            final FontRenderer fr = mc.font;
            final int bottom = mc.getWindow().getGuiScaledHeight();
            final float part = displayData.getCompletedPercent();
            ITextComponent txt = (ITextComponent)new StringTextComponent("Build the vault!").withStyle(TextFormatting.AQUA);
            fr.drawInBatch(txt.getVisualOrderText(), 8.0f, (float)(bottom - 60), -1, true, renderStack.last().pose(), (IRenderTypeBuffer)buffer, false, 0, LightmapHelper.getPackedFullbrightCoords());
            final int lockTime = displayData.getTotalTicksUntilNextVote();
            final String duration = UIHelper.formatTimeString(lockTime);
            txt = (ITextComponent)new StringTextComponent("Vote Lock Time");
            fr.drawInBatch(txt.getVisualOrderText(), 8.0f, (float)(bottom - 42), -1, true, renderStack.last().pose(), (IRenderTypeBuffer)buffer, false, 0, LightmapHelper.getPackedFullbrightCoords());
            txt = (ITextComponent)new StringTextComponent(duration).withStyle((lockTime > 0) ? TextFormatting.RED : TextFormatting.GREEN);
            fr.drawInBatch(txt.getVisualOrderText(), 28.0f, (float)(bottom - 32), -1, true, renderStack.last().pose(), (IRenderTypeBuffer)buffer, false, 0, LightmapHelper.getPackedFullbrightCoords());
            buffer.endBatch();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            mc.getTextureManager().bind(ArchitectGoalVoteOverlay.ARCHITECT_HUD);
            ScreenDrawHelper.drawQuad(buf -> {
                ScreenDrawHelper.rect((IVertexBuilder)buf, renderStack).at(15.0f, (float)(bottom - 51)).dim(54.0f, 7.0f).texVanilla(0.0f, 105.0f, 54.0f, 7.0f).draw();
                ScreenDrawHelper.rect((IVertexBuilder)buf, renderStack).at(16.0f, (float)(bottom - 50)).dim(52.0f * part, 5.0f).texVanilla(0.0f, 113.0f, 52.0f * part, 5.0f).draw();
                return;
            });
        }
        mc.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
    }
    
    @Override
    public boolean shouldDisplay() {
        return this.data.getTicksUntilNextVote() > 0 || (this.data.getActiveSession() != null && !this.data.getActiveSession().getDirections().isEmpty());
    }
    
    @Override
    public int drawOverlay(final MatrixStack renderStack, final float pTicks) {
        final VotingSession activeSession = this.data.getActiveSession();
        if (!this.shouldDisplay()) {
            return 0;
        }
        final Minecraft mc = Minecraft.getInstance();
        int offsetY = 5;
        if (this.data.getTicksUntilNextVote() > 0) {
            offsetY = this.drawVotingTimer(this.data.getTicksUntilNextVote(), renderStack, offsetY);
        }
        if (activeSession != null && !activeSession.getDirections().isEmpty()) {
            offsetY = this.drawVotingSession(activeSession, renderStack, offsetY);
        }
        mc.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
        return offsetY;
    }
    
    private int drawVotingTimer(final int ticksUntilNextVote, final MatrixStack renderStack, final int offsetY) {
        final IRenderTypeBuffer.Impl buffer = IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuilder());
        final Minecraft mc = Minecraft.getInstance();
        final FontRenderer fr = mc.font;
        final int midX = mc.getWindow().getGuiScaledWidth() / 2;
        final float scale = 1.25f;
        final String tplText = "Voting locked: ";
        final String text = tplText + UIHelper.formatTimeString(ticksUntilNextVote);
        final float shift = fr.width(tplText + "00:00") * 1.25f;
        final ITextComponent textCmp = (ITextComponent)new StringTextComponent(text).withStyle(TextFormatting.RED);
        renderStack.pushPose();
        renderStack.translate((double)(midX - shift / 2.0f), (double)offsetY, 0.0);
        renderStack.scale(scale, scale, 1.0f);
        fr.drawInBatch(textCmp, 0.0f, 0.0f, -1, false, renderStack.last().pose(), (IRenderTypeBuffer)buffer, true, 0, LightmapHelper.getPackedFullbrightCoords());
        buffer.endBatch();
        renderStack.popPose();
        return offsetY + 13;
    }
    
    private int drawVotingSession(final VotingSession activeSession, final MatrixStack renderStack, int offsetY) {
        final IRenderTypeBuffer.Impl buffer = IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuilder());
        final Minecraft mc = Minecraft.getInstance();
        final FontRenderer fr = mc.font;
        final int midX = mc.getWindow().getGuiScaledWidth() / 2;
        final int segmentWidth = 8;
        final int barSegments = 22;
        final int startEndWith = 4;
        final int barWidth = segmentWidth * barSegments;
        final int totalWidth = barWidth + startEndWith * 2;
        final int offsetX = midX - totalWidth / 2;
        final Map<DirectionChoice, Float> barParts = new LinkedHashMap<DirectionChoice, Float>();
        for (final DirectionChoice choice : activeSession.getDirections()) {
            barParts.put(choice, activeSession.getChoicePercentage(choice));
        }
        final float shiftTitleX = fr.width("Vote! 00:00") * 1.25f;
        final String timeRemainingStr = UIHelper.formatTimeString(activeSession.getRemainingVoteTicks());
        final ITextComponent title = (ITextComponent)new StringTextComponent("Vote! ").append(timeRemainingStr).withStyle(TextFormatting.AQUA);
        renderStack.pushPose();
        renderStack.translate((double)(midX - shiftTitleX / 2.0f), (double)offsetY, 0.0);
        renderStack.scale(1.25f, 1.25f, 1.0f);
        fr.drawInBatch(title, 0.0f, 0.0f, -1, false, renderStack.last().pose(), (IRenderTypeBuffer)buffer, true, 0, LightmapHelper.getPackedFullbrightCoords());
        buffer.endBatch();
        renderStack.popPose();
        offsetY += (int)12.5f;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        mc.getTextureManager().bind(ArchitectGoalVoteOverlay.ARCHITECT_HUD);
        this.drawBarContent(renderStack, offsetX + 1, offsetY + 1, barWidth, barParts);
        this.drawBarFrame(renderStack, offsetX, offsetY);
        offsetY += 12;
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
            final IReorderingProcessor bidiDir = choice.getDirectionDisplay("/").getVisualOrderText();
            final int dirLength = fr.width(bidiDir);
            fr.drawInBatch(bidiDir, barMid - dirLength / 2.0f, (float)(yShift + offsetY), -1, false, renderStack.last().pose(), (IRenderTypeBuffer)buffer, true, 0, LightmapHelper.getPackedFullbrightCoords());
            yShift += 9;
            float scaledShift = 0.0f;
            final float modifierScale = 0.75f;
            renderStack.pushPose();
            renderStack.translate((double)barMid, (double)(offsetY + yShift), 0.0);
            renderStack.scale(modifierScale, modifierScale, 1.0f);
            for (final VoteModifier modifier : choice.getModifiers()) {
                final int changeSeconds = modifier.getVoteLockDurationChangeSeconds();
                if (changeSeconds != 0) {
                    final TextFormatting color = (changeSeconds > 0) ? TextFormatting.RED : TextFormatting.GREEN;
                    final String changeDesc = (changeSeconds > 0) ? ("+" + changeSeconds) : String.valueOf(changeSeconds);
                    final ITextComponent line = (ITextComponent)new StringTextComponent(changeDesc + "s Vote Lock").withStyle(color);
                    final int voteLockLength = fr.width((ITextProperties)line);
                    fr.drawInBatch(line.getVisualOrderText(), -voteLockLength / 2.0f, 0.0f, -1, false, renderStack.last().pose(), (IRenderTypeBuffer)buffer, true, 0, LightmapHelper.getPackedFullbrightCoords());
                    renderStack.translate(0.0, 9.0, 0.0);
                    scaledShift += 9.0f;
                }
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
    
    private void drawBarContent(final MatrixStack renderStack, final int offsetX, final int offsetY, final int barWidth, final Map<DirectionChoice, Float> barParts) {
        ScreenDrawHelper.drawQuad(buf -> {
            float drawX = (float)offsetX;
            DirectionChoice lastChoice = null;
            boolean drawStart = true;
            Iterator iterator=barParts.keySet().iterator();
            while (iterator.hasNext()) {
                final DirectionChoice choice = (DirectionChoice) iterator.next();
                float part = barParts.get(choice) * barWidth;
                final int vOffset = DirectionChoice.getVOffset(choice.getDirection());
                lastChoice = choice;
                if (drawStart) {
                    ScreenDrawHelper.rect((IVertexBuilder)buf, renderStack).at((float)offsetX, (float)offsetY).dim(3.0f, 8.0f).texVanilla(0.0f, (float)vOffset, 3.0f, 8.0f).draw();
                    drawX += 3.0f;
                    drawStart = false;
                }
                while (part > 0.0f) {
                    final float length = Math.min(8.0f, part);
                    part -= length;
                    ScreenDrawHelper.rect((IVertexBuilder)buf, renderStack).at(drawX, (float)offsetY).dim(length, 8.0f).texVanilla(100.0f, (float)vOffset, length, 8.0f).draw();
                    drawX += length;
                }
            }
            final int vOffset2 = DirectionChoice.getVOffset(lastChoice.getDirection());
            ScreenDrawHelper.rect((IVertexBuilder)buf, renderStack).at(drawX, (float)offsetY).dim(3.0f, 8.0f).texVanilla(96.0f, (float)vOffset2, 3.0f, 8.0f).draw();
        });
    }
    
    private void drawBarFrame(final MatrixStack renderStack, final int offsetX, final int offsetY) {
        renderStack.pushPose();
        ScreenDrawHelper.drawQuad(buf -> {
            ScreenDrawHelper.rect((IVertexBuilder)buf, renderStack).at((float)offsetX, (float)offsetY).dim(4.0f, 10.0f).texVanilla(0.0f, 11.0f, 4.0f, 10.0f).draw();
            int barOffsetX = offsetX + 4;
            for (int i = 0; i < 22; ++i) {
                ScreenDrawHelper.rect((IVertexBuilder)buf, renderStack).at((float)barOffsetX, (float)offsetY).dim(8.0f, 10.0f).texVanilla(0.0f, 0.0f, 8.0f, 10.0f).draw();
                barOffsetX += 8;
            }
            ScreenDrawHelper.rect((IVertexBuilder)buf, renderStack).at((float)barOffsetX, (float)offsetY).dim(4.0f, 10.0f).texVanilla(97.0f, 11.0f, 4.0f, 10.0f).draw();
            return;
        });
        renderStack.popPose();
    }
    
    static {
        ARCHITECT_HUD = new ResourceLocation("the_vault", "textures/gui/architect_event_bar.png");
    }
}
