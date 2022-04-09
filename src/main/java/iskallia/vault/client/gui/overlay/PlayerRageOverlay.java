// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.client.gui.overlay;

import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.Vault;
import iskallia.vault.util.PlayerRageHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

@OnlyIn(Dist.CLIENT)
public class PlayerRageOverlay
{
    private static final ResourceLocation OVERLAY_ICONS;
    
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void setupHealthTexture(final RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.EXPERIENCE) {
            return;
        }
        final PlayerEntity player = (PlayerEntity)Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        final Minecraft mc = Minecraft.getInstance();
        if (!mc.gameMode.hasExperience()) {
            return;
        }
        final int rage = PlayerRageHelper.getCurrentRage(player, LogicalSide.CLIENT);
        if (rage <= 0) {
            return;
        }
        final int scaledWidth = event.getWindow().getGuiScaledWidth();
        final int scaledHeight = event.getWindow().getGuiScaledHeight();
        final MatrixStack matrixStack = event.getMatrixStack();
        final int offsetX = scaledWidth / 2 - 91;
        final int offsetY = scaledHeight - 32 + 3;
        final int width = Math.round(182.0f * (rage / 100.0f));
        final int height = 5;
        final int uOffset = 0;
        final int vOffset = 64;
        mc.getTextureManager().bind(PlayerRageOverlay.OVERLAY_ICONS);
        AbstractGui.blit(matrixStack, offsetX, offsetY, 0, (float)uOffset, (float)vOffset, width, height, 256, 256);
        mc.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
    }
    
    static {
        OVERLAY_ICONS = Vault.id("textures/gui/overlay_icons.png");
    }
}
