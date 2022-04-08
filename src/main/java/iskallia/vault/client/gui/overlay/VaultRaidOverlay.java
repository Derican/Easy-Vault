package iskallia.vault.client.gui.overlay;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import iskallia.vault.client.ClientVaultRaidData;
import iskallia.vault.client.gui.helper.FontHelper;
import iskallia.vault.client.gui.helper.ScreenDrawHelper;
import iskallia.vault.client.gui.helper.UIHelper;
import iskallia.vault.network.message.VaultOverlayMessage;
import iskallia.vault.world.vault.modifier.TexturedVaultModifier;
import iskallia.vault.world.vault.modifier.VaultModifiers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class VaultRaidOverlay {
    public static final ResourceLocation RESOURCE = new ResourceLocation("the_vault", "textures/gui/vault-hud.png");
    public static final int PANIC_TICKS_THRESHOLD = 600;

    @SubscribeEvent
    public static void onRender(RenderGameOverlayEvent.Post event) {
        VaultOverlayMessage.OverlayType type = ClientVaultRaidData.getOverlayType();
        if (event.getType() != RenderGameOverlayEvent.ElementType.POTION_ICONS || type == VaultOverlayMessage.OverlayType.NONE) {
            return;
        }


        int remainingTicks = ClientVaultRaidData.getRemainingTicks();
        boolean canGetRecordTime = ClientVaultRaidData.canGetRecordTime();

        Minecraft minecraft = Minecraft.getInstance();
        MatrixStack matrixStack = event.getMatrixStack();
        int bottom = minecraft.getWindow().getGuiScaledHeight();
        int barWidth = 62;
        int hourglassWidth = 12;
        int hourglassHeight = 16;

        int color = -1;
        if (remainingTicks < 600) {
            if (remainingTicks % 10 < 5) {
                color = -65536;
            }
        } else if (canGetRecordTime) {
            color = -17664;
        }
        String timer = UIHelper.formatTimeString(remainingTicks);
        FontHelper.drawStringWithBorder(matrixStack, timer, (barWidth + 18), (bottom - 12), color, -16777216);

        minecraft.getTextureManager().bind(RESOURCE);
        RenderSystem.enableBlend();
        RenderSystem.disableDepthTest();

        matrixStack.pushPose();
        matrixStack.translate((barWidth + 30), (bottom - 25), 0.0D);
        if (remainingTicks < 600) {
            matrixStack.mulPose(Vector3f.ZP.rotationDegrees(remainingTicks * 10.0F % 360.0F));
        } else {
            matrixStack.mulPose(Vector3f.ZP.rotationDegrees((remainingTicks % 360)));
        }
        matrixStack.translate((-hourglassWidth / 2.0F), (-hourglassHeight / 2.0F), 0.0D);

        ScreenDrawHelper.drawQuad(buf -> ScreenDrawHelper.rect((IVertexBuilder) buf, matrixStack).dim(hourglassWidth, hourglassHeight).texVanilla(1.0F, 36.0F, hourglassWidth, hourglassHeight).draw());


        matrixStack.popPose();

        if (type == VaultOverlayMessage.OverlayType.VAULT) {
            renderVaultModifiers(event);
        }
        minecraft.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
    }

    public static void renderVaultModifiers(RenderGameOverlayEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        MatrixStack matrixStack = event.getMatrixStack();
        VaultModifiers modifiers = ClientVaultRaidData.getModifiers();

        int right = minecraft.getWindow().getGuiScaledWidth();
        int bottom = minecraft.getWindow().getGuiScaledHeight();

        int rightMargin = 28;
        int modifierSize = 24;
        int modifierGap = 2;

        modifiers.forEach((index, modifier) -> {
            if (!(modifier instanceof TexturedVaultModifier))
                return;
            minecraft.getTextureManager().bind(((TexturedVaultModifier) modifier).getIcon());
            int x = index.intValue() % 4;
            int y = index.intValue() / 4;
            int offsetX = modifierSize * x + modifierGap * Math.max(x - 1, 0);
            int offsetY = modifierSize * y + modifierGap * Math.max(y - 1, 0);
            AbstractGui.blit(matrixStack, right - rightMargin + modifierSize - offsetX, bottom - modifierSize - 2 - offsetY, 0.0F, 0.0F, modifierSize, modifierSize, modifierSize, modifierSize);
        });
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\overlay\VaultRaidOverlay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */