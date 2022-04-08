package iskallia.vault.client.gui.overlay;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import iskallia.vault.client.ClientDamageData;
import iskallia.vault.client.gui.helper.ScreenDrawHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.text.DecimalFormat;

@OnlyIn(Dist.CLIENT)
public class PlayerDamageOverlay {
    private static final ResourceLocation STRENGTH_ICON = new ResourceLocation("minecraft", "textures/mob_effect/strength.png");

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void setupHealthTexture(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.FOOD) {
            return;
        }
        ClientPlayerEntity clientPlayerEntity = (Minecraft.getInstance()).player;
        if (clientPlayerEntity == null) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (!mc.gameMode.hasExperience()) {
            return;
        }

        float multiplier = ClientDamageData.getCurrentDamageMultiplier();
        if (Math.abs(multiplier - 1.0F) < 0.001D) {
            return;
        }

        DecimalFormat format = new DecimalFormat("0");
        float value = (multiplier - 1.0F) * 100.0F;
        String displayStr = format.format(value);
        if (value >= 0.0F) {
            displayStr = "+" + displayStr;
        }
        displayStr = displayStr + "%";
        TextFormatting color = (value < 0.0F) ? TextFormatting.RED : TextFormatting.DARK_GREEN;

        IFormattableTextComponent iFormattableTextComponent = (new StringTextComponent(displayStr)).withStyle(color);

        ForgeIngameGui.left_height += 6;
        int left = mc.getWindow().getGuiScaledWidth() / 2 - 91;
        int top = mc.getWindow().getGuiScaledHeight() - ForgeIngameGui.left_height;

        MatrixStack matrixStack = event.getMatrixStack();
        mc.getTextureManager().bind(STRENGTH_ICON);
        matrixStack.pushPose();
        matrixStack.translate(left, top, 0.0D);

        ScreenDrawHelper.drawQuad(buf -> ScreenDrawHelper.rect((IVertexBuilder) buf, matrixStack).dim(16.0F, 16.0F).draw());


        matrixStack.translate(16.0D, 4.0D, 0.0D);

        mc.font.drawShadow(matrixStack, (ITextComponent) iFormattableTextComponent, 0.0F, 0.0F, 16777215);

        matrixStack.popPose();
        mc.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\overlay\PlayerDamageOverlay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */