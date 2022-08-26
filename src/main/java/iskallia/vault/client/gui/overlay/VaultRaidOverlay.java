package iskallia.vault.client.gui.overlay;

import net.minecraft.client.renderer.BufferBuilder;
import iskallia.vault.world.vault.modifier.VaultModifier;
import iskallia.vault.world.vault.modifier.VaultModifiers;
import iskallia.vault.util.ListHelper;
import iskallia.vault.world.vault.modifier.TexturedVaultModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.AbstractGui;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import iskallia.vault.client.gui.helper.ScreenDrawHelper;
import net.minecraft.util.math.vector.Vector3f;
import com.mojang.blaze3d.systems.RenderSystem;
import iskallia.vault.client.gui.helper.FontHelper;
import iskallia.vault.client.gui.helper.UIHelper;
import net.minecraft.client.Minecraft;
import iskallia.vault.network.message.VaultOverlayMessage;
import iskallia.vault.client.ClientVaultRaidData;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class VaultRaidOverlay {
    public static final ResourceLocation RESOURCE;
    public static final int PANIC_TICKS_THRESHOLD = 600;

    @SubscribeEvent
    public static void onRender(final RenderGameOverlayEvent.Post event) {
        final VaultOverlayMessage.OverlayType type = ClientVaultRaidData.getOverlayType();
        if (event.getType() != RenderGameOverlayEvent.ElementType.POTION_ICONS || type == VaultOverlayMessage.OverlayType.NONE) {
            return;
        }
        final int remainingTicks = ClientVaultRaidData.getRemainingTicks();
        final boolean canGetRecordTime = ClientVaultRaidData.canGetRecordTime();
        final Minecraft minecraft = Minecraft.getInstance();
        final MatrixStack matrixStack = event.getMatrixStack();
        final int bottom = minecraft.getWindow().getGuiScaledHeight();
        final int barWidth = 62;
        final int hourglassWidth = 12;
        final int hourglassHeight = 16;
        int color = -1;
        if (remainingTicks < 600) {
            if (remainingTicks % 10 < 5) {
                color = -65536;
            }
        } else if (canGetRecordTime) {
            color = -17664;
        }
        final String timer = UIHelper.formatTimeString(remainingTicks);
        if (ClientVaultRaidData.showTimer()) {
            FontHelper.drawStringWithBorder(matrixStack, timer, (float) (barWidth + 18), (float) (bottom - 12), color, -16777216);
        }
        minecraft.getTextureManager().bind(VaultRaidOverlay.RESOURCE);
        RenderSystem.enableBlend();
        RenderSystem.disableDepthTest();
        matrixStack.pushPose();
        if (ClientVaultRaidData.showTimer()) {
            matrixStack.translate(barWidth + 30, bottom - 25, 0.0);
            if (remainingTicks < 600) {
                matrixStack.mulPose(Vector3f.ZP.rotationDegrees(remainingTicks * 10.0f % 360.0f));
            } else {
                matrixStack.mulPose(Vector3f.ZP.rotationDegrees((float) (remainingTicks % 360)));
            }
            matrixStack.translate(-hourglassWidth / 2.0f, -hourglassHeight / 2.0f, 0.0);
            ScreenDrawHelper.drawQuad(buf -> ScreenDrawHelper.rect(buf, matrixStack).dim((float) hourglassWidth, (float) hourglassHeight).texVanilla(1.0f, 36.0f, (float) hourglassWidth, (float) hourglassHeight).draw());
        }
        matrixStack.popPose();
        if (type == VaultOverlayMessage.OverlayType.VAULT) {
            renderVaultModifiers(event);
        }
        minecraft.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
    }

    public static void renderVaultModifiers(final RenderGameOverlayEvent.Post event) {
        final Minecraft minecraft = Minecraft.getInstance();
        final MatrixStack matrixStack = event.getMatrixStack();
        final VaultModifiers modifiers = ClientVaultRaidData.getModifiers();
        final int right = minecraft.getWindow().getGuiScaledWidth();
        final int bottom = minecraft.getWindow().getGuiScaledHeight();
        final int rightMargin = 28;
        final int modifierSize = 24;
        final int modifierGap = 2;
        ListHelper.traverseOccurrences(modifiers, (index, modifier, occurrence) -> {
            if (modifier instanceof TexturedVaultModifier) {
                minecraft.getTextureManager().bind(((TexturedVaultModifier) modifier).getIcon());
                final int x = index % 4;
                final int y = index / 4;
                final int offsetX = modifierSize * x + modifierGap * Math.max(x - 1, 0);
                final int offsetY = modifierSize * y + modifierGap * Math.max(y - 1, 0);
                final int posX = right - (rightMargin + modifierSize) - offsetX;
                final int posY = bottom - modifierSize - 2 - offsetY;
                AbstractGui.blit(matrixStack, posX, posY, 0.0f, 0.0f, modifierSize, modifierSize, modifierSize, modifierSize);
                if (occurrence > 1L) {
                    final String text = String.valueOf(occurrence);
                    final int textWidth = minecraft.font.width(text);
                    minecraft.font.drawShadow(matrixStack, text, (float) (posX + (modifierSize - textWidth)), (float) (posY + (modifierSize - 10)), -1);
                }
            }
        });
    }

    static {
        RESOURCE = new ResourceLocation("the_vault", "textures/gui/vault-hud.png");
    }
}
