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
import net.minecraft.util.text.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(value = {Dist.CLIENT}, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ArchitectGoalVoteOverlay extends BossBarOverlay {
    private static final ResourceLocation ARCHITECT_HUD = new ResourceLocation("the_vault", "textures/gui/architect_event_bar.png");

    private final ArchitectGoalData data;

    public ArchitectGoalVoteOverlay(ArchitectGoalData data) {
        this.data = data;
    }

    @SubscribeEvent
    public static void onArchitectBuild(RenderGameOverlayEvent.Post event) {
        VaultOverlayMessage.OverlayType type = ClientVaultRaidData.getOverlayType();
        if (event.getType() != RenderGameOverlayEvent.ElementType.HOTBAR || type != VaultOverlayMessage.OverlayType.VAULT) {
            return;
        }


        Minecraft mc = Minecraft.getInstance();
        VaultGoalData data = VaultGoalData.CURRENT_DATA;
        if (data instanceof ArchitectGoalData) {
            ArchitectGoalData displayData = (ArchitectGoalData) data;
            MatrixStack renderStack = event.getMatrixStack();
            IRenderTypeBuffer.Impl buffer = IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuilder());
            FontRenderer fr = mc.font;
            int bottom = mc.getWindow().getGuiScaledHeight();
            float part = displayData.getCompletedPercent();

            IFormattableTextComponent iFormattableTextComponent2 = (new StringTextComponent("Build the vault!")).withStyle(TextFormatting.AQUA);
            fr.drawInBatch(iFormattableTextComponent2.getVisualOrderText(), 8.0F, (bottom - 60), -1, true, renderStack.last().pose(), (IRenderTypeBuffer) buffer, false, 0,
                    LightmapHelper.getPackedFullbrightCoords());

            int lockTime = displayData.getTotalTicksUntilNextVote();
            String duration = UIHelper.formatTimeString(lockTime);
            StringTextComponent stringTextComponent = new StringTextComponent("Vote Lock Time");
            fr.drawInBatch(stringTextComponent.getVisualOrderText(), 8.0F, (bottom - 42), -1, true, renderStack.last().pose(), (IRenderTypeBuffer) buffer, false, 0,
                    LightmapHelper.getPackedFullbrightCoords());
            IFormattableTextComponent iFormattableTextComponent1 = (new StringTextComponent(duration)).withStyle((lockTime > 0) ? TextFormatting.RED : TextFormatting.GREEN);
            fr.drawInBatch(iFormattableTextComponent1.getVisualOrderText(), 28.0F, (bottom - 32), -1, true, renderStack.last().pose(), (IRenderTypeBuffer) buffer, false, 0,
                    LightmapHelper.getPackedFullbrightCoords());
            buffer.endBatch();

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            mc.getTextureManager().bind(ARCHITECT_HUD);
            ScreenDrawHelper.drawQuad(buf -> {
                ScreenDrawHelper.rect((IVertexBuilder) buf, renderStack).at(15.0F, (bottom - 51)).dim(54.0F, 7.0F).texVanilla(0.0F, 105.0F, 54.0F, 7.0F).draw();


                ScreenDrawHelper.rect((IVertexBuilder) buf, renderStack).at(16.0F, (bottom - 50)).dim(52.0F * part, 5.0F).texVanilla(0.0F, 113.0F, 52.0F * part, 5.0F).draw();
            });
        }


        mc.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
    }


    public boolean shouldDisplay() {
        return (this.data.getTicksUntilNextVote() > 0 || (this.data
                .getActiveSession() != null && !this.data.getActiveSession().getDirections().isEmpty()));
    }


    public int drawOverlay(MatrixStack renderStack, float pTicks) {
        VotingSession activeSession = this.data.getActiveSession();
        if (!shouldDisplay()) {
            return 0;
        }
        Minecraft mc = Minecraft.getInstance();

        int offsetY = 5;
        if (this.data.getTicksUntilNextVote() > 0) {
            offsetY = drawVotingTimer(this.data.getTicksUntilNextVote(), renderStack, offsetY);
        }
        if (activeSession != null && !activeSession.getDirections().isEmpty()) {
            offsetY = drawVotingSession(activeSession, renderStack, offsetY);
        }

        mc.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
        return offsetY;
    }

    private int drawVotingTimer(int ticksUntilNextVote, MatrixStack renderStack, int offsetY) {
        IRenderTypeBuffer.Impl buffer = IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuilder());
        Minecraft mc = Minecraft.getInstance();
        FontRenderer fr = mc.font;
        int midX = mc.getWindow().getGuiScaledWidth() / 2;

        float scale = 1.25F;

        String tplText = "Voting locked: ";
        String text = tplText + UIHelper.formatTimeString(ticksUntilNextVote);
        float shift = fr.width(tplText + "00:00") * 1.25F;
        IFormattableTextComponent iFormattableTextComponent = (new StringTextComponent(text)).withStyle(TextFormatting.RED);

        renderStack.pushPose();
        renderStack.translate((midX - shift / 2.0F), offsetY, 0.0D);
        renderStack.scale(scale, scale, 1.0F);
        fr.drawInBatch((ITextComponent) iFormattableTextComponent, 0.0F, 0.0F, -1, false, renderStack
                .last().pose(), (IRenderTypeBuffer) buffer, true, 0, LightmapHelper.getPackedFullbrightCoords());
        buffer.endBatch();
        renderStack.popPose();

        return offsetY + 13;
    }

    private int drawVotingSession(VotingSession activeSession, MatrixStack renderStack, int offsetY) {
        IRenderTypeBuffer.Impl buffer = IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuilder());
        Minecraft mc = Minecraft.getInstance();
        FontRenderer fr = mc.font;
        int midX = mc.getWindow().getGuiScaledWidth() / 2;

        int segmentWidth = 8;
        int barSegments = 22;
        int startEndWith = 4;
        int barWidth = segmentWidth * barSegments;
        int totalWidth = barWidth + startEndWith * 2;

        int offsetX = midX - totalWidth / 2;

        Map<DirectionChoice, Float> barParts = new LinkedHashMap<>();
        for (DirectionChoice choice : activeSession.getDirections()) {
            barParts.put(choice, Float.valueOf(activeSession.getChoicePercentage(choice)));
        }

        float shiftTitleX = fr.width("Vote! 00:00") * 1.25F;
        String timeRemainingStr = UIHelper.formatTimeString(activeSession.getRemainingVoteTicks());
        IFormattableTextComponent iFormattableTextComponent = (new StringTextComponent("Vote! ")).append(timeRemainingStr).withStyle(TextFormatting.AQUA);

        renderStack.pushPose();
        renderStack.translate((midX - shiftTitleX / 2.0F), offsetY, 0.0D);
        renderStack.scale(1.25F, 1.25F, 1.0F);
        fr.drawInBatch((ITextComponent) iFormattableTextComponent, 0.0F, 0.0F, -1, false, renderStack
                .last().pose(), (IRenderTypeBuffer) buffer, true, 0, LightmapHelper.getPackedFullbrightCoords());
        buffer.endBatch();
        renderStack.popPose();
        offsetY = (int) (offsetY + 12.5F);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        mc.getTextureManager().bind(ARCHITECT_HUD);
        drawBarContent(renderStack, offsetX + 1, offsetY + 1, barWidth, barParts);
        drawBarFrame(renderStack, offsetX, offsetY);
        offsetY += 12;
        offsetY += drawVoteChoices(renderStack, offsetX, offsetY, totalWidth, activeSession.getDirections());
        return offsetY;
    }

    private int drawVoteChoices(MatrixStack renderStack, int offsetX, int offsetY, int totalWidth, List<DirectionChoice> choices) {
        IRenderTypeBuffer.Impl buffer = IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuilder());
        Minecraft mc = Minecraft.getInstance();
        FontRenderer fr = mc.font;

        int maxHeight = 0;

        for (int i = 0; i < choices.size(); i++) {
            DirectionChoice choice = choices.get(i);
            float offsetPart = (i + 0.5F) / choices.size();
            float barMid = offsetX + offsetPart * totalWidth;
            int yShift = 0;

            IReorderingProcessor bidiDir = choice.getDirectionDisplay("/").getVisualOrderText();
            int dirLength = fr.width(bidiDir);
            fr.drawInBatch(bidiDir, barMid - dirLength / 2.0F, (yShift + offsetY), -1, false, renderStack
                    .last().pose(), (IRenderTypeBuffer) buffer, true, 0, LightmapHelper.getPackedFullbrightCoords());
            yShift += 9;

            float scaledShift = 0.0F;
            float modifierScale = 0.75F;
            renderStack.pushPose();
            renderStack.translate(barMid, (offsetY + yShift), 0.0D);
            renderStack.scale(modifierScale, modifierScale, 1.0F);
            for (VoteModifier modifier : choice.getModifiers()) {
                int changeSeconds = modifier.getVoteLockDurationChangeSeconds();
                if (changeSeconds != 0) {
                    TextFormatting color = (changeSeconds > 0) ? TextFormatting.RED : TextFormatting.GREEN;
                    String changeDesc = (changeSeconds > 0) ? ("+" + changeSeconds) : String.valueOf(changeSeconds);
                    IFormattableTextComponent iFormattableTextComponent = (new StringTextComponent(changeDesc + "s Vote Lock")).withStyle(color);

                    int voteLockLength = fr.width((ITextProperties) iFormattableTextComponent);
                    fr.drawInBatch(iFormattableTextComponent.getVisualOrderText(), -voteLockLength / 2.0F, 0.0F, -1, false, renderStack
                            .last().pose(), (IRenderTypeBuffer) buffer, true, 0, LightmapHelper.getPackedFullbrightCoords());
                    renderStack.translate(0.0D, 9.0D, 0.0D);
                    scaledShift += 9.0F;
                }

                IReorderingProcessor bidiDesc = modifier.getDescription().getVisualOrderText();
                int descLength = fr.width(bidiDesc);
                fr.drawInBatch(bidiDesc, -descLength / 2.0F, 0.0F, -1, false, renderStack
                        .last().pose(), (IRenderTypeBuffer) buffer, true, 0, LightmapHelper.getPackedFullbrightCoords());
                renderStack.translate(0.0D, 9.0D, 0.0D);
                scaledShift += 9.0F;
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

    private void drawBarContent(MatrixStack renderStack, int offsetX, int offsetY, int barWidth, Map<DirectionChoice, Float> barParts) {
        ScreenDrawHelper.drawQuad(buf -> {
            float drawX = offsetX;
            DirectionChoice lastChoice = null;
            boolean drawStart = true;
            for (DirectionChoice choice : barParts.keySet()) {
                float part = ((Float) barParts.get(choice)).floatValue() * barWidth;
                int i = DirectionChoice.getVOffset(choice.getDirection());
                lastChoice = choice;
                if (drawStart) {
                    ScreenDrawHelper.rect((IVertexBuilder) buf, renderStack).at(offsetX, offsetY).dim(3.0F, 8.0F).texVanilla(0.0F, i, 3.0F, 8.0F).draw();
                    drawX += 3.0F;
                    drawStart = false;
                }
                while (part > 0.0F) {
                    float length = Math.min(8.0F, part);
                    part -= length;
                    ScreenDrawHelper.rect((IVertexBuilder) buf, renderStack).at(drawX, offsetY).dim(length, 8.0F).texVanilla(100.0F, i, length, 8.0F).draw();
                    drawX += length;
                }
            }
            int vOffset = DirectionChoice.getVOffset(lastChoice.getDirection());
            ScreenDrawHelper.rect((IVertexBuilder) buf, renderStack).at(drawX, offsetY).dim(3.0F, 8.0F).texVanilla(96.0F, vOffset, 3.0F, 8.0F).draw();
        });
    }


    private void drawBarFrame(MatrixStack renderStack, int offsetX, int offsetY) {
        renderStack.pushPose();
        ScreenDrawHelper.drawQuad(buf -> {
            ScreenDrawHelper.rect((IVertexBuilder) buf, renderStack).at(offsetX, offsetY).dim(4.0F, 10.0F).texVanilla(0.0F, 11.0F, 4.0F, 10.0F).draw();


            int barOffsetX = offsetX + 4;


            for (int i = 0; i < 22; i++) {
                ScreenDrawHelper.rect((IVertexBuilder) buf, renderStack).at(barOffsetX, offsetY).dim(8.0F, 10.0F).texVanilla(0.0F, 0.0F, 8.0F, 10.0F).draw();


                barOffsetX += 8;
            }


            ScreenDrawHelper.rect((IVertexBuilder) buf, renderStack).at(barOffsetX, offsetY).dim(4.0F, 10.0F).texVanilla(97.0F, 11.0F, 4.0F, 10.0F).draw();
        });


        renderStack.popPose();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\overlay\goal\ArchitectGoalVoteOverlay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */